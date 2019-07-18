package com.dartlexx.eicarscanner.avcore;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;

import java.util.HashMap;
import java.util.Map;

final class ThreatProcessor {

    @NonNull
    private final FoundAppThreatRepo mAppThreatRepo;

    @NonNull
    private final ThreatFoundListener mListener;

    ThreatProcessor(@NonNull FoundAppThreatRepo foundAppThreatRepo,
                    @NonNull ThreatFoundListener listener) {
        mAppThreatRepo = foundAppThreatRepo;
        mListener = listener;
    }

    void onAppThreatFound(@NonNull AppThreatInfo foundThreat) {
        final Map<String, AppThreatInfo> knownThreats = new HashMap<>(mAppThreatRepo.getAppThreats());
        AppThreatInfo existing = knownThreats.get(foundThreat.getPackageName());
        if (existing != null) {
            return;
        }

        knownThreats.put(foundThreat.getPackageName(), foundThreat);
        mAppThreatRepo.updateAppThreats(knownThreats);

        mListener.onAppThreatFound(foundThreat.getAppName(), foundThreat.getPackageName());
    }

    void onAppDeleted(@NonNull String packageName) {
        final Map<String, AppThreatInfo> knownThreats = new HashMap<>(mAppThreatRepo.getAppThreats());
        AppThreatInfo removed = knownThreats.remove(packageName);
        if (removed == null) {
            return;
        }

        mAppThreatRepo.updateAppThreats(knownThreats);
        mListener.onAppThreatRemoved(packageName);
    }
}
