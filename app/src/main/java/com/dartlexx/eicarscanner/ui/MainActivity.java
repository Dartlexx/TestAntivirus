package com.dartlexx.eicarscanner.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dartlexx.eicarscanner.R;
import com.dartlexx.eicarscanner.service.ScanService;

public class MainActivity extends AppCompatActivity {

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
    }
}
