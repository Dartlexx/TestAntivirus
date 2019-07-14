package com.dartlexx.eicarscanner.storage;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class ThreatSignatureStorageImpl implements AppThreatSignatureStorage {

    private static final String TAG = ThreatSignatureStorageImpl.class.getSimpleName();
    private static final String APP_SIGNATURES_LIST_KEY = "AppSignatures";

    private final SharedPreferences mSignatureStorage;

    ThreatSignatureStorageImpl(SharedPreferences signaturePrefs) {
        mSignatureStorage = signaturePrefs;
    }

    @NonNull
    @Override
    public List<AppThreatSignature> getAppSignatures() {
        final Set<String> data = mSignatureStorage.getStringSet(APP_SIGNATURES_LIST_KEY,
                Collections.<String>emptySet());
        if (data == null) {
            return Collections.emptyList();
        }

        final Gson gson = new Gson();
        final List<AppThreatSignature> result = new ArrayList<>();
        for (String record: data) {
            try {
                AppThreatSignature threat = gson.fromJson(record, AppThreatSignature.class);
                result.add(threat);
            } catch (JsonSyntaxException exc) {
                Log.w(TAG, "Failed to parse from JSON following string: " + record);
            }
        }
        return result;
    }

    @Override
    public void updateAppSignatures(@NonNull List<AppThreatSignature> updatedSignatures) {
        final Set<String> data = new HashSet<>(updatedSignatures.size());
        final Gson gson = new Gson();
        for (AppThreatSignature signature: updatedSignatures) {
            String record = gson.toJson(signature);
            data.add(record);
        }
        mSignatureStorage.edit()
                .putStringSet(APP_SIGNATURES_LIST_KEY, data)
                .apply();
    }
}
