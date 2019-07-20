package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.FileThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;

import java.util.Map;

final class FoundThreatRepoImpl implements FoundAppThreatRepo, FoundFileThreatRepo {

    @NonNull
    private final FoundAppThreatStorage mAppThreatStorage;

    @NonNull
    private final FoundFileThreatStorage mFileThreatStorage;

    FoundThreatRepoImpl(@NonNull FoundAppThreatStorage appThreatStorage,
                        @NonNull FoundFileThreatStorage fileThreatStorage) {
        mAppThreatStorage = appThreatStorage;
        mFileThreatStorage = fileThreatStorage;
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

    @NonNull
    @Override
    public Map<String, FileThreatInfo> getFileThreats() {
        return mFileThreatStorage.getFileThreats();
    }

    @Override
    public void updateFileThreats(@NonNull Map<String, FileThreatInfo> foundThreats) {
        mFileThreatStorage.updateFileThreats(foundThreats);
    }
}
