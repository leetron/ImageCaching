package com.luclx.nab.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.Toast;

import com.luclx.nab.NABApplication;
import com.luclx.nab.R;
import com.luclx.nab.utils.ObservableUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LucLX on 3/18/17.
 */
public class MainActivity
        extends AbstractActivity {
    private static final String URL_FILE = "https://drive.google.com/uc?export=download&id=0B1lYcD-2KM4XSjVQQ09ObXltMm8";
    private static final String ZIP_FILE = "data.zip";
    private static final int MY_PERMISSIONS_REQUEST = 1000;

    Observable<Integer> downloadObservable;
    Observable<Integer> unzipObservable;
    Observable<Integer> saveLocalObservable;
    Disposable disposable;
    private File zipFile;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initImageLoader() {
        // do nothing
    }

    @Override
    protected void onViewReady() {
        Button btnStart = (Button) findViewById(R.id.start);
        btnStart.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, DemoActivity.class))
        );

        zipFile = new File(Environment.getExternalStorageDirectory(), ZIP_FILE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
            requestData();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                            , Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestData();
                } else {

                }
                return;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * download, unzip, save data to local
     */
    private void requestData() {
        if (NABApplication.getInstance().getDatabase().noDataExist()) {
            downloadObservable = downloadTask();
            unzipObservable = unzipTask().startWith(downloadObservable);
            saveLocalObservable = saveLocalTask().startWith(unzipObservable);
            disposable = saveLocalObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (progress) -> mProgressDialog.setProgress(progress)
                            , (e) -> {
                                hideDialog();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }, () -> {
                                hideDialog();
                                Toast.makeText(this, "Setup completed!", Toast.LENGTH_LONG).show();
                            });
        }
    }

    /**
     * create observable
     *
     * @return
     */
    private Observable<Integer> downloadTask() {
        showDialog("Setup data...");
        return ObservableUtils.downloadFile(URL_FILE, zipFile.getAbsolutePath());
    }

    /**
     * unzip file
     *
     * @return
     */
    private Observable<Integer> unzipTask() {
        return ObservableUtils.requestUnZip(zipFile.getAbsolutePath());
    }

    /**
     * save local
     *
     * @return
     */
    private Observable<Integer> saveLocalTask() {
        return ObservableUtils.requestSaveLocal();
    }

}
