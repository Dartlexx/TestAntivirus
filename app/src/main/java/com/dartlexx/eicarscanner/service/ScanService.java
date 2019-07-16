package com.dartlexx.eicarscanner.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.App;
import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;
import com.dartlexx.eicarscanner.di.AppComponent;

public class ScanService extends Service {

    private static final String IS_STOP_SCAN_PARAM = "isStopScan";
    private static final String IS_FULL_SCAN_PARAM = "isFullScan";
    private static final String FILE_SCAN_TARGET_PARAM = "fileScanTarget";
    private static final String APP_SCAN_TARGET_PARAM = "appScanTarget";

    public static void startFullScan(@NonNull Context context) {
        startService(context, false, true, null, null);
    }

    public static void stopAllScans(@NonNull Context context) {
        startService(context, true, false, null, null);
    }

    public static void startSingleAooScan(@NonNull Context context,
                                          @NonNull ApplicationInfo appInfo) {
        startService(context, false, false, null, appInfo);
    }

    private static void startService(@NonNull Context context,
                                     boolean isStopScan,
                                     boolean isFullScan,
                                     @Nullable String fileTarget,
                                     @Nullable ApplicationInfo appTarget) {
        final Intent intent = new Intent(context, ScanService.class);
        intent.putExtra(IS_STOP_SCAN_PARAM, isStopScan);
        intent.putExtra(IS_FULL_SCAN_PARAM, isFullScan);

        if (fileTarget != null) {
            intent.putExtra(FILE_SCAN_TARGET_PARAM, fileTarget);
        } else if (appTarget != null) {
            intent.putExtra(APP_SCAN_TARGET_PARAM, appTarget);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final AppComponent appComponent = ((App) getApplication()).getAppComponent();
        final AvDispatcher avDispatcher = appComponent.getAvDispatcher();

        if (intent.getBooleanExtra(IS_STOP_SCAN_PARAM, false)) {
            avDispatcher.stopAppsScan();
            stopSelf();
            return Service.START_NOT_STICKY;
        }

        // TODO Create or update notification and start in foreground

        final ScanStateListener scanListener = new ScanStateListenerImpl(startId);

        if (intent.getBooleanExtra(IS_FULL_SCAN_PARAM, false)) {
            avDispatcher.scanInstalledApps(scanListener);
        } else {
            final String fileTarget = intent.getStringExtra(FILE_SCAN_TARGET_PARAM);

            if (fileTarget == null) {
                ApplicationInfo appInfo = intent.getParcelableExtra(APP_SCAN_TARGET_PARAM);
                if (appInfo != null) {
                    avDispatcher.scanSingleApp(appInfo, scanListener);
                }
            }
        }

        return Service.START_REDELIVER_INTENT;
    }

    private class ScanStateListenerImpl implements ScanStateListener {

        private final int mId;

        ScanStateListenerImpl(int serviceId) {
            mId = serviceId;
        }

        @Override
        public void onScanStarted(boolean isPartialScam) {
        }

        @Override
        public void onScanProgressChanged(int progress) {
            // TODO Update notification
        }

        @Override
        public void onScanFinished() {
            stopSelf(mId);
        }
    }
}
