package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FileThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;

public final class RepositoryServiceProvider {

    @Nullable
    private FoundThreatRepoImpl mThreatRepo;

    @Nullable
    private ThreatSignatureRepo mThreatSignatureRepo;

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
    public ThreatSignatureRepo getThreatSignatureRepo(@NonNull AppThreatSignatureStorage appStorage,
                                                      @NonNull FileThreatSignatureStorage fileStorage) {
        if (mThreatSignatureRepo == null) {
            mThreatSignatureRepo = new ThreatSignatureRepoImpl(appStorage, fileStorage);
        }
        return mThreatSignatureRepo;
    }
}
