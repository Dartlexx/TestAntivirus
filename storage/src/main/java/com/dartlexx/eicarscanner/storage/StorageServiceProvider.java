package com.dartlexx.eicarscanner.storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FileThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;

public class StorageServiceProvider {

    private static final String THREATS_STORAGE = "FoundThreats";
    private static final String SIGNATURES_STORAGE = "Signatures";

    @Nullable
    private FoundThreatsStorageImpl mThreatStorage;

    @Nullable
    private ThreatSignatureStorageImpl mSignatureStorage;

    @NonNull
    public synchronized FoundAppThreatStorage getAppThreatStorage(@NonNull Context appContext) {
        if (mThreatStorage == null) {
            initThreatStorage(appContext);
        }
        return mThreatStorage;
    }

    @NonNull
    public synchronized FoundFileThreatStorage getFileThreatStorage(@NonNull Context appContext) {
        if (mThreatStorage == null) {
            initThreatStorage(appContext);
        }
        return mThreatStorage;
    }

    @NonNull
    public synchronized AppThreatSignatureStorage getAppSignatureStorage(@NonNull Context context) {
        if (mSignatureStorage == null) {
            initSignatureStorage(context);
        }
        return mSignatureStorage;
    }

    @NonNull
    public synchronized FileThreatSignatureStorage getFileSignatureStorage(@NonNull Context context) {
        if (mSignatureStorage == null) {
            initSignatureStorage(context);
        }
        return mSignatureStorage;
    }

    @SuppressWarnings("WeakerAccess")
    public synchronized void resetProvider() {
        mThreatStorage = null;
        mSignatureStorage = null;
    }

    private void initThreatStorage(@NonNull Context appContext) {
        final SharedPreferences prefs = appContext.getSharedPreferences(THREATS_STORAGE,
                Context.MODE_PRIVATE);
        mThreatStorage = new FoundThreatsStorageImpl(prefs);
    }

    private void initSignatureStorage(@NonNull Context appContext) {
        final SharedPreferences prefs = appContext.getSharedPreferences(SIGNATURES_STORAGE,
                Context.MODE_PRIVATE);
        mSignatureStorage = new ThreatSignatureStorageImpl(prefs);
    }
}
