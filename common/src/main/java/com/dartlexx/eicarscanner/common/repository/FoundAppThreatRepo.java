package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;

import java.util.List;

public interface FoundAppThreatRepo {

    @NonNull
    List<AppThreatInfo> getFoundThreats();

    void updateFoundThreats(@NonNull List<AppThreatInfo> foundThreats);
}
