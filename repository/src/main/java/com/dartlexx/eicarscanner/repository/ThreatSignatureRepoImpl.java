package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;

import java.util.Map;

final class ThreatSignatureRepoImpl implements AppThreatSignatureRepo {

    @NonNull
    private final AppThreatSignatureStorage mAppSignatureStorage;

    ThreatSignatureRepoImpl(@NonNull AppThreatSignatureStorage appSignatureStorage) {
        mAppSignatureStorage = appSignatureStorage;
    }

    @NonNull
    @Override
    public Map<String, AppThreatSignature> getAppSignatures() {
        return mAppSignatureStorage.getAppSignatures();
    }

    @Override
    public void updateAppSignatures(@NonNull Map<String, AppThreatSignature> newSignatures) {
        mAppSignatureStorage.updateAppSignatures(newSignatures);
    }
}
