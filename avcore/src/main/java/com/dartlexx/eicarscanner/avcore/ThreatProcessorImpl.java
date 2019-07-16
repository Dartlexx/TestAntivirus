package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
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
    public void onAppThreatFound(@NonNull AppThreatSignature signature,
                                 @NonNull ApplicationInfo appInfo,
                                 int version) {
        final List<AppThreatInfo> knownThreats = new ArrayList<>(mAppThreatRepo.getAppThreats());
        for (AppThreatInfo threat : knownThreats) {
            if (Objects.equals(threat.getPackageName(), appInfo.packageName)) {
                return;
            }
        }

        knownThreats.add(new AppThreatInfo(signature, appInfo.name, version));
        mAppThreatRepo.updateAppThreats(knownThreats);

        mUiListener.onAppThreatFound(appInfo.name, appInfo.packageName);
    }

    @Override
    public void onAppScanProgressUpdated(int progress) {
        mUiListener.onAppScanProgressUpdated(progress);
    }
}
