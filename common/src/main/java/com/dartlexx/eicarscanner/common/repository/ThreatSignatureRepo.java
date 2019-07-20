package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;

import java.util.Map;

public interface ThreatSignatureRepo {

    @NonNull
    Map<String, AppThreatSignature> getAppSignatures();

    @NonNull
    Map<String, FileThreatSignature> getFileSignatures();

    void updateAppSignatures(@NonNull Map<String, AppThreatSignature> newSignatures);

    void updateFileSignatures(@NonNull Map<String, FileThreatSignature> newSignatures);
}
