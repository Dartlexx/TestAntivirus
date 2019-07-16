package com.dartlexx.eicarscanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.ui.ThreatFoundUiListener;
import com.dartlexx.eicarscanner.di.AppComponent;

public class MainActivity extends AppCompatActivity implements ThreatFoundUiListener {

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
        AppComponent appComponent = ((App) getApplication()).getAppComponent();
        return appComponent.getAvDispatcher(this);
    }
}
