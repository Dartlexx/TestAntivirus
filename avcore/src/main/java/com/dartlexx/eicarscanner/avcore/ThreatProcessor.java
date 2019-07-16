package com.dartlexx.eicarscanner.avcore;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        final List<AppThreatInfo> knownThreats = new ArrayList<>(mAppThreatRepo.getAppThreats());
        for (AppThreatInfo threat : knownThreats) {
            if (Objects.equals(threat.getPackageName(), foundThreat.getPackageName())) {
                return;
            }
        }

        knownThreats.add(foundThreat);
        mAppThreatRepo.updateAppThreats(knownThreats);

        mListener.onAppThreatFound(foundThreat.getAppName(), foundThreat.getPackageName());
    }
}
