package com.amazon.speech.speechlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;
import java.util.*;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.interfaces.audioplayer.AudioPlayer;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFailedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackNearlyFinishedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStartedRequest;
import com.amazon.speech.speechlet.interfaces.audioplayer.request.PlaybackStoppedRequest;
import com.amazon.speech.speechlet.interfaces.core.*;
import com.amazon.speech.speechlet.interfaces.system.Error;
import com.amazon.speech.speechlet.interfaces.system.ErrorCause;
import com.amazon.speech.speechlet.interfaces.system.request.ExceptionEncounteredRequest;
import com.amazon.speech.speechlet.services.householdlist.HouseholdListEventListener;
import com.amazon.speech.speechlet.services.householdlist.HouseholdListEventListenerV2;
import com.amazon.speech.speechlet.services.householdlist.ListBody;
import com.amazon.speech.speechlet.services.householdlist.ListCreatedRequest;
import com.amazon.speech.speechlet.services.householdlist.ListItemBody;
import com.amazon.speech.speechlet.services.householdlist.ListItemsCreatedRequest;
import com.amazon.speech.speechlet.services.householdlist.ListItemsDeletedRequest;
import com.amazon.speech.speechlet.services.householdlist.ListItemsUpdatedRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;



@RunWith(PowerMockRunner.class)
@PrepareForTest({
        Error.class,
        ErrorCause.class
})
public class SpeechletRequestDispatcherTest {

    @Mock
    private TestSpeechlet speechlet;
    @Mock
    private Session session;
    @Mock
    private SpeechletResponse speechletResponse;

    private SpeechletRequestDispatcher speechletRequestDispatcher;
    private static final Locale LOCALE = Locale.forLanguageTag("en-US");

