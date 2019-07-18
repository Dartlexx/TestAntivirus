package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.Map;

public interface AppThreatSignatureRepo {

    @NonNull
    Map<String, AppThreatSignature> getAppSignatures();

    void updateAppSignatures(@NonNull Map<String, AppThreatSignature> newSignatures);
}
