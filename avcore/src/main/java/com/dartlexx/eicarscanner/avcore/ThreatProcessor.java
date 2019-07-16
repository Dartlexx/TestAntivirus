package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

public interface ThreatProcessor {

    void onAppThreatFound(@NonNull AppThreatSignature signature,
                          @NonNull ApplicationInfo appInfo,
                          int version);

    void onAppScanProgressUpdated(int progress);
}