    @Before
    public void setup() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        speechlet = mock(TestSpeechlet.class);
        when(speechlet.onLaunch(any(LaunchRequest.class), any(Session.class))).thenReturn(
                speechletResponse);
        when(speechlet.onPlaybackStarted(any(SpeechletRequestEnvelope.class))).thenReturn(
                speechletResponse);
        speechletRequestDispatcher = new SpeechletRequestDispatcher(speechlet);
    }

    @Test
    public void launchRequest_happyPath_goesToSpeechlet() throws SpeechletException, IOException,
            SpeechletRequestHandlerException {
        LaunchRequest request =
                LaunchRequest.builder().withRequestId("requestId").withLocale(LOCALE).build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),
                        session);
        verify(speechlet, times(1)).onLaunch(any(LaunchRequest.class), any(Session.class));
        assertEquals(envelope.getResponse(), speechletResponse);
    }

    @Test
    public void audioPlayerPlaybackStartedRequest_happyPath_goesToPlaybackStartedHandler()
            throws SpeechletException, IOException, SpeechletRequestHandlerException {
        PlaybackStartedRequest request =
                PlaybackStartedRequest
                        .builder()
                        .withOffsetInMilliseconds(0)
                        .withToken("token")
                        .withRequestId("requestId")
                        .withLocale(LOCALE)
                        .withTimestamp(mock(Date.class))
                        .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),
                        session);
        verify(speechlet, times(1)).onPlaybackStarted(any(SpeechletRequestEnvelope.class));
        assertEquals(envelope.getResponse(), speechletResponse);
    }

    @Test
    public void skilleventsSkillEnabledRequest_happyPath_goesToSkillEnableHandler() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        SkillEnabledEventRequest request =
                SkillEnabledEventRequest
                    .builder()
                    .withRequestId("requestId")
                    .withLocale(LOCALE)
                    .withTimestamp(mock(Date.class))
                    .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),session);
        assertNull(envelope.getResponse());

    }

    @Test
    public void permissionAcceptedRequest_happyPath_goesToPermissionGrantedHandler() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        Speechlet speechlet = Mockito.mock(Speechlet.class, withSettings().extraInterfaces(SkillEventListener.class));
        SpeechletRequestDispatcher speechletRequestDispatcher = new SpeechletRequestDispatcher(speechlet);

        Permission acceptedPermission = Permission.builder().withScope("scope").build();
        List<Permission> acceptedPermissionList = new ArrayList<>();
        acceptedPermissionList.add(acceptedPermission);
        PermissionBody body = PermissionBody.builder().withAcceptPermissions(acceptedPermissionList).build();
        PermissionAcceptedRequest request =
                PermissionAcceptedRequest
                    .builder()
                    .withRequestId("requestId")
                    .withLocale(LOCALE)
                    .withTimestamp(mock(Date.class))
                    .withPermissionBody(body)
                    .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),session);

        verify((SkillEventListener)speechlet, times(1)).onPermissionAccepted(any(SpeechletRequestEnvelope.class));
        assertNull(envelope.getResponse());
    }

    @Test
    public void accountLinkedRequest_happyPath_goesToAccountLinkedHandler() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        Speechlet speechlet = Mockito.mock(Speechlet.class, withSettings().extraInterfaces(SkillEventListener.class));
        SpeechletRequestDispatcher speechletRequestDispatcher = new SpeechletRequestDispatcher(speechlet);

        AccountLinkedBody body = AccountLinkedBody.builder().withAccessToken("accessToken").build();
        AccountLinkedRequest request=
                AccountLinkedRequest
                    .builder()
                    .withRequestId("requestId")
                    .withLocale(LOCALE)
                    .withTimestamp(mock(Date.class))
                    .withAccountLinkedBody(body)
                    .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),session);

        verify((SkillEventListener)speechlet, times(1)).onAccountLinked(any(SpeechletRequestEnvelope.class));
        assertNull(envelope.getResponse());
    }

    @Test
    public void listItemsCreatedRequest_happyPath_goesToListItemsCreatedHandler() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        Speechlet speechlet = Mockito.mock(Speechlet.class, withSettings().extraInterfaces(HouseholdListEventListener.class));
        SpeechletRequestDispatcher speechletRequestDispatcher = new SpeechletRequestDispatcher(speechlet);

        List<String> listItems = new ArrayList<>();
        listItems.add("item1");
        listItems.add("item2");
        ListItemBody body = ListItemBody.builder().withListId("listId").withListItemIds(listItems).build();
        ListItemsCreatedRequest request =
                ListItemsCreatedRequest
                    .builder()
                    .withRequestId("requestId")
                    .withLocale(LOCALE)
                    .withTimestamp(mock(Date.class))
                    .withListItemBody(body)
                    .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),session);

        verify((HouseholdListEventListener)speechlet, times(1)).onListItemsCreated(any(SpeechletRequestEnvelope.class));
        assertNull(envelope.getResponse());
    }

    @Test
    public void listCreatedRequest_happyPath_goesToListItemsCreatedHandler() throws SpeechletException, IOException, SpeechletRequestHandlerException {
        Speechlet speechlet = Mockito.mock(Speechlet.class, withSettings().extraInterfaces(HouseholdListEventListenerV2.class));
        SpeechletRequestDispatcher speechletRequestDispatcher = new SpeechletRequestDispatcher(speechlet);

        ListBody body = ListBody.builder().withListId("listId").build();
        ListCreatedRequest request =
                ListCreatedRequest
                        .builder()
                        .withRequestId("requestId")
                        .withLocale(LOCALE)
                        .withTimestamp(mock(Date.class))
                        .withListBody(body)
                        .build();
        SpeechletResponseEnvelope envelope =
                speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),session);

        verify((HouseholdListEventListenerV2)speechlet, times(1)).onListCreated(any(SpeechletRequestEnvelope.class));
        assertNull(envelope.getResponse());
    }

    @Test
    public void systemExceptionEncountered_noInterface_getsSwallowed() throws SpeechletException,
            IOException, SpeechletRequestHandlerException {
        try {
            ExceptionEncounteredRequest request =
                    ExceptionEncounteredRequest
                            .builder()
                            .withRequestId("requestId")
                            .withError(mock(Error.class))
                            .withCause(mock(ErrorCause.class))
                            .withLocale(LOCALE)
                            .withTimestamp(mock(Date.class))
                            .build();
            SpeechletResponseEnvelope envelope =
                    speechletRequestDispatcher.dispatchSpeechletCall(getRequestEnvelope(request),
                            session);

            assertNull(envelope.getResponse());
        } catch (Exception ex) {
            assert (false);
        }
    }

    /**
     * Test Speechlet implementation to trigger "shouldEndSession" or throw an exception.
     */
    private class TestSpeechlet implements Speechlet, AudioPlayer, SkillEventListener, HouseholdListEventListener {
        @Override
        public void onSessionStarted(final SessionStartedRequest request, final Session session)
                throws SpeechletException {
        }

        @Override
        public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
                throws SpeechletException {
            return speechletResponse;
        }

        @Override
        public SpeechletResponse onIntent(final IntentRequest request, final Session session)
                throws SpeechletException {
            return speechletResponse;
        }

        @Override
        public void onSessionEnded(final SessionEndedRequest request, final Session session)
                throws SpeechletException {
        }

        @Override
        public SpeechletResponse onPlaybackFailed(
                SpeechletRequestEnvelope<PlaybackFailedRequest> request) {
            return speechletResponse;
        }

        @Override
        public SpeechletResponse onPlaybackFinished(
                SpeechletRequestEnvelope<PlaybackFinishedRequest> request) {
            return speechletResponse;
        }

        @Override
        public SpeechletResponse onPlaybackNearlyFinished(
                SpeechletRequestEnvelope<PlaybackNearlyFinishedRequest> request) {
            return speechletResponse;
        }

        @Override
        public SpeechletResponse onPlaybackStarted(
                SpeechletRequestEnvelope<PlaybackStartedRequest> request) {
            return speechletResponse;
        }

        @Override
        public SpeechletResponse onPlaybackStopped(
                SpeechletRequestEnvelope<PlaybackStoppedRequest> request) {
            return speechletResponse;
        }

        @Override
        public void onSkillEnabled(SpeechletRequestEnvelope<SkillEnabledEventRequest> request) {

        }

        @Override
        public void onSkillDisabled(SpeechletRequestEnvelope<SkillDisabledEventRequest> request) {

        }

        @Override
        public void onPermissionAccepted(SpeechletRequestEnvelope<PermissionAcceptedRequest> request) {

        }

        @Override
        public void onPermissionChanged(SpeechletRequestEnvelope<PermissionChangedRequest> request) {

        }

        @Override
        public void onAccountLinked(SpeechletRequestEnvelope<AccountLinkedRequest> request) {

        }

        @Override
        public void onListItemsCreated(SpeechletRequestEnvelope<ListItemsCreatedRequest> request) {

        }

        @Override
        public void onListItemsUpdated(SpeechletRequestEnvelope<ListItemsUpdatedRequest> request) {

        }

        @Override
        public void onListItemsDeleted(SpeechletRequestEnvelope<ListItemsDeletedRequest> request) {

        }

    }

    private SpeechletRequestEnvelope<?> getRequestEnvelope(SpeechletRequest request) {
        SpeechletRequestEnvelope envelope = mock(SpeechletRequestEnvelope.class);
        when(envelope.getRequest()).thenReturn(request);
        return envelope;
    }
}
