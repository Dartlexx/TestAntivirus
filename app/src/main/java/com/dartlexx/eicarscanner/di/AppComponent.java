package com.dartlexx.eicarscanner.di;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.avcore.AvCoreServiceProvider;
import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;
import com.dartlexx.eicarscanner.repository.RepositoryServiceProvider;
import com.dartlexx.eicarscanner.storage.StorageServiceProvider;
import com.dartlexx.eicarscanner.ui.ThreatFoundListenerImpl;

public final class AppComponent {

    @NonNull
    private final Context mAppContext;

    @Nullable
    private StorageServiceProvider mStorageService;

    @Nullable
    private RepositoryServiceProvider mRepoService;

    @Nullable
    private AvCoreServiceProvider mAvCoreService;

    @Nullable
    private ThreatFoundListener mThreatFoundListener;

    public AppComponent(@NonNull Context appContext) {
        mAppContext = appContext;
    }

    @NonNull
    public AppThreatSignatureRepo getAppThreatSignaturesRepo() {
        AppThreatSignatureStorage storage = getStorageService().getAppSignatureStorage(mAppContext);
        return getRepoService().getAppSignatureRepo(storage);
    }

    @NonNull
    public AvDispatcher getAvDispatcher() {
        PackageManager packageManager = mAppContext.getPackageManager();
        AppThreatSignatureRepo signatureRepo = getAppThreatSignaturesRepo();
        FoundAppThreatRepo threatRepo = getFoundAppThreatRepo();
        ThreatFoundListener listener = getThreatFoundListeer();

        return getAvCoreService().getAvDispatcher(packageManager, signatureRepo, threatRepo, listener);
    }

    @NonNull
    private FoundAppThreatRepo getFoundAppThreatRepo() {
        FoundAppThreatStorage storage = getStorageService().getAppThreatStorage(mAppContext);
        return getRepoService().getAppThreatRepo(storage);
    }

    @NonNull
    private ThreatFoundListener getThreatFoundListeer() {
        if (mThreatFoundListener == null) {
            mThreatFoundListener = new ThreatFoundListenerImpl(mAppContext);
        }
        return mThreatFoundListener;
    }

    @NonNull
    private StorageServiceProvider getStorageService() {
        if (mStorageService == null) {
            mStorageService = new StorageServiceProvider();
        }
        return mStorageService;
    }

    @NonNull
    private RepositoryServiceProvider getRepoService() {
        if (mRepoService == null) {
            mRepoService = new RepositoryServiceProvider();
        }
        return mRepoService;
    }

    @NonNull
    private AvCoreServiceProvider getAvCoreService() {
        if (mAvCoreService == null) {
            mAvCoreService = new AvCoreServiceProvider();
        }
        return mAvCoreService;
    }
}
