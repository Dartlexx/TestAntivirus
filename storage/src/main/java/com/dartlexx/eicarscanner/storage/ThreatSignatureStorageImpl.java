package com.dartlexx.eicarscanner.storage;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FileThreatSignatureStorage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class ThreatSignatureStorageImpl implements AppThreatSignatureStorage,
        FileThreatSignatureStorage {

    private static final String TAG = ThreatSignatureStorageImpl.class.getSimpleName();
    private static final String APP_SIGNATURES_LIST_KEY = "AppSignatures";
    private static final String FILE_SIGNATURES_LIST_KEY = "FileSignatures";

    private final SharedPreferences mSignatureStorage;

    ThreatSignatureStorageImpl(SharedPreferences signaturePrefs) {
        mSignatureStorage = signaturePrefs;
    }

    @NonNull
    @Override
    public Map<String, AppThreatSignature> getAppSignatures() {
        final Set<String> data = mSignatureStorage.getStringSet(APP_SIGNATURES_LIST_KEY,
                Collections.<String>emptySet());
        if (data == null) {
            return Collections.emptyMap();
        }

        final Gson gson = new Gson();
        final Map<String, AppThreatSignature> result = new HashMap<>();
        for (String record: data) {
            try {
                AppThreatSignature threat = gson.fromJson(record, AppThreatSignature.class);
                result.put(threat.getPackageName(), threat);
            } catch (JsonSyntaxException exc) {
                Log.w(TAG, "Failed to parse from JSON following string: " + record);
            }
        }
        return result;
    }

    @Override
    public void updateAppSignatures(@NonNull Map<String, AppThreatSignature> updatedSignatures) {
        final Set<String> data = new HashSet<>(updatedSignatures.size());
        final Gson gson = new Gson();
        for (AppThreatSignature signature: updatedSignatures.values()) {
            String record = gson.toJson(signature);
            data.add(record);
        }
        mSignatureStorage.edit()
                .putStringSet(APP_SIGNATURES_LIST_KEY, data)
                .apply();
    }

    @NonNull
    @Override
    public Map<String, FileThreatSignature> getFileSignatures() {
        final Set<String> data = mSignatureStorage.getStringSet(FILE_SIGNATURES_LIST_KEY,
                Collections.<String>emptySet());
        if (data == null) {
            return Collections.emptyMap();
        }

        final Gson gson = new Gson();
        final Map<String, FileThreatSignature> result = new HashMap<>();
        for (String record: data) {
            try {
                FileThreatSignature threat = gson.fromJson(record, FileThreatSignature.class);
                result.put(threat.getFileNameMask(), threat);
            } catch (JsonSyntaxException exc) {
                Log.w(TAG, "Failed to parse from JSON following string: " + record);
            }
        }
        return result;
    }

    @Override
    public void updateFileSignatures(@NonNull Map<String, FileThreatSignature> updatedSignatures) {
        final Set<String> data = new HashSet<>(updatedSignatures.size());
        final Gson gson = new Gson();
        for (FileThreatSignature signature: updatedSignatures.values()) {
            String record = gson.toJson(signature);
            data.add(record);
        }
        mSignatureStorage.edit()
                .putStringSet(FILE_SIGNATURES_LIST_KEY, data)
                .apply();
    }
}
