package com.dartlexx.eicarscanner.common.storage;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.FileThreatSignature;

import java.util.Map;

public interface FileThreatSignatureStorage {

    @NonNull
    Map<String, FileThreatSignature> getFileSignatures();

    void updateFileSignatures(@NonNull Map<String, FileThreatSignature> newSignatures);
}
