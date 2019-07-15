package com.dartlexx.eicarscanner.common.ui;

import androidx.annotation.NonNull;

public interface ThreatFoundUiListener {

    void onAppThreatFound(@NonNull String appName, @NonNull String packageName);
}
