package com.dartlexx.eicarscanner.common.avcore;

public interface ScanStateListener {

    void onScanStarted();

    void onScanProgressChanged(int progress);

    void onScanFinished();
}
