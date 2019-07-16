package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;

public final class RepositoryServiceProvider {

    @Nullable
    private FoundAppThreatRepo mAppThreatRepo;

    @Nullable
    private AppThreatSignatureRepo mAppSignatureRepo;

    @NonNull
    public FoundAppThreatRepo getAppThreatRepo(@NonNull FoundAppThreatStorage storage) {
        if (mAppThreatRepo == null) {
            mAppThreatRepo = new FoundThreatRepoImpl(storage);
        }
        return mAppThreatRepo;
    }

    @NonNull
    public AppThreatSignatureRepo getAppSignatureRepo(@NonNull AppThreatSignatureStorage storage) {
        if (mAppSignatureRepo == null) {
            mAppSignatureRepo = new ThreatSignatureRepoImpl(storage);
        }
        return mAppSignatureRepo;
    }
}
