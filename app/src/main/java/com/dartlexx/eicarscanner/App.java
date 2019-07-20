package com.dartlexx.eicarscanner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;
import com.dartlexx.eicarscanner.di.AppComponent;
import com.dartlexx.eicarscanner.receivers.AppInstalledReceiver;

import java.util.HashMap;
import java.util.Map;

public final class App extends Application {

    private static final String FIRST_RUN_PREFS = "firstRun";
    private static final String IS_FIRST_RUN_KEY = "isFirstRun";

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = new AppComponent(this);

        if (isFirstRun()) {
            setupTestSignatures();
        }
        registerAvReceivers();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private boolean isFirstRun() {
        final SharedPreferences prefs = getSharedPreferences(FIRST_RUN_PREFS, Context.MODE_PRIVATE);
        boolean result = prefs.getBoolean(IS_FIRST_RUN_KEY, true);

        if (result) {
            prefs.edit()
                    .putBoolean(IS_FIRST_RUN_KEY, false)
                    .apply();
        }
        return result;
    }

    private void setupTestSignatures() {
        final Map<String, AppThreatSignature> appSignatures = new HashMap<>(2);
        appSignatures.put("com.zoner.android.eicar",
                new AppThreatSignature(1, "com.zoner.android.eicar"));
        appSignatures.put("com.fsecure.eicar.antivirus.test",
                new AppThreatSignature(2, "com.fsecure.eicar.antivirus.test"));

        final Map<String, FileThreatSignature> fileSignatures = new HashMap<>(2);
        fileSignatures.put(".+\\.com", new FileThreatSignature(1, "EICAR",
                ".+\\.com", "EICAR-STANDARD-ANTIVIRUS-TEST-FILE"));
        fileSignatures.put(".+\\.tmp", new FileThreatSignature(1, "EICAR",
                ".+\\.tmp", "EICAR-STANDARD-ANTIVIRUS-TEST-FILE"));

        final ThreatSignatureRepo repo = mAppComponent.getThreatSignaturesRepo();
        repo.updateAppSignatures(appSignatures);
        repo.updateFileSignatures(fileSignatures);
    }

    private void registerAvReceivers() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(new AppInstalledReceiver(), filter);
    }
}
