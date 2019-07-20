package com.dartlexx.eicarscanner.avcore.files;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipFileHelper {

    private static final String TAG = ZipFileHelper.class.getSimpleName();
    private static final byte[] ZIP_FILE_SIGNATURE = new byte[]{0x50, 0x4b, 0x03, 0x04};

    private volatile boolean mStopReading;

    void stopReading() {
        mStopReading = true;
    }

    void findMatchingFilesInZip(@NonNull File zipFile,
                                @NonNull Set<String> fileNameMasks,
                                @NonNull ZipEntryMatchListener matchListener) {
        mStopReading = false;

        try (ZipInputStream input =
                     new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)))) {

            ZipEntry entry = input.getNextEntry();
            while (entry != null) {
                if (mStopReading) {
                    break;
                }
                if (entry.isDirectory()) {
                    entry = input.getNextEntry();
                    continue;
                }

                for (String mask: fileNameMasks) {
                    if (entry.getName().matches(mask)) {
                        matchListener.onZipEntryMatchFound(entry.getName(), zipFile, input);
                        break;
                    }
                }
                entry = input.getNextEntry();
            }
        } catch (FileNotFoundException exc) {
            Log.e(TAG, "Failed to find file " + zipFile.getAbsolutePath() + ": " + exc.getMessage());
        } catch (IOException exc) {
            Log.w(TAG, "Failed to read contents of zip archive " + zipFile.getAbsolutePath() +
                    ": " + exc.getMessage());
        }
    }

    boolean isZipFile(@NonNull File file) {
        if (file.length() < ZIP_FILE_SIGNATURE.length) {
            return false;
        }
        byte[] buffer = new byte[ZIP_FILE_SIGNATURE.length];

        try (InputStream input = new FileInputStream(file)) {
            //noinspection ResultOfMethodCallIgnored
            input.read(buffer);
        } catch (IOException exc) {
            Log.e(TAG, "Failed to check if file " + file.getAbsolutePath() + " is ZIP: " +
                    exc.getMessage());
        }
        return Arrays.equals(buffer, ZIP_FILE_SIGNATURE);
    }
}
