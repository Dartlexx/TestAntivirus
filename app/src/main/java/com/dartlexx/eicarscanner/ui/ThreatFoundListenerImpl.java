package com.dartlexx.eicarscanner.ui;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;

public final class ThreatFoundListenerImpl implements ThreatFoundListener {

    @NonNull
    private final Context mAppContext;

    public ThreatFoundListenerImpl(@NonNull Context appContext) {
        mAppContext = appContext;
    }

    @Override
    public void onAppThreatFound(@NonNull String appName, @NonNull String packageName) {
        Toast.makeText(mAppContext, "Found new app threat: " + appName,
                Toast.LENGTH_LONG)
                .show();
    }
}
