package com.dartlexx.eicarscanner.di;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dartlexx.eicarscanner.avcore.AvCoreServiceProvider;
import com.dartlexx.eicarscanner.avcore.AvDispatcher;
import com.dartlexx.eicarscanner.common.repository.ThreatSignatureRepo;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.repository.FoundFileThreatRepo;
import com.dartlexx.eicarscanner.common.storage.AppThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FileThreatSignatureStorage;
import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;
import com.dartlexx.eicarscanner.repository.RepositoryServiceProvider;
import com.dartlexx.eicarscanner.ui.NotificationHelper;
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

    @Nullable
    private NotificationHelper mNotificationHelper;

    public AppComponent(@NonNull Context appContext) {
        mAppContext = appContext;
    }

    @NonNull
    public ThreatSignatureRepo getThreatSignaturesRepo() {
        AppThreatSignatureStorage appStorage = getStorageService().getAppSignatureStorage(mAppContext);
        FileThreatSignatureStorage fileStorage = getStorageService().getFileSignatureStorage(mAppContext);
        return getRepoService().getThreatSignatureRepo(appStorage, fileStorage);
    }

    @NonNull
    public AvDispatcher getAvDispatcher() {
        PackageManager packageManager = mAppContext.getPackageManager();
        ThreatSignatureRepo signatureRepo = getThreatSignaturesRepo();
        FoundAppThreatRepo appThreatRepo = getFoundAppThreatRepo();
        FoundFileThreatRepo fileThreatRepo = getFoundFileThreatRepo();
        ThreatFoundListener listener = getThreatFoundListener();

        return getAvCoreService().getAvDispatcher(packageManager, signatureRepo, appThreatRepo,
                fileThreatRepo, listener);
    }

    @NonNull
    public NotificationHelper getNotificationHelper() {
        if (mNotificationHelper == null) {
            final NotificationManager manager = (NotificationManager) mAppContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationHelper = new NotificationHelper(manager, mAppContext);
        }
        return mNotificationHelper;
    }

    @NonNull
    private FoundAppThreatRepo getFoundAppThreatRepo() {
        FoundAppThreatStorage appStorage = getStorageService().getAppThreatStorage(mAppContext);
        FoundFileThreatStorage fileStorage = getStorageService().getFileThreatStorage(mAppContext);
        return getRepoService().getAppThreatRepo(appStorage, fileStorage);
    }

    @NonNull
    private FoundFileThreatRepo getFoundFileThreatRepo() {
        FoundAppThreatStorage appStorage = getStorageService().getAppThreatStorage(mAppContext);
        FoundFileThreatStorage fileStorage = getStorageService().getFileThreatStorage(mAppContext);
        return getRepoService().getFileThreatRepo(appStorage, fileStorage);
    }

    @NonNull
    private ThreatFoundListener getThreatFoundListener() {
        if (mThreatFoundListener == null) {
            mThreatFoundListener = new ThreatFoundListenerImpl(mAppContext, getNotificationHelper());
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
