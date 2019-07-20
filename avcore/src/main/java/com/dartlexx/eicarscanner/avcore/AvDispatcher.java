package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.avcore.apps.AppScanner;
import com.dartlexx.eicarscanner.avcore.files.FileScanner;
import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AvDispatcher {

    @NonNull
    private final AppScanner mAppScanner;

    @NonNull
    private final FileScanner mFileScanner;

    private ExecutorService mExecutor;

    AvDispatcher(@NonNull AppScanner appScanner,
                 @NonNull FileScanner fileScanner) {
        mAppScanner = appScanner;
        mFileScanner = fileScanner;
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public synchronized void scanInstalledApps(@NonNull final ScanStateListener listener) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.scanInstalledApps(listener);
            }
        });
    }

    public synchronized void scanFileSystem(@NonNull final ScanStateListener listener) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mFileScanner.scanFileSystem(listener);
            }
        });
    }

    public synchronized void scanSingleApp(@NonNull final ApplicationInfo info,
                                           @NonNull final ScanStateListener listener) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.checkNewOrUpdatedApp(info, listener);
            }
        });
    }

    public synchronized void scanSingleFile(@NonNull final File fileToScan,
                                            @NonNull final ScanStateListener listener) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mFileScanner.scanSingleFile(fileToScan, listener);
            }
        });
    }

    public synchronized void stopAllScans() {
        mAppScanner.stopScan();
        mFileScanner.stopScan();

        mExecutor.shutdown();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public synchronized void checkRemovedApp(@NonNull final String packageName) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.checkRemovedApp(packageName);
            }
        });
    }

    public synchronized void onFileThreatWasDeleted(@NonNull final String filePath) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mFileScanner.onFileThreatWasDeleted(filePath);
            }
        });
    }
}
