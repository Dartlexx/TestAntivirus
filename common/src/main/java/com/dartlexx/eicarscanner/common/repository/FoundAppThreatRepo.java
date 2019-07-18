package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;

import java.util.Map;

public interface FoundAppThreatRepo {

    @NonNull
    Map<String, AppThreatInfo> getAppThreats();

    void updateAppThreats(@NonNull Map<String, AppThreatInfo> foundThreats);
}
