package com.dartlexx.eicarscanner.avcore.files;

import androidx.annotation.NonNull;

import com.dartlexx.eicarscanner.avcore.common.ThreatProcessor;
import com.dartlexx.eicarscanner.common.avcore.ScanStateListener;
import com.dartlexx.eicarscanner.common.models.FileThreatSignature;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public final class FileScanner {

    private final ThreatSignatureRepo mSignaturesRepo;
    private final ThreatProcessor mThreatProcessor;
    private final ZipFileHelper mZipHelper;

    private volatile boolean mStopFileScan;
    private WeakReference<MatchedFileChecker> mFileChecker;

    public FileScanner(@NonNull ThreatSignatureRepo repo,
                       @NonNull ThreatProcessor threatProcessor,
                       @NonNull ZipFileHelper zipHelper) {
        mSignaturesRepo = repo;
        mThreatProcessor = threatProcessor;
        mZipHelper = zipHelper;
    }

    void scanFolders(@NonNull ScanStateListener listener,
                     @NonNull List<File> folders,
                     int totalFilesCount) {
        mStopFileScan = false;
        listener.onScanStarted();
        listener.onScanProgressChanged(0);

        final Map<String, FileThreatSignature> signatures = mSignaturesRepo.getFileSignatures();
        final MatchedFileChecker checker = new MatchedFileChecker(signatures, mThreatProcessor);
        mFileChecker = new WeakReference<>(checker);

        int filesChecked = 0;
        for (File flatFolder : folders) {
            if (mStopFileScan) {
                break;
            }

            final File[] children = flatFolder.listFiles();
            for (File child : children) {
                if (mStopFileScan) {
                    break;
                }

                scanFile(child, signatures, checker);
                filesChecked++;
            }

            listener.onScanProgressChanged(filesChecked * 100 / totalFilesCount);
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
