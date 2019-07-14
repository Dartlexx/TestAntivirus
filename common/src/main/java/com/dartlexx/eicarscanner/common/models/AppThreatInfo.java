package com.dartlexx.eicarscanner.common.models;

import androidx.annotation.NonNull;

public class AppThreatInfo {

    private final long mThreatId;
    private final String mAppName;
    private final String mPackageName;
    private final int mVersion;

    public AppThreatInfo(@NonNull AppThreatSignature signature,
                         @NonNull String appName,
                         int version) {
        mThreatId = signature.getId();
        mPackageName = signature.getPackageName();
        mAppName = appName;
        mVersion = version;
    }

    public long getThreatId() {
        return mThreatId;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getAppName() {
        return mAppName;
    }

    public int getVersion() {
        return mVersion;
    }
}
