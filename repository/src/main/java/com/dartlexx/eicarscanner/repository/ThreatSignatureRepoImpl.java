package com.dartlexx.eicarscanner.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FileThreatSignatureStorage;

import java.util.Map;

final class ThreatSignatureRepoImpl implements ThreatSignatureRepo {

    @NonNull
    private final AppThreatSignatureStorage mAppSignatureStorage;
    @NonNull
    private final FileThreatSignatureStorage mFileSignatureStorage;

    ThreatSignatureRepoImpl(@NonNull AppThreatSignatureStorage appSignatureStorage,
                            @NonNull FileThreatSignatureStorage fileSignatureStorage) {
        mAppSignatureStorage = appSignatureStorage;
        mFileSignatureStorage = fileSignatureStorage;
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

    @NonNull
    @Override
    public Map<String, FileThreatSignature> getFileSignatures() {
        return mFileSignatureStorage.getFileSignatures();
    }

    @Override
    public void updateFileSignatures(@NonNull Map<String, FileThreatSignature> newSignatures) {
        mFileSignatureStorage.updateFileSignatures(newSignatures);
    }
}
