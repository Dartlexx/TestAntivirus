package com.dartlexx.eicarscanner.common.storage;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.List;
import androidx.annotation.NonNull;

public interface AppThreatSignatureStorage {

    @NonNull
    List<AppThreatSignature> getAppSignatures();

    void updateAppSignatures(@NonNull List<AppThreatSignature> newSignatures);
}
