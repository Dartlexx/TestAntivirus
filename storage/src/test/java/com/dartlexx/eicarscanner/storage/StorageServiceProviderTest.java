package com.dartlexx.eicarscanner.storage;

import android.content.Context;

import com.dartlexx.eicarscanner.common.storage.FoundAppThreatStorage;
import com.dartlexx.eicarscanner.common.storage.FoundFileThreatStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceProviderTest {

    private final Context mAppContext = mock(Context.class);
    private StorageServiceProvider mProvider;

    @Before
    public void setUp() throws Exception {
        mProvider = new StorageServiceProvider();
    }

    @Test
    public void whenNoThreatStoragePresentThenItIsCreated() throws Exception {
        FoundAppThreatStorage storage = mProvider.getAppThreatStorage(mAppContext);

        assertTrue(storage instanceof FoundThreatsStorageImpl);
    }

    @Test
    public void whenThreatStorageExistsThenItIsNotRecreated() throws Exception {
        FoundAppThreatStorage storage = mProvider.getAppThreatStorage(mAppContext);
        assertTrue(storage instanceof FoundThreatsStorageImpl);

        FoundFileThreatStorage fileThreatsStorage = mProvider.getFileThreatStorage(mAppContext);
        assertEquals(storage, fileThreatsStorage);

        verify(mAppContext).getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE));
    }

    @Test
    public void whenProviderIsResetThenStorageIsRecreated() throws Exception {
        FoundAppThreatStorage storage = mProvider.getAppThreatStorage(mAppContext);
        assertTrue(storage instanceof FoundThreatsStorageImpl);

        mProvider.resetProvider();

        FoundAppThreatStorage appThreatsStorage = mProvider.getAppThreatStorage(mAppContext);
        assertNotEquals(storage, appThreatsStorage);

        verify(mAppContext, times(2)).getSharedPreferences(anyString(), eq(Context.MODE_PRIVATE));
    }
}
