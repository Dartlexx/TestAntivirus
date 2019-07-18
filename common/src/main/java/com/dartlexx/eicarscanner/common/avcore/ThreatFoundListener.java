package com.dartlexx.eicarscanner.common.avcore;

import androidx.annotation.NonNull;

public interface ThreatFoundListener {

    void onAppThreatFound(@NonNull String appName, @NonNull String packageName);

    void onAppThreatRemoved(@NonNull String packageName);
}
