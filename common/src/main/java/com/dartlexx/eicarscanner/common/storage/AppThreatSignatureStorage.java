package com.dartlexx.eicarscanner.common.storage;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.List;
import androidx.annotation.NonNull;

public interface AppThreatSignatureStorage {

    @NonNull
    List<AppThreatSignature> getSignatures();

    void updateSignatures(@NonNull List<AppThreatSignature> newSignatures);
}
