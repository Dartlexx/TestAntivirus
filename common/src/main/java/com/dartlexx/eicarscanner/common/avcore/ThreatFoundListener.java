package com.dartlexx.eicarscanner.common.avcore;

import androidx.annotation.NonNull;

public interface ThreatFoundListener {

    void onAppThreatFound(@NonNull String appName, @NonNull String packageName);

    void onFileThreatFound(@NonNull String fileName, @NonNull String absolutePath);

    void onAppThreatRemoved(@NonNull String packageName);

    void onFileThreatRemoved(@NonNull String filePath);
}
