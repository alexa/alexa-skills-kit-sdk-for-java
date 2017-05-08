package plantripdialog;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.Map;

public class IntentHandler implements IIntentHandler {

    private Map<String,IIntentHandler> handlerFns;
    private IIntentHandler defaultHandlerFn;

    public static Builder builder() {
        return new Builder();
    }

    private IntentHandler(Builder builder) {
        this.handlerFns = builder.handlerFns;
        this.defaultHandlerFn = builder.defaultHandlerFn;
    }

    @Override
    public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> request) throws SpeechletRequestHandlerException {
        final IIntentHandler handler = this.handlerFns.getOrDefault(request.getRequest().getIntent().getName(), this.defaultHandlerFn);
        if(handler == null) {
            throw new SpeechletRequestHandlerException("Unhandled Intent");
        }
        return handler.handle(request);
    }

    public static class Builder {
        private Map<String,IIntentHandler> handlerFns;
        private IIntentHandler defaultHandlerFn;

        private Builder() {
            handlerFns = new HashMap<>();
        }

        public Builder with(String intentName, IIntentHandler handlerFn) {
            if(Strings.isNullOrEmpty(intentName) || handlerFn == null) return this;
            this.handlerFns.put(intentName, handlerFn);
            return this;
        }

        public Builder withDefault(IIntentHandler defaultHandlerFn) {
            this.defaultHandlerFn = defaultHandlerFn;
            return this;
        }

        public IntentHandler build() {
            return new IntentHandler(this);
        }
    }
}
