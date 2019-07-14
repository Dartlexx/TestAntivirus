package com.dartlexx.eicarscanner.common.storage;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;

import java.util.List;

public interface FoundAppThreatStorage {

    @NonNull
    List<AppThreatInfo> getAppThreats();

    void updateAppThreats(@NonNull List<AppThreatInfo> foundThreats);
}
