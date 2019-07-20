package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;

public final class RepositoryServiceProvider {

    @Nullable
    private FoundThreatRepoImpl mThreatRepo;

    @Nullable
    private AppThreatSignatureRepo mAppSignatureRepo;

    @NonNull
    public FoundAppThreatRepo getAppThreatRepo(@NonNull FoundAppThreatStorage appStorage,
                                               @NonNull FoundFileThreatStorage fileStorage) {
        if (mThreatRepo == null) {
            mThreatRepo = new FoundThreatRepoImpl(appStorage, fileStorage);
        }
        return mThreatRepo;
    }

    @NonNull
    public FoundFileThreatRepo getFileThreatRepo(@NonNull FoundAppThreatStorage appStorage,
                                                 @NonNull FoundFileThreatStorage fileStorage) {
        return (FoundFileThreatRepo) getAppThreatRepo(appStorage, fileStorage);
    }

    @NonNull
    public AppThreatSignatureRepo getAppSignatureRepo(@NonNull AppThreatSignatureStorage storage) {
        if (mAppSignatureRepo == null) {
            mAppSignatureRepo = new ThreatSignatureRepoImpl(storage);
        }
        return mAppSignatureRepo;
    }
}
