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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 24)
public class ThreatSignatureStorageImplTest {

    private static final String PREFS_NAME = "test_prefs";
    private static final Map<String, AppThreatSignature> EXPECTED;

    static {
        EXPECTED = new HashMap<>(2);
        EXPECTED.put("com.test.virus", new AppThreatSignature(42, "com.test.virus"));
        EXPECTED.put("org.virus.eicar", new AppThreatSignature(66, "org.virus.eicar"));
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
        Map<String, AppThreatSignature> signatures = mStorage.getAppSignatures();

        assertNotNull(signatures);
        assertTrue(signatures.isEmpty());
    }

    @Test
    public void whenSignaturesSavedThenTheyAreLoaded() throws Exception {
        mStorage.updateAppSignatures(EXPECTED);

        Map<String, AppThreatSignature> signatures = mStorage.getAppSignatures();

        assertNotNull(signatures);
        assertEquals(EXPECTED.size(), signatures.size());
        assertTrue(signatures.values().containsAll(EXPECTED.values()));
    }

    @Test
    public void whenSignaturesAreRewrittenThenCorrectAreLoaded() throws Exception {
        mStorage.updateAppSignatures(EXPECTED);

        Map<String, AppThreatSignature> newSignatures = new HashMap<>(1);
        newSignatures.put("someOtherSignature", new AppThreatSignature(77, "someOtherSignature"));
        mStorage.updateAppSignatures(newSignatures);

        Map<String, AppThreatSignature> updated = mStorage.getAppSignatures();

        assertNotNull(updated);
        assertEquals(updated.size(), newSignatures.size());
        assertTrue(updated.values().containsAll(newSignatures.values()));
    }
}
