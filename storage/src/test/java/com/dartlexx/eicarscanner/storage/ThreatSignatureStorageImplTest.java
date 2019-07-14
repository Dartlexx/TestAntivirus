package com.dartlexx.eicarscanner.storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.dartlexx.eicarscanner.common.models.AppThreatSignature;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 24)
public class ThreatSignatureStorageImplTest {

    private static final String PREFS_NAME = "test_prefs";
    private static final List<AppThreatSignature> EXPECTED;

    static {
        EXPECTED = new ArrayList<>(2);
        EXPECTED.add(new AppThreatSignature(42, "com.test.virus"));
        EXPECTED.add(new AppThreatSignature(66, "org.virus.eicar"));
    }

    private ThreatSignatureStorageImpl mStorage;

    @Before
    public void setUp() throws Exception {
        final Context appContext = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mStorage = new ThreatSignatureStorageImpl(prefs);
    }

    @After
    public void tearDown() throws Exception {
        mStorage = null;
        final Context appContext = ApplicationProvider.getApplicationContext();
        appContext.deleteSharedPreferences(PREFS_NAME);
    }

    @Test
    public void whenStorageEmptyNoSignaturesReturned() throws Exception {
        List<AppThreatSignature> signatures = mStorage.getAppSignatures();

        assertNotNull(signatures);
        assertTrue(signatures.isEmpty());
    }

    @Test
    public void whenSignaturesSavedThenTheyAreLoaded() throws Exception {
        mStorage.updateAppSignatures(EXPECTED);

        List<AppThreatSignature> signatures = mStorage.getAppSignatures();

        assertNotNull(signatures);
        assertEquals(EXPECTED.size(), signatures.size());
        assertTrue(signatures.containsAll(EXPECTED));
    }

    @Test
    public void whenSignaturesAreRewrittenThenCorrectAreLoaded() throws Exception {
        mStorage.updateAppSignatures(EXPECTED);

        List<AppThreatSignature> newSignatures = new ArrayList<>(1);
        newSignatures.add(new AppThreatSignature(77, "someOtherSignature"));
        mStorage.updateAppSignatures(newSignatures);

        List<AppThreatSignature> updated = mStorage.getAppSignatures();

        assertNotNull(updated);
        assertEquals(updated.size(), newSignatures.size());
        assertTrue(updated.containsAll(newSignatures));
    }
}
