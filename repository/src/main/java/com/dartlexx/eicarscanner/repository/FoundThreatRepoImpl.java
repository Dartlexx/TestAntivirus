package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;

import java.util.List;

final class FoundThreatRepoImpl implements FoundAppThreatRepo {

    @NonNull
    private final FoundAppThreatStorage mAppThreatStorage;

    FoundThreatRepoImpl(@NonNull FoundAppThreatStorage appThreatStorage) {
        mAppThreatStorage = appThreatStorage;
    }

    @NonNull
    @Override
    public List<AppThreatInfo> getAppThreats() {
        return mAppThreatStorage.getAppThreats();
    }

    @Override
    public void updateAppThreats(@NonNull List<AppThreatInfo> foundThreats) {
        mAppThreatStorage.updateAppThreats(foundThreats);
    }
}
