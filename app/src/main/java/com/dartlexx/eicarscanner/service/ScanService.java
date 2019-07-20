package com.dartlexx.eicarscanner.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.dartlexx.eicarscanner.App;
import com.dartlexx.eicarscanner.R;
import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;
import com.dartlexx.eicarscanner.di.AppComponent;
import com.dartlexx.eicarscanner.ui.NotificationHelper;

import java.io.File;

public class ScanService extends Service {

    private static final String IS_STOP_SCAN_PARAM = "isStopScan";
    private static final String IS_FULL_SCAN_PARAM = "isFullScan";
    private static final String FILE_SCAN_TARGET_PARAM = "fileScanTarget";
    private static final String APP_SCAN_TARGET_PARAM = "appScanTarget";

    private volatile boolean mIsStopped;

    public static void startFullScan(@NonNull Context context) {
        startService(context, false, true, null, null);
    }

    public static void stopAllScans(@NonNull Context context) {
        startService(context, true, false, null, null);
    }

    public static void startSingleAppScan(@NonNull Context context,
                                          @NonNull ApplicationInfo appInfo) {
        startService(context, false, false, null, appInfo);
    }

    public static void startSingleFileScan(@NonNull Context context,
                                           @NonNull String filePath) {
        startService(context, false, false, filePath, null);
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
            mIsStopped = true;
            avDispatcher.stopAllScans();
            stopSelf();
            return Service.START_NOT_STICKY;
        }

        mIsStopped = false;

        boolean isFullScan = intent.getBooleanExtra(IS_FULL_SCAN_PARAM, false);
        final ScanStateListener scanListener = new ScanStateListenerImpl(appComponent.getNotificationHelper(),
                startId, isFullScan);

        if (isFullScan) {
            avDispatcher.scanInstalledApps(scanListener);
        } else {
            final String fileTarget = intent.getStringExtra(FILE_SCAN_TARGET_PARAM);
            if (fileTarget == null) {
                ApplicationInfo appInfo = intent.getParcelableExtra(APP_SCAN_TARGET_PARAM);
                if (appInfo != null) {
                    avDispatcher.scanSingleApp(appInfo, scanListener);
                }
            } else {
                avDispatcher.scanSingleFile(new File(fileTarget), scanListener);
            }
        }

        return Service.START_REDELIVER_INTENT;
    }

    private class ScanStateListenerImpl implements ScanStateListener {

        private final NotificationHelper mHelper;
        private final int mId;
        private final int mNotificationId;
        private final boolean mIsFullScan;

        ScanStateListenerImpl(@NonNull NotificationHelper helper,
                              int serviceId,
                              boolean isFullScan) {
            mHelper = helper;
            mId = serviceId;
            mIsFullScan = isFullScan;
            mNotificationId = mHelper.getNextNotificationId();

            startForeground(mNotificationId, getNotification(false, 0));
        }

        @Override
        public void onScanStarted() {
        }

        @Override
        public void onScanProgressChanged(int progress) {
            if (!mIsStopped) {
                mHelper.updateNotification(mNotificationId,
                        getNotification(true, progress));
            }
        }

        @Override
        public void onScanFinished() {
            mHelper.cancelNotification(mNotificationId);
            stopSelf(mId);
        }

        @NonNull
        private Notification getNotification(boolean showProgress, int progressValue) {
            return mHelper.getForegroundServiceNotification(
                    getTitleRes(),
                    getDescriptionRes(!showProgress),
                    showProgress,
                    progressValue);
        }

        @StringRes
        private int getTitleRes() {
            return mIsFullScan
                    ? R.string.notification_title_full_scan
                    : R.string.notification_title_partial_scan;
        }

        @StringRes
        private int getDescriptionRes(boolean isScheduled) {
            if (isScheduled) {
                return R.string.notification_description_scan_scheduled;
            }
            return mIsFullScan
                    ? R.string.notification_description_full_scan_executing
                    : R.string.notification_description_partial_scan_executing;
        }
    }
}
