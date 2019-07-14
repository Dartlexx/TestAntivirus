package com.dartlexx.eicarscanner.common.storage;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.FileThreatInfo;

import java.util.List;

public interface FoundFileThreatStorage {

    @NonNull
    List<FileThreatInfo> getFileThreats();

    void updateFileThreats(@NonNull List<FileThreatInfo> foundThreats);
}
