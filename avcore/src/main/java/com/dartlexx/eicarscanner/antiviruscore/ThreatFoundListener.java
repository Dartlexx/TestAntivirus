package com.dartlexx.eicarscanner.antiviruscore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

public interface ThreatFoundListener {

    void onAppThreatFound(@NonNull AppThreatSignature signature,
                          @NonNull ApplicationInfo appInfo,
                          int version);
}
