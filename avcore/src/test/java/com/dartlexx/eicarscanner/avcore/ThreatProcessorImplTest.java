package com.dartlexx.eicarscanner.avcore;

import com.dartlexx.eicarscanner.common.avcore.ThreatFoundListener;
import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ThreatProcessorImplTest {

    private static final Map<String, AppThreatInfo> EXISTING_THREATS;

    static {
        EXISTING_THREATS = new HashMap<>(2);
        EXISTING_THREATS.put("test.virus",
                new AppThreatInfo(new AppThreatSignature(1, "test.virus"),
                        "TestVirus",
                        13));

        EXISTING_THREATS.put("eicar.virus",
                new AppThreatInfo(new AppThreatSignature(2, "eicar.virus"),
                        "EicarVirus",
                        42));
    }

    private final FoundAppThreatRepo mRepo = mock(FoundAppThreatRepo.class);
    private final ThreatFoundListener mUiListener = mock(ThreatFoundListener.class);
    private final ThreatProcessor mProcessor = new ThreatProcessor(mRepo, mUiListener);

    @Captor
    private ArgumentCaptor<Map<String, AppThreatInfo>> mCaptor;

    @Before
    public void setUp() throws Exception {
        doReturn(EXISTING_THREATS).when(mRepo).getAppThreats();
    }

    @Test
    public void whenKnownThreatIsFoundThenListenerIsNotInvoked() throws Exception {
        AppThreatSignature signature = new AppThreatSignature(2, "eicar.virus");
        AppThreatInfo threat = new AppThreatInfo(signature, "EicarVirus-2", 43);

        mProcessor.onAppThreatFound(threat);

        verify(mUiListener).onAppThreatFound("EicarVirus-2", "eicar.virus");
        verify(mRepo).getAppThreats();
        verify(mRepo, never()).updateAppThreats(ArgumentMatchers.<String, AppThreatInfo>anyMap());
    }

    @Test
    public void whenNewThreatIsFoundThenListenerIsInvokedAndBaseUpdated() throws Exception {
        AppThreatSignature signature = new AppThreatSignature(77, "new.threat");
        AppThreatInfo threat = new AppThreatInfo(signature, "New threat", 15);

        mProcessor.onAppThreatFound(threat);

        verify(mUiListener).onAppThreatFound("New threat", "new.threat");
        verify(mRepo).getAppThreats();
        verify(mRepo).updateAppThreats(mCaptor.capture());

        Map<String, AppThreatInfo> updated = mCaptor.getValue();
        assertEquals(3, updated.size());
        assertTrue(updated.values().containsAll(EXISTING_THREATS.values()));
        assertTrue(updated.values().contains(threat));
    }
}
