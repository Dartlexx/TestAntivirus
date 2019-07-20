package com.dartlexx.eicarscanner.avcore.common;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;
import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.FileThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;

import java.util.HashMap;
import java.util.Map;

public final class ThreatProcessor {

    @NonNull
    private final FoundAppThreatRepo mAppThreatRepo;

    @NonNull
    private final FoundFileThreatRepo mFileThreatRepo;

    @NonNull
    private final ThreatFoundListener mListener;

    public ThreatProcessor(@NonNull FoundAppThreatRepo foundAppThreatRepo,
                    @NonNull FoundFileThreatRepo foundFileThreatRepo,
                    @NonNull ThreatFoundListener listener) {
        mAppThreatRepo = foundAppThreatRepo;
        mFileThreatRepo = foundFileThreatRepo;
        mListener = listener;
    }

    public void onAppThreatFound(@NonNull AppThreatInfo foundThreat) {
        // Intentionally showing UI message each time threat is found (for test purposes)
        mListener.onAppThreatFound(foundThreat.getAppName(), foundThreat.getPackageName());

        final Map<String, AppThreatInfo> knownThreats = new HashMap<>(mAppThreatRepo.getAppThreats());
        AppThreatInfo existing = knownThreats.get(foundThreat.getPackageName());
        if (existing != null) {
            return;
        }

        knownThreats.put(foundThreat.getPackageName(), foundThreat);
        mAppThreatRepo.updateAppThreats(knownThreats);
    }

    public void onFileThreatFound(@NonNull FileThreatInfo foundThreat) {
        // Intentionally showing UI message each time threat is found (for test purposes)
        mListener.onFileThreatFound(foundThreat.getFileName(), foundThreat.getFilePath());

        final Map<String, FileThreatInfo> knownThreats = new HashMap<>(mFileThreatRepo.getFileThreats());
        FileThreatInfo existing = knownThreats.get(foundThreat.getFilePath());
        if (existing != null) {
            return;
        }

        knownThreats.put(foundThreat.getFilePath(), foundThreat);
        mFileThreatRepo.updateFileThreats(knownThreats);
    }

    public void onFileThreatWasDeleted(@NonNull String filePath) {
        final Map<String, FileThreatInfo> knownThreats = new HashMap<>(mFileThreatRepo.getFileThreats());
        FileThreatInfo removed = knownThreats.remove(filePath);
        if (removed != null) {
            mFileThreatRepo.updateFileThreats(knownThreats);
            mListener.onFileThreatRemoved(filePath);
        }
    }

    public void onAppDeleted(@NonNull String packageName) {
        final Map<String, AppThreatInfo> knownThreats = new HashMap<>(mAppThreatRepo.getAppThreats());
        AppThreatInfo removed = knownThreats.remove(packageName);
        if (removed != null) {
            mAppThreatRepo.updateAppThreats(knownThreats);
            mListener.onAppThreatRemoved(packageName);
        }
    }
}
