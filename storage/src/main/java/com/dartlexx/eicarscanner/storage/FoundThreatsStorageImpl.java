package com.dartlexx.eicarscanner.storage;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.FileThreatInfo;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class FoundThreatsStorageImpl implements FoundAppThreatStorage, FoundFileThreatStorage {

    private static final String TAG = FoundThreatsStorageImpl.class.getSimpleName();
    private static final String FOUND_APP_THREATS_KEY = "AppThreats";
    private static final String FOUND_FILE_THREATS_KEY = "FileThreats";

    private final SharedPreferences mThreatStorage;

    FoundThreatsStorageImpl(SharedPreferences threatPrefs) {
        mThreatStorage = threatPrefs;
    }

    @NonNull
    @Override
    public List<AppThreatInfo> getAppThreats() {
        final Set<String> data = mThreatStorage.getStringSet(FOUND_APP_THREATS_KEY,
                Collections.<String>emptySet());
        if (data == null) {
            return Collections.emptyList();
        }

        final Gson gson = new Gson();
        final List<AppThreatInfo> result = new ArrayList<>();
        for (String record: data) {
            try {
                AppThreatInfo threat = gson.fromJson(record, AppThreatInfo.class);
                result.add(threat);
            } catch (JsonSyntaxException exc) {
                Log.w(TAG, "Failed to parse from JSON following string: " + record);
            }
        }
        return result;
    }

    @Override
    public void updateAppThreats(@NonNull List<AppThreatInfo> foundThreats) {
        final Set<String> data = new HashSet<>(foundThreats.size());
        final Gson gson = new Gson();
        for (AppThreatInfo threat: foundThreats) {
            String record = gson.toJson(threat);
            data.add(record);
        }
        mThreatStorage.edit()
                .putStringSet(FOUND_APP_THREATS_KEY, data)
                .apply();
    }

    @NonNull
    @Override
    public List<FileThreatInfo> getFileThreats() {
        final Set<String> data = mThreatStorage.getStringSet(FOUND_FILE_THREATS_KEY,
                Collections.<String>emptySet());
        if (data == null) {
            return Collections.emptyList();
        }

        final Gson gson = new Gson();
        final List<FileThreatInfo> result = new ArrayList<>();
        for (String record: data) {
            try {
                FileThreatInfo threat = gson.fromJson(record, FileThreatInfo.class);
                result.add(threat);
            } catch (JsonSyntaxException exc) {
                Log.w(TAG, "Failed to parse from JSON following string: " + record);
            }
        }
        return result;
    }

    @Override
    public void updateFileThreats(@NonNull List<FileThreatInfo> foundThreats) {
        final Set<String> data = new HashSet<>(foundThreats.size());
        final Gson gson = new Gson();
        for (FileThreatInfo threat: foundThreats) {
            String record = gson.toJson(threat);
            data.add(record);
        }
        mThreatStorage.edit()
                .putStringSet(FOUND_FILE_THREATS_KEY, data)
                .apply();
    }
}
