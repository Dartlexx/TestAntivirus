package com.dartlexx.eicarscanner.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.dartlexx.eicarscanner.R;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;

public final class ThreatFoundListenerImpl implements ThreatFoundListener {

    @NonNull
    private final ArrayMap<String, Integer> mThreatNotifications = new ArrayMap<>();

    @NonNull
    private final Context mContext;

    @NonNull
    private final NotificationHelper mHelper;

    public ThreatFoundListenerImpl(@NonNull Context context,
                                   @NonNull NotificationHelper helper) {
        mContext = context;
        mHelper = helper;
    }

    @Override
    public void onAppThreatFound(@NonNull String appName, @NonNull String packageName) {
        int notificationId = mHelper.getNextNotificationId();

        Uri packageUri = Uri.parse("package:" + packageName);
        PendingIntent uninstallIntent = PendingIntent.getActivity(mContext,
                notificationId,
                new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri),
                0);

        mHelper.showThreatNotification(notificationId,
                R.string.notification_title_app_threat_found,
                R.string.notification_description_threat_found,
                appName,
                uninstallIntent);

        mThreatNotifications.put(packageName, notificationId);
    }

    @Override
    public void onAppThreatRemoved(@NonNull String packageName) {
        Integer id = mThreatNotifications.get(packageName);
        if (id != null) {
            mHelper.cancelNotification(id);
        }
    }
}
