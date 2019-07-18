package com.dartlexx.eicarscanner.ui;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.dartlexx.eicarscanner.R;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;

public final class ThreatFoundListenerImpl implements ThreatFoundListener {

    @NonNull
    private final ArrayMap<String, Integer> mThreatNotifications = new ArrayMap<>();

    @NonNull
    private final NotificationHelper mHelper;

    public ThreatFoundListenerImpl(@NonNull NotificationHelper helper) {
        mHelper = helper;
    }

    @Override
    public void onAppThreatFound(@NonNull String appName, @NonNull String packageName) {
        int notificationId = mHelper.showThreatNotification(R.string.notification_title_app_threat_found,
                R.string.notification_description_threat_found,
                appName);

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
