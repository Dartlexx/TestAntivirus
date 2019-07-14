package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import java.util.List;

public interface AppThreatSinatureRepo {

    @NonNull
    List<AppThreatSignature> getSignatures();

    void updateSignatures(@NonNull List<AppThreatSignature> newSignatures);
}
