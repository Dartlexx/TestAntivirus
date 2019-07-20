package com.dartlexx.eicarscanner.avcore.files;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.InputStream;

public interface ZipEntryMatchListener {

    void onZipEntryMatchFound(@NonNull String zipEntryName,
                              @NonNull File zipFile,
                              @NonNull InputStream zipEntryStream);
}
