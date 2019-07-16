package com.dartlexx.eicarscanner.avcore;

import com.dartlexx.eicarscanner.common.models.AppThreatInfo;
import com.dartlexx.eicarscanner.common.models.AppThreatSignature;
import com.dartlexx.eicarscanner.common.repository.FoundAppThreatRepo;
import com.dartlexx.eicarscanner.common.ui.ThreatFoundUiListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ThreatProcessorImplTest {

    private static final List<AppThreatInfo> EXISTING_THREATS;

    static {
        EXISTING_THREATS = new ArrayList<>(2);
        EXISTING_THREATS.add(new AppThreatInfo(
                new AppThreatSignature(1, "test.virus"),
                "TestVirus",
                13));

        EXISTING_THREATS.add(new AppThreatInfo(
                new AppThreatSignature(2, "eicar.virus"),
                "EicarVirus",
                42));
    }

    private final FoundAppThreatRepo mRepo = mock(FoundAppThreatRepo.class);
    private final ThreatFoundUiListener mUiListener = mock(ThreatFoundUiListener.class);
    private final ThreatProcessorImpl mProcessor = new ThreatProcessorImpl(mRepo, mUiListener);

    @Captor
    private ArgumentCaptor<List<AppThreatInfo>> mCaptor;

    @Before
    public void setUp() throws Exception {
        doReturn(EXISTING_THREATS).when(mRepo).getAppThreats();
    }

    @Test
    public void whenKnownThreatIsFoundThenListenerIsNotInvoked() throws Exception {
        AppThreatSignature signature = new AppThreatSignature(2, "eicar.virus");
        AppThreatInfo threat = new AppThreatInfo(signature, "EicarVirus-2", 43);

        mProcessor.onAppThreatFound(threat);

        verify(mRepo).getAppThreats();
        verify(mRepo, never()).updateAppThreats(ArgumentMatchers.<AppThreatInfo>anyList());
        verifyZeroInteractions(mUiListener);
    }

    @Test
    public void whenNewThreatIsFoundThenListenerIsInvokedAndBaseUpdated() throws Exception {
        AppThreatSignature signature = new AppThreatSignature(77, "new.threat");
        AppThreatInfo threat = new AppThreatInfo(signature, "New threat", 15);

        mProcessor.onAppThreatFound(threat);

        verify(mRepo).getAppThreats();
        verify(mRepo).updateAppThreats(mCaptor.capture());
        verify(mUiListener).onAppThreatFound("New threat", "new.threat");

        List<AppThreatInfo> updated = mCaptor.getValue();
        assertEquals(3, updated.size());
        assertTrue(updated.containsAll(EXISTING_THREATS));
        assertTrue(updated.contains(threat));
}
}
