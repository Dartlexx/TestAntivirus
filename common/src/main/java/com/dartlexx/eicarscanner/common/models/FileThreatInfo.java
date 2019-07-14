package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

public class FileThreatInfo {

    private final long mThreatId;
    private final String mFilePath;

    public FileThreatInfo(@NonNull Long threatId,
                          @NonNull String filePath) {
        mThreatId = threatId;
        mFilePath = filePath;
    }

    public long getThreatId() {
        return mThreatId;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
