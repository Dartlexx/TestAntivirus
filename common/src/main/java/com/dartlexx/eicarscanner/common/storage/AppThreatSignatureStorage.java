package com.dartlexx.eicarscanner.common.storage;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.Map;

import androidx.annotation.NonNull;

public interface AppThreatSignatureStorage {

    @NonNull
    Map<String, AppThreatSignature> getAppSignatures();

    void updateAppSignatures(@NonNull Map<String, AppThreatSignature> newSignatures);
}
