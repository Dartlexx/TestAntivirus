package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class AppThreatSignature {

    private final long mId;
    private final String mPackageName;
    private final int mLowVersion;
    private final int mTopVersion;

    public AppThreatSignature(long id, @NonNull String packageName) {
        this(id, packageName, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @SuppressWarnings("WeakerAccess")
    public AppThreatSignature(long id,
                              @NonNull String packageName,
                              int lowVersion,
                              int topVersion) {
        mId = id;
        mPackageName = packageName;
        mLowVersion = lowVersion;
        mTopVersion = topVersion;
    }

    public long getId() {
        return mId;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getLowVersion() {
        return mLowVersion;
    }

    public int getTopVersion() {
        return mTopVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppThreatSignature that = (AppThreatSignature) o;
        return mId == that.mId &&
                mLowVersion == that.mLowVersion &&
                mTopVersion == that.mTopVersion &&
                mPackageName.equals(that.mPackageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mPackageName, mLowVersion, mTopVersion);
    }
}
