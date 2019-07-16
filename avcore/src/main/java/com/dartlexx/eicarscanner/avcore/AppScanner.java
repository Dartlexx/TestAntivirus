package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class AppScanner {

    private static final String TAG = AppScanner.class.getSimpleName();
    private static final int GET_INSTALLED_APPS_FLAGS = 0;
    private static final int GET_PACKAGE_INFO_FLAGS = 0;

    @NonNull
    private final PackageManager mPackageMan;

    @NonNull
    private final AppThreatSignatureRepo mSignatureRepo;

    @Nullable
    private volatile ThreatProcessor mListener;

    private volatile boolean mStopAppScan;

    AppScanner(@NonNull PackageManager packageManager,
               @NonNull AppThreatSignatureRepo repo) {
        mPackageMan = packageManager;
        mSignatureRepo = repo;
    }

    synchronized void setThreatListener(@Nullable ThreatProcessor listener) {
        mListener = listener;
    }

    void scanInstalledApps() {
        mStopAppScan = false;

        final List<ApplicationInfo> appsList = mPackageMan.getInstalledApplications(GET_INSTALLED_APPS_FLAGS);
        final Map<String, AppThreatSignature> signatures = toMap(mSignatureRepo.getAppSignatures());

        updateProgress(0);
        for (int i = 0; i < appsList.size(); i++) {
            if (mStopAppScan) {
                break;
            }

            // Emulate long check
            try {
                Thread.sleep(50);
            } catch (Exception ignored) {
            }

            ApplicationInfo app = appsList.get(i);
            AppThreatSignature match = signatures.get(app.packageName);
            if (match != null) {
                checkSingleApp(app, match);
            }
            updateProgress(i * 100 / appsList.size());
        }
        updateProgress(100);
    }

    void checkNewOrUpdatedApp(@NonNull ApplicationInfo app) {
        mStopAppScan = false;
        final List<AppThreatSignature> signaturesList = mSignatureRepo.getAppSignatures();

        updateProgress(0);
        for (AppThreatSignature signature : signaturesList) {
            if (mStopAppScan) {
                break;
            }

            if (Objects.equals(signature.getPackageName(), app.packageName)) {
                checkSingleApp(app, signature);
            }
        }
        updateProgress(100);
    }

    void stopScan() {
        mStopAppScan = true;
    }

    private void checkSingleApp(@NonNull ApplicationInfo app,
                                @NonNull AppThreatSignature match) {
        final String packName = app.packageName;
        Log.d(TAG, "Found app matching threat signature by packageName = " + packName);

        int version = match.getTopVersion();
        try {
            PackageInfo info = mPackageMan.getPackageInfo(packName, GET_PACKAGE_INFO_FLAGS);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException exc) {
            Log.w(TAG, "Failed to find package info for suspected threat: " + packName);
        }

        if (version >= match.getLowVersion() && version <= match.getTopVersion()) {
            Log.d(TAG, "Found App Threat: " + packName);
            notifyListener(match, app, version);
        }
    }

    private synchronized void notifyListener(@NonNull AppThreatSignature signature,
                                             @NonNull ApplicationInfo app,
                                             int version) {
        if (mListener != null) {
            //noinspection ConstantConditions
            mListener.onAppThreatFound(signature, app, version);
        }
    }

    private synchronized void updateProgress(int progress) {
        if (mListener != null) {
            //noinspection ConstantConditions
            mListener.onAppScanProgressUpdated(progress);
        }
    }

    @NonNull
    private static Map<String, AppThreatSignature> toMap(@NonNull List<AppThreatSignature> signatureList) {
        final Map<String, AppThreatSignature> result = new HashMap<>(signatureList.size());
        for (AppThreatSignature signature : signatureList) {
            result.put(signature.getPackageName(), signature);
        }
        return result;
    }
}
