package com.dartlexx.eicarscanner.avcore;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.avcore.apps.AppScanner;
import com.dartlexx.eicarscanner.avcore.common.ThreatProcessor;
import com.dartlexx.eicarscanner.avcore.files.FileScanner;
import com.dartlexx.eicarscanner.avcore.files.ZipFileHelper;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;

public final class AvCoreServiceProvider {

    @Nullable
    private AvDispatcher mDispatcher;

    @Nullable
    private AppScanner mAppScanner;

    @Nullable
    private FileScanner mFileScanner;

    @Nullable
    private ThreatProcessor mThreatProcessor;

    @NonNull
    public AvDispatcher getAvDispatcher(@NonNull PackageManager packageManager,
                                        @NonNull ThreatSignatureRepo signatureRepo,
                                        @NonNull FoundAppThreatRepo appThreatRepo,
                                        @NonNull FoundFileThreatRepo fileThreatRepo,
                                        @NonNull ThreatFoundListener listener) {
        if (mDispatcher == null) {
            ThreatProcessor processor = getThreatProcessor(appThreatRepo, fileThreatRepo, listener);
            AppScanner appScanner = getAppScanner(packageManager, signatureRepo, processor);
            FileScanner fileScanner = getFileScanner(signatureRepo, processor);
            mDispatcher = new AvDispatcher(appScanner, fileScanner);
        }
        return mDispatcher;
    }

    @NonNull
    private AppScanner getAppScanner(@NonNull PackageManager packageManager,
                                     @NonNull ThreatSignatureRepo repo,
                                     @NonNull ThreatProcessor processor) {
        if (mAppScanner == null) {
            mAppScanner = new AppScanner(packageManager, repo, processor);
        }
        return mAppScanner;
    }

    @NonNull
    private FileScanner getFileScanner(@NonNull ThreatSignatureRepo signatureRepo,
                                       @NonNull ThreatProcessor threatProcessor) {
        if (mFileScanner == null) {
            mFileScanner = new FileScanner(signatureRepo, threatProcessor, new ZipFileHelper());
        }
        return mFileScanner;
    }

    @NonNull
    private ThreatProcessor getThreatProcessor(@NonNull FoundAppThreatRepo appThreatRepo,
                                               @NonNull FoundFileThreatRepo fileThreatRepo,
                                               @NonNull ThreatFoundListener listener) {
        if (mThreatProcessor == null) {
            mThreatProcessor = new ThreatProcessor(appThreatRepo, fileThreatRepo, listener);
        }
        return mThreatProcessor;
    }
}
