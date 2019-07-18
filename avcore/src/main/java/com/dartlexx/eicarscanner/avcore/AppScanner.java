package com.dartlexx.eicarscanner.avcore;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;
import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;

import java.util.List;
import java.util.Map;

final class AppScanner {

    private static final String TAG = AppScanner.class.getSimpleName();
    private static final int GET_INSTALLED_APPS_FLAGS = 0;
    private static final int GET_PACKAGE_INFO_FLAGS = 0;

    @NonNull
    private final PackageManager mPackageMan;

    @NonNull
    private final AppThreatSignatureRepo mSignatureRepo;

    @NonNull
    private final ThreatProcessor mProcessor;

    private volatile boolean mStopAppScan;

    AppScanner(@NonNull PackageManager packageManager,
               @NonNull AppThreatSignatureRepo repo,
               @NonNull ThreatProcessor processor) {
        mPackageMan = packageManager;
        mSignatureRepo = repo;
        mProcessor = processor;
    }

    void scanInstalledApps(@NonNull ScanStateListener listener) {
        mStopAppScan = false;

        listener.onScanStarted();
        listener.onScanProgressChanged(0);

        final List<ApplicationInfo> appsList = mPackageMan.getInstalledApplications(GET_INSTALLED_APPS_FLAGS);
        final Map<String, AppThreatSignature> signatures = mSignatureRepo.getAppSignatures();

        for (int i = 0; i < appsList.size(); i++) {
            if (mStopAppScan) {
                break;
            }

            // Emulate long check
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }

            ApplicationInfo app = appsList.get(i);
            AppThreatSignature match = signatures.get(app.packageName);
            if (match != null) {
                checkSingleApp(app, match);
            }
            listener.onScanProgressChanged((i + 1) * 100 / appsList.size());
        }

        listener.onScanProgressChanged(100);
        listener.onScanFinished();
    }

    void checkNewOrUpdatedApp(@NonNull ApplicationInfo app,
                              @NonNull ScanStateListener listener) {
        mStopAppScan = false;
        listener.onScanStarted();
        listener.onScanProgressChanged(0);

        // Emulate long check
        try {
            Thread.sleep(250);
        } catch (InterruptedException ignored) {
        }

        AppThreatSignature signature = mSignatureRepo.getAppSignatures().get(app.packageName);
        if (signature != null && !mStopAppScan) {
            checkSingleApp(app, signature);
        }

        listener.onScanProgressChanged(100);
        listener.onScanFinished();
    }

    void stopScan() {
        mStopAppScan = true;
    }

    void checkRemovedApp(@NonNull String packageName) {
        mProcessor.onAppDeleted(packageName);
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

            CharSequence appName = mPackageMan.getApplicationLabel(app);

            AppThreatInfo foundThreat = new AppThreatInfo(match,
                    appName != null ? appName.toString() : app.packageName,
                    version);
            mProcessor.onAppThreatFound(foundThreat);
        }
    }
}
