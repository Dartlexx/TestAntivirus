package com.dartlexx.eicarscanner.avcore.files;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.avcore.common.ThreatProcessor;
import com.dartlexx.eicarscanner.common.models.FileThreatInfo;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

final class MatchedFileChecker implements ZipEntryMatchListener {

    private static final String TAG = MatchedFileChecker.class.getSimpleName();

    private static final int READ_BUFFER_SIZE = 8192;
    private static final int BUFFER_MAX_SIZE = 100_000;
    private static final String ZIP_FILE_CONTENT_FORMAT = "%s::%s";

    @NonNull
    private final Map<String, FileThreatSignature> mSignatures;

    @NonNull
    private final ThreatProcessor mThreatProcessor;

    @NonNull
    private final ByteBuffer mBuffer;

    private volatile boolean mStopChecking;

    MatchedFileChecker(@NonNull Map<String, FileThreatSignature> signatures,
                       @NonNull ThreatProcessor threatProcessor) {
        mSignatures = signatures;
        mThreatProcessor = threatProcessor;
        mBuffer = ByteBuffer.allocate(BUFFER_MAX_SIZE);
    }

    @Override
    public void onZipEntryMatchFound(@NonNull String zipEntryName,
                                     @NonNull File zipFile,
                                     @NonNull InputStream zipEntryStream) {
        String fileName = String.format(ZIP_FILE_CONTENT_FORMAT, zipFile.getName(), zipEntryName);
        try {
            checkMatchedFile(fileName, zipFile.getAbsolutePath(), zipEntryStream);
        } catch (IOException exc) {
            Log.e(TAG, "Failed to check contents of zip entry : " + zipFile.getName() + " at " + zipFile.getAbsolutePath() +
                    ": " + exc.getMessage());
        }
    }

    void checkMatchedFile(@NonNull File fileToCheck) {
        try (InputStream stream = new FileInputStream(fileToCheck)) {
            checkMatchedFile(fileToCheck.getName(), fileToCheck.getAbsolutePath(), stream);
        } catch (IOException exc) {
            Log.e(TAG, "Failed to check contents of file: " + fileToCheck.getName() + " at " + fileToCheck.getAbsolutePath() +
                    ": " + exc.getMessage());
        }
    }

    void stopChecking() {
        mStopChecking = true;
    }

    private void checkMatchedFile(@NonNull String fileName,
                                  @NonNull String filePath,
                                  @NonNull InputStream fileStream) throws IOException {
        mStopChecking = false;

        readFileToBuffer(fileStream);
        String contents = new String(mBuffer.array(), 0, mBuffer.position(), StandardCharsets.UTF_8);

        for (FileThreatSignature signature : mSignatures.values()) {
            if (mStopChecking) {
                break;
            }

            if (fileName.matches(signature.getFileNameMask()) &&
                    contents.contains(signature.getContentPart())) {
                FileThreatInfo info = new FileThreatInfo(signature.getId(), fileName, filePath);
                mThreatProcessor.onFileThreatFound(info);

                // No need to check other signatures since we found a threat
                break;
            }
        }
    }

    private void readFileToBuffer(@NonNull InputStream inputStream) throws IOException {
        int readBytes;
        byte[] readBuffer = new byte[READ_BUFFER_SIZE];
        mBuffer.clear();

        while ((readBytes = inputStream.read(readBuffer)) > 0) {
            if (mStopChecking) {
                break;
            }
            mBuffer.put(readBuffer, 0, readBytes);
        }
    }
}
