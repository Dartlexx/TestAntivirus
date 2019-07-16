package com.dartlexx.eicarscanner;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dartlexx.eicarscanner.avcore.AvCoreServiceProvider;
import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.ui.ThreatFoundUiListener;
import com.dartlexx.eicarscanner.repository.RepositoryServiceProvider;
import com.dartlexx.eicarscanner.storage.StorageServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ThreatFoundUiListener {

    private final StorageServiceProvider mStorageProvider = new StorageServiceProvider();
    private final RepositoryServiceProvider mRepoProvider = new RepositoryServiceProvider();
    private final AvCoreServiceProvider mAvCoreProvider = new AvCoreServiceProvider();

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);

        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvDispatcher().scanInstalledApps();
            }
        });

        Button stopButton = findViewById(R.id.button2);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvDispatcher().stopAppsScan();
            }
        });

        List<AppThreatSignature> signatures = new ArrayList<>(2);
        signatures.add(new AppThreatSignature(1, "com.zoner.android.eicar"));
        signatures.add(new AppThreatSignature(2, "com.fsecure.eicar.antivirus.test"));

        AppThreatSignatureStorage storage = mStorageProvider.getAppSignatureStorage(getApplicationContext());
        storage.updateAppSignatures(signatures);
    }

    @Override
    public void onAppThreatFound(@NonNull final String appName, @NonNull String packageName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Found new app threat: " + appName,
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onAppScanProgressUpdated(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(progress);
            }
        });
    }

    private AvDispatcher getAvDispatcher() {
        Context appContext = getApplicationContext();
        AppThreatSignatureStorage appSignStorage = mStorageProvider.getAppSignatureStorage(appContext);
        FoundAppThreatStorage appThreatStorage = mStorageProvider.getAppThreatStorage(appContext);

        AppThreatSignatureRepo appSignRepo = mRepoProvider.getAppSignatureRepo(appSignStorage);
        FoundAppThreatRepo appThreatRepo = mRepoProvider.getAppThreatRepo(appThreatStorage);

        return mAvCoreProvider.getAvDispatcher(
                appContext.getPackageManager(),
                appSignRepo,
                appThreatRepo,
                this);
    }
}
