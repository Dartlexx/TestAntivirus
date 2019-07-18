package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;

import java.util.Map;

final class FoundThreatRepoImpl implements FoundAppThreatRepo {

    @NonNull
    private final FoundAppThreatStorage mAppThreatStorage;

    FoundThreatRepoImpl(@NonNull FoundAppThreatStorage appThreatStorage) {
        mAppThreatStorage = appThreatStorage;
    }

    @NonNull
    @Override
    public Map<String, AppThreatInfo> getAppThreats() {
        return mAppThreatStorage.getAppThreats();
    }

    @Override
    public void updateAppThreats(@NonNull Map<String, AppThreatInfo> foundThreats) {
        mAppThreatStorage.updateAppThreats(foundThreats);
    }
}
