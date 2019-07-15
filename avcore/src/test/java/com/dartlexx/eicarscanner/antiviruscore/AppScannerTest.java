package com.dartlexx.eicarscanner.antiviruscore;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.AppThreatSignatureRepo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 24)
public class AppScannerTest {

    private static final List<AppThreatSignature> SIGNATURE_LIST;
    private static final List<ApplicationInfo> APPS_LIST_WITH_THREAT;
    private static final List<ApplicationInfo> APPS_LIST_NO_THREATS;

    static {
        SIGNATURE_LIST = new ArrayList<>(2);
        SIGNATURE_LIST.add(new AppThreatSignature(42, "com.test.virus"));
        SIGNATURE_LIST.add(new AppThreatSignature(66, "org.virus.eicar",
                33, 99));

        APPS_LIST_WITH_THREAT = new ArrayList<>(2);
        ApplicationInfo app1 = new ApplicationInfo();
        app1.packageName = "com.test.app";
        APPS_LIST_WITH_THREAT.add(app1);
        ApplicationInfo app2 = new ApplicationInfo();
        app2.packageName = "org.virus.eicar";
        APPS_LIST_WITH_THREAT.add(app2);

        APPS_LIST_NO_THREATS = new ArrayList<>(1);
        APPS_LIST_NO_THREATS.add(app1);
    }

    private final ThreatFoundListener mListener = mock(ThreatFoundListener.class);
    private final AppThreatSignatureRepo mRepo = mock(AppThreatSignatureRepo.class);
    private final PackageManager mPackMan = mock(PackageManager.class);
    private AppScanner mAppScanner;

    @Before
    public void setUp() throws Exception {
        mAppScanner = new AppScanner(mPackMan, mRepo);
        mAppScanner.setThreatListener(mListener);
        doReturn(SIGNATURE_LIST).when(mRepo).getAppSignatures();
    }

    @After
    public void tearDown() throws Exception {
        mAppScanner.setThreatListener(null);
    }

    @Test
    public void whenThereAreNoThreatsThenListenerIsNotInvoked() throws Exception {
        doReturn(APPS_LIST_NO_THREATS).when(mPackMan).getInstalledApplications(anyInt());

        mAppScanner.scanInstalledApps();

        verify(mRepo).getAppSignatures();
        verify(mPackMan).getInstalledApplications(anyInt());
        verifyZeroInteractions(mListener);
    }

    @Test
    public void whenThereIsAThreatThenListenerIsInvoked() throws Exception {
        doReturn(APPS_LIST_WITH_THREAT).when(mPackMan).getInstalledApplications(anyInt());
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionCode = 77;
        doReturn(packageInfo).when(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());

        mAppScanner.scanInstalledApps();

        verify(mRepo).getAppSignatures();
        verify(mPackMan).getInstalledApplications(anyInt());
        verify(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());
        verify(mListener).onAppThreatFound(SIGNATURE_LIST.get(1), APPS_LIST_WITH_THREAT.get(1), 77);
    }

    @Test
    public void whenAppVersionDoesNotMatchSignatureThenListenerIsNotInvoked() throws Exception {
        doReturn(APPS_LIST_WITH_THREAT).when(mPackMan).getInstalledApplications(anyInt());
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionCode = 1;
        doReturn(packageInfo).when(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());

        mAppScanner.scanInstalledApps();

        verify(mRepo).getAppSignatures();
        verify(mPackMan).getInstalledApplications(anyInt());
        verify(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());
        verifyZeroInteractions(mListener);
    }

    @Test
    public void whenCantGetPackageNameThenSignaturesMaxVersionIsUsed() throws Exception {
        doReturn(APPS_LIST_WITH_THREAT).when(mPackMan).getInstalledApplications(anyInt());
        doThrow(new PackageManager.NameNotFoundException()).when(mPackMan)
                .getPackageInfo(eq("org.virus.eicar"), anyInt());

        mAppScanner.scanInstalledApps();

        verify(mRepo).getAppSignatures();
        verify(mPackMan).getInstalledApplications(anyInt());
        verify(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());
        verify(mListener).onAppThreatFound(SIGNATURE_LIST.get(1), APPS_LIST_WITH_THREAT.get(1), 99);
    }

    @Test
    public void whenListenerIsUnregisteredThenOldListenerIsNotInvoked() throws Exception {
        mAppScanner.setThreatListener(null);

        doReturn(APPS_LIST_WITH_THREAT).when(mPackMan).getInstalledApplications(anyInt());
        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionCode = 77;
        doReturn(packageInfo).when(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());

        mAppScanner.scanInstalledApps();

        verify(mRepo).getAppSignatures();
        verify(mPackMan).getInstalledApplications(anyInt());
        verify(mPackMan).getPackageInfo(eq("org.virus.eicar"), anyInt());
        verifyZeroInteractions(mListener);
    }

    @Test
    public void whenCheckOfAppWithThreatIsCalledThenListenerIsInvoked() throws Exception {
        ApplicationInfo updatedApp = new ApplicationInfo();
        updatedApp.packageName = "com.test.virus";

        PackageInfo packageInfo = new PackageInfo();
        packageInfo.versionCode = 13;
        doReturn(packageInfo).when(mPackMan).getPackageInfo(eq("com.test.virus"), anyInt());

        mAppScanner.checkNewOrUpdateApp(updatedApp);

        verify(mRepo).getAppSignatures();
        verify(mPackMan, never()).getInstalledApplications(anyInt());
        verify(mPackMan).getPackageInfo(eq("com.test.virus"), anyInt());
        verify(mListener).onAppThreatFound(SIGNATURE_LIST.get(0), updatedApp, 13);
    }
}
