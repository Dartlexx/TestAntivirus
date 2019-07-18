package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AvDispatcher {

    @NonNull
    private final AppScanner mAppScanner;

    private ExecutorService mExecutor;

    AvDispatcher(@NonNull AppScanner appScanner) {
        mAppScanner = appScanner;
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

    public synchronized void scanSingleApp(@NonNull final ApplicationInfo info,
                                           @NonNull final ScanStateListener listener) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.checkNewOrUpdatedApp(info, listener);
            }
        });
    }

    public synchronized void stopAllScans() {
        mAppScanner.stopScan();
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
}
