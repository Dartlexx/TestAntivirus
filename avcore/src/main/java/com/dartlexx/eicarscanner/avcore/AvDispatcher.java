package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class AvDispatcher {

    @NonNull
    private final AppScanner mAppScanner;

    private final ExecutorService mExecutor;

    private volatile Boolean mStopAppScan;

    public AvDispatcher(@NonNull AppScanner appScanner) {
        mAppScanner = appScanner;
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public synchronized void scanInstalledApps() {
        mStopAppScan = false;
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.scanInstalledApps(mStopAppScan);
            }
        });
    }

    public synchronized void scanSingleApp(@NonNull final ApplicationInfo info) {
        mStopAppScan = false;
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                mAppScanner.checkNewOrUpdatedApp(info, mStopAppScan);
            }
        });
    }

    public synchronized void stopAppsScan() {
        mStopAppScan = true;

        mExecutor.shutdown();
        try {
            mExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException exc) {
            mExecutor.shutdownNow();
        }
    }
}
