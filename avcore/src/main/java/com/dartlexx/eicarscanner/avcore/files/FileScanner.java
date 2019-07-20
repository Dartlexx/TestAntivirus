package com.dartlexx.eicarscanner.avcore.files;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.avcore.common.ThreatProcessor;
import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;

public final class FileScanner {

    @NonNull
    private final ThreatSignatureRepo mSignaturesRepo;

    @NonNull
    private final ThreatProcessor mThreatProcessor;

    @NonNull
    private final ZipFileHelper mZipHelper;

    @NonNull
    private final FilesListHelper mFilesListHelper;

    private volatile boolean mStopFileScan;

    @Nullable
    private WeakReference<MatchedFileChecker> mFileChecker;

    public FileScanner(@NonNull ThreatSignatureRepo repo,
                       @NonNull ThreatProcessor threatProcessor,
                       @NonNull ZipFileHelper zipHelper,
                       @NonNull FilesListHelper filesListHelper) {
        mSignaturesRepo = repo;
        mThreatProcessor = threatProcessor;
        mZipHelper = zipHelper;
        mFilesListHelper = filesListHelper;
    }

    public void scanFileSystem(@NonNull ScanStateListener listener) {
        mStopFileScan = false;
        listener.onScanStarted();
        listener.onScanProgressChanged(0);

        final FilesListHelper.PlainReadableFolders folders = mFilesListHelper.getPlainReadableFolders();
        if (folders.plainFolders.isEmpty() || folders.totalFilesCount == 0) {
            return;
        }

        final Map<String, FileThreatSignature> signatures = mSignaturesRepo.getFileSignatures();
        final MatchedFileChecker checker = new MatchedFileChecker(signatures, mThreatProcessor);
        mFileChecker = new WeakReference<>(checker);

        int filesChecked = 0;
        int progress = 0;
        for (File flatFolder : folders.plainFolders) {
            if (mStopFileScan) {
                break;
            }

            final File[] children = flatFolder.listFiles();
            for (File child : children) {
                if (mStopFileScan) {
                    break;
                }

                Log.d("FILE-SCAN", "Check file " + child.getAbsolutePath());

                scanFile(child, signatures, checker);
                filesChecked++;

                int newProgress = filesChecked * 100 / folders.totalFilesCount;
                if (newProgress != progress) {
                    listener.onScanProgressChanged(newProgress);
                    progress = newProgress;
                }
            }
        }

        listener.onScanProgressChanged(100);
        listener.onScanFinished();
        mFileChecker = null;
    }

    public void scanSingleFile(@NonNull File fileToCheck,
                               @NonNull ScanStateListener listener) {
        mStopFileScan = false;
        listener.onScanStarted();
        listener.onScanProgressChanged(0);

        final Map<String, FileThreatSignature> signatures = mSignaturesRepo.getFileSignatures();
        final MatchedFileChecker checker = new MatchedFileChecker(signatures, mThreatProcessor);

        scanFile(fileToCheck, signatures, checker);

        listener.onScanProgressChanged(100);
        listener.onScanFinished();
    }

    public void stopScan() {
        mStopFileScan = true;

        MatchedFileChecker checker = mFileChecker != null ? mFileChecker.get() : null;
        if (checker != null) {
            checker.stopChecking();
        }
        mZipHelper.stopReading();
    }

    public void onFileThreatWasDeleted(@NonNull String filePath) {
        mThreatProcessor.onFileThreatWasDeleted(filePath);
    }

    private void scanFile(@NonNull File fileToCheck,
                          @NonNull Map<String, FileThreatSignature> signatures,
                          @NonNull MatchedFileChecker checker) {
        if (!fileToCheck.exists() || !fileToCheck.isFile() || !fileToCheck.canRead()) {
            return;
        }

        if (mZipHelper.isZipFile(fileToCheck)) {
            mZipHelper.findMatchingFilesInZip(fileToCheck, signatures.keySet(), checker);
        } else {
            for (String mask : signatures.keySet()) {
                if (mStopFileScan) {
                    break;
                }

                if (fileToCheck.getName().matches(mask)) {
                    checker.checkMatchedFile(fileToCheck);

                    // No need to find other matches as they will be checked inside 'checker'
                    break;
                }
            }
        }
    }
}
