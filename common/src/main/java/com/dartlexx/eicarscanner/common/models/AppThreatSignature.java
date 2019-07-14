package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

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
}
