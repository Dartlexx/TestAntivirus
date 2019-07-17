package com.dartlexx.eicarscanner.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;

import com.dartlexx.eicarscanner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class NotificationHelper {

    private static final String SCAN_STATUS_CHANNEL = "scanStatusChannel";
    private static final String FOUND_THREATS_CHANNEL = "foundThreatsChannel";

    private final NotificationManager mNotificationManager;
    private final Context mAppContext;
    private final AtomicInteger mNextId = new AtomicInteger(1);

    public NotificationHelper(@NonNull NotificationManager manager,
                              @NonNull Context appContext) {
        mNotificationManager = manager;
        mAppContext = appContext;

        setUpChannels();
    }

    int getNextNotificationId() {
        return mNextId.getAndIncrement();
    }

    Notification getForegroundServiceNotification(@StringRes int titleRes,
                                                         @StringRes int descriptionRes,
                                                         boolean showProgress,
                                                         int progressValue) {
        String title = mAppContext.getString(titleRes);
        String description = mAppContext.getString(descriptionRes);

        return getNotificationBuilder(SCAN_STATUS_CHANNEL)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setProgress(showProgress ? 100 : 0, showProgress ? progressValue : 0, false)
                .build();
    }

    void updateNotification(int notificationId,
                            @NonNull Notification notification) {
        mNotificationManager.notify(notificationId, notification);
    }

    void cancelNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
    }

    private void setUpChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel scanChannel = new NotificationChannel(SCAN_STATUS_CHANNEL,
                    mAppContext.getString(R.string.notification_channel_title_scan),
                    NotificationManager.IMPORTANCE_DEFAULT);
            scanChannel.setDescription(mAppContext.getString(R.string.notification_channel_description_scan));

            final NotificationChannel threatsChannel = new NotificationChannel(FOUND_THREATS_CHANNEL,
                    mAppContext.getString(R.string.notification_channel_title_threats),
                    NotificationManager.IMPORTANCE_HIGH);
            threatsChannel.setDescription(mAppContext.getString(R.string.notification_channel_description_threats));

            final List<NotificationChannel> channels = new ArrayList<>(2);
            channels.add(scanChannel);
            channels.add(threatsChannel);
            mNotificationManager.createNotificationChannels(channels);
        }
    }

    @NonNull
    private NotificationCompat.Builder getNotificationBuilder(@NonNull String channelId) {
        return new NotificationCompat.Builder(mAppContext, channelId);
    }
}
