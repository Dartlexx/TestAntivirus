package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class FileThreatSignature {

    private final long mId;
    private final String mThreatName;
    private final String mFileNameMask;
    private final String mContentPart;

    public FileThreatSignature(long id,
                               @NonNull String threatName,
                               @NonNull String fileNameMask,
                               @NonNull String contentPart) {
        mId = id;
        mThreatName = threatName;
        mFileNameMask = fileNameMask;
        mContentPart = contentPart;
    }

    public long getId() {
        return mId;
    }

    public String getFileNameMask() {
        return mFileNameMask;
    }

    public String getContentPart() {
        return mContentPart;
    }

    public String getThreatName() {
        return mThreatName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileThreatSignature that = (FileThreatSignature) o;
        return mId == that.mId &&
                mFileNameMask.equals(that.mFileNameMask) &&
                mContentPart.equals(that.mContentPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mFileNameMask, mContentPart);
    }
}
