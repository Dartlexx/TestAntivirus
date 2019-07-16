package com.dartlexx.eicarscanner.avcore;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.ui.ThreatFoundUiListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class ThreatProcessorImpl implements ThreatProcessor {

    @NonNull
    private final FoundAppThreatRepo mAppThreatRepo;

    @NonNull
    private final ThreatFoundUiListener mUiListener;

    ThreatProcessorImpl(@NonNull FoundAppThreatRepo foundAppThreatRepo,
                        @NonNull ThreatFoundUiListener listener) {
        mAppThreatRepo = foundAppThreatRepo;
        mUiListener = listener;
    }

    @Override
    public void onAppThreatFound(@NonNull AppThreatInfo foundThreat) {
        final List<AppThreatInfo> knownThreats = new ArrayList<>(mAppThreatRepo.getAppThreats());
        for (AppThreatInfo threat : knownThreats) {
            if (Objects.equals(threat.getPackageName(), foundThreat.getPackageName())) {
                return;
            }
        }

        knownThreats.add(foundThreat);
        mAppThreatRepo.updateAppThreats(knownThreats);

        mUiListener.onAppThreatFound(foundThreat.getAppName(), foundThreat.getPackageName());
    }

    @Override
    public void onAppScanProgressUpdated(int progress) {
        mUiListener.onAppScanProgressUpdated(progress);
    }
}
