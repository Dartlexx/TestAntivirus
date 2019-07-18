package com.dartlexx.eicarscanner.common.storage;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;

import java.util.Map;

public interface FoundAppThreatStorage {

    @NonNull
    Map<String, AppThreatInfo> getAppThreats();

    void updateAppThreats(@NonNull Map<String, AppThreatInfo> foundThreats);
}
