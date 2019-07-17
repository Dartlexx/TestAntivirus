package com.dartlexx.eicarscanner;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.di.AppComponent;
import com.dartlexx.eicarscanner.receivers.AppInstalledReceiver;

import java.util.ArrayList;
import java.util.List;

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
        final List<AppThreatSignature> signatures = new ArrayList<>(2);
        signatures.add(new AppThreatSignature(1, "com.zoner.android.eicar"));
        signatures.add(new AppThreatSignature(2, "com.fsecure.eicar.antivirus.test"));

        final AppThreatSignatureRepo repo = mAppComponent.getAppThreatSignaturesRepo();
        repo.updateAppSignatures(signatures);
    }

    private void registerAvReceivers() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(new AppInstalledReceiver(), filter);
    }
}
