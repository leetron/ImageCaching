package com.luclx.nab.utils;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LucLX on 3/18/17.
 */

public class FileUtils {

    /**
     * unzip file
     *
     * @param path
     * @param unzipDestination
     */
    @SuppressWarnings("rawtypes")
    public static void unZip(String path, String unzipDestination) {
        try {
            int BUFFER = 2048;
            List<String> zipFiles = new ArrayList<String>();
            File sourceZipFile = new File(path);
            File unzipDestinationDirectory = new File(unzipDestination);
            unzipDestinationDirectory.mkdir();
            ZipFile zipFile;
            zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                if (currentEntry.endsWith(".zip")) {
                    zipFiles.add(destFile.getAbsolutePath());
                }

                File destinationParent = destFile.getParentFile();

                destinationParent.mkdirs();

                try {
                    if (!entry.isDirectory()) {
                        BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            zipFile.close();
            //delete zip file
            if (sourceZipFile.exists()) {
                sourceZipFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * download file from url
     *
     * @param url
     * @param destination
     * @return
     */
    public static Observable<Integer> downloadFile(String url, String destination) {
        return Observable.create(emitter -> {
            final OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().url(url).build();
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                Response response = client.newCall(request).execute();
                inputStream = response.body().byteStream();
                fileOutputStream = new FileOutputStream(new File(destination));
                long totalSize = response.body().contentLength();

                byte[] buffer = new byte[2 * 1024];
                int len;
                int readLen = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    readLen += len;
                    int progress = (int) ((readLen * 50) / totalSize);
                    emitter.onNext(progress);
                    Thread.sleep(500);
                }

            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(new Throwable("Error"));
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            emitter.onComplete();
        });

    }

    /**
     * get url from file
     *
     * @param filePath
     * @return
     */
    public static ArrayList<String> getURL(String filePath) {
        ArrayList<String> urlList = new ArrayList<>();
        try {
            File yourFile = new File(filePath);
            FileInputStream stream = new FileInputStream(yourFile);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Getting data JSON Array nodes
            JSONArray data = new JSONArray(jsonStr);

            // looping through All child
            for (int i = 0; i < data.length(); i++) {
                urlList.add((String) data.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlList;
    }
}
