package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.List;

public interface AppThreatSignatureRepo {

    @NonNull
    List<AppThreatSignature> getAppSignatures();

    void updateAppSignatures(@NonNull List<AppThreatSignature> newSignatures);
}
