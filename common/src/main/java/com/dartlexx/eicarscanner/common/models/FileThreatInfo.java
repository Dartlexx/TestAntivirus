package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.Objects;

public class FileThreatInfo {

    private final long mThreatId;
    private final String mFileName;
    private final String mFilePath;

    public FileThreatInfo(@NonNull Long threatId,
                          @NonNull String fileName,
                          @NonNull String filePath) {
        mThreatId = threatId;
        mFileName = fileName;
        mFilePath = filePath;
    }

    public FileThreatInfo(@NonNull Long threatId,
                          @NonNull File file) {
        this(threatId, file.getName(), file.getAbsolutePath());
    }

    public long getThreatId() {
        return mThreatId;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public String getFileName() {
        return mFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileThreatInfo that = (FileThreatInfo) o;
        return mThreatId == that.mThreatId &&
                mFilePath.equals(that.mFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mThreatId, mFilePath);
    }
}
