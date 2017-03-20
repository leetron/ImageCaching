package com.luclx.nab.utils;

import android.os.Environment;

import com.luclx.nab.NABApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LucLX on 3/18/17.
 */

public class ObservableUtils {

    /**
     * download file from url
     *
     * @param url
     * @param destination
     * @return
     */
    public static Observable<Integer> downloadFile(String url, String destination) {
        return Observable.create(emitter -> {
            final OkHttpClient client = NABApplication.getInstance().getOkHttpClient();
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
     * request unzip file
     *
     * @param destination
     * @return
     */
    public static Observable<Integer> requestUnZip(String destination) {
        return Observable.create(emitter -> {
            FileUtils.unZip(destination, Environment.getExternalStorageDirectory() + "");
            emitter.onNext(70);
            emitter.onComplete();
        });
    }

    /**
     * request save data local
     *
     * @return
     */
    public static Observable<Integer> requestSaveLocal() {
        return Observable.create(emitter -> {
            ArrayList<String> list0 = FileUtils.getURL(Environment.getExternalStorageDirectory() + "/JSON files/images0.json");
            emitter.onNext(75);
            ArrayList<String> list1 = FileUtils.getURL(Environment.getExternalStorageDirectory() + "/JSON files/images1.json");
            emitter.onNext(80);
            ArrayList<String> list2 = FileUtils.getURL(Environment.getExternalStorageDirectory() + "/JSON files/images2.json");
            emitter.onNext(85);
            NABApplication.getInstance().getDatabase().addURLs(list0, 0);
            emitter.onNext(90);
            NABApplication.getInstance().getDatabase().addURLs(list1, 1);
            emitter.onNext(95);
            NABApplication.getInstance().getDatabase().addURLs(list2, 2);
            emitter.onNext(100);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            emitter.onComplete();
        });
    }
}
