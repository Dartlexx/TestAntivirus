package com.dartlexx.eicarscanner.avcore.files;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

public final class FilesListHelper {

    @NonNull
    PlainReadableFolders getPlainReadableFolders() {
        final PlainReadableFolders result = new PlainReadableFolders();
        final Queue<File> toProcess = new ArrayDeque<>();

        File sdCard = getExternalStorage();
        if (sdCard != null && sdCard.canRead()) {
            toProcess.add(sdCard);
        }

        while (!toProcess.isEmpty()) {
            File folder = toProcess.poll();
            File[] children = folder.listFiles();
            boolean hasFiles = false;

            for (File child : children) {
                if (!child.canRead()) {
                    continue;
                }

                if (child.isFile() && child.length() > 0) {
                    hasFiles = true;
                    result.mTotalFilesCount++;
                } else if (child.isDirectory()) {
                    toProcess.add(child);
                }
            }

            if (hasFiles) {
                result.mPlainFolders.add(folder);
            }
        }
        return result;
    }

    @Nullable
    private File getExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    static final class PlainReadableFolders {
        final HashSet<File> mPlainFolders = new HashSet<>();
        int mTotalFilesCount;
    }
}
