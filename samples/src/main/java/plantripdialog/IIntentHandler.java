package plantripdialog;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletResponse;

public interface IIntentHandler {
    SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> request) throws SpeechletRequestHandlerException;
}
