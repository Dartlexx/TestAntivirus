package com.dartlexx.eicarscanner.avcore;

import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.ui.ThreatFoundUiListener;

public final class AvCoreServiceProvider {

    @Nullable
    private AvDispatcher mDispatcher;

    @Nullable
    private AppScanner mAppScanner;

    @Nullable
    private ThreatProcessor mThreatProcessor;

    @NonNull
    public AvDispatcher getAvDispatcher(@NonNull PackageManager packageManager,
                                        @NonNull AppThreatSignatureRepo signatureRepo,
                                        @NonNull FoundAppThreatRepo threatRepo,
                                        @NonNull ThreatFoundUiListener listener) {
        if (mDispatcher == null) {
            ThreatProcessor processor = getThreatProcessor(threatRepo, listener);
            AppScanner scanner = getAppScanner(packageManager, signatureRepo, processor);
            mDispatcher = new AvDispatcher(scanner);
        }
        return mDispatcher;
    }

    @NonNull
    private AppScanner getAppScanner(@NonNull PackageManager packageManager,
                                     @NonNull AppThreatSignatureRepo repo,
                                     @NonNull ThreatProcessor processor) {
        if (mAppScanner == null) {
            mAppScanner = new AppScanner(packageManager, repo);
            mAppScanner.setThreatListener(processor);
        }
        return mAppScanner;
    }

    @NonNull
    private ThreatProcessor getThreatProcessor(@NonNull FoundAppThreatRepo repo,
                                               @NonNull ThreatFoundUiListener listener) {
        if (mThreatProcessor == null) {
            mThreatProcessor = new ThreatProcessorImpl(repo, listener);
        }
        return mThreatProcessor;
    }
}
