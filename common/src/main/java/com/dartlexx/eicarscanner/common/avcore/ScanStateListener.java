package com.dartlexx.eicarscanner.common.avcore;

public interface ScanStateListener {

    void onScanStarted(boolean isPartialScam);

    void onScanProgressChanged(int progress);

    void onScanFinished();
}
