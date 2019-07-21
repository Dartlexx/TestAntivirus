package com.dartlexx.eicarscanner.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dartlexx.eicarscanner.R;
import com.dartlexx.eicarscanner.service.ScanService;

public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[] { Manifest.permission.READ_EXTERNAL_STORAGE };
    private static final int REQUEST_CODE = 42;

    private View mLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startScanButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanService.startFullScan(MainActivity.this);
            }
        });

        Button stopButton = findViewById(R.id.stopScansButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanService.stopAllScans(MainActivity.this);
            }
        });

        mLabel = findViewById(R.id.label_no_storage_permission);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            mLabel.setVisibility(View.VISIBLE);
            requestPermissions(PERMISSIONS, REQUEST_CODE);
        } else {
            mLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean hasPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
        mLabel.setVisibility(hasPermission ? View.INVISIBLE : View.VISIBLE);
    }
}
