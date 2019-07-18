package com.dartlexx.eicarscanner.common.storage;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.FileThreatInfo;

import java.util.Map;

public interface FoundFileThreatStorage {

    @NonNull
    Map<String, FileThreatInfo> getFileThreats();

    void updateFileThreats(@NonNull Map<String, FileThreatInfo> foundThreats);
}
