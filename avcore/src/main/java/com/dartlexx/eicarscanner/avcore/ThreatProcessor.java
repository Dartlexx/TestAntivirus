package com.dartlexx.eicarscanner.avcore;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;

public interface ThreatProcessor {

    void onAppThreatFound(@NonNull AppThreatInfo foundThreat);

    void onAppScanProgressUpdated(int progress);
}
