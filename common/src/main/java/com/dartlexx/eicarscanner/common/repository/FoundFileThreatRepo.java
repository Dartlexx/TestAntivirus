package com.dartlexx.eicarscanner.common.repository;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.FileThreatInfo;

import java.util.Map;

public interface FoundFileThreatRepo {

    @NonNull
    Map<String, FileThreatInfo> getFileThreats();

    void updateFileThreats(@NonNull Map<String, FileThreatInfo> foundThreats);
}
