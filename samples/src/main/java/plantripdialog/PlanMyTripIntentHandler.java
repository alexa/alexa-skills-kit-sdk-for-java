package plantripdialog;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.dialog.ConfirmationStatus;
import com.amazon.speech.speechlet.interfaces.dialog.DialogState;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dionh on 4/30/17.
 */
public class PlanMyTripIntentHandler implements IIntentHandler {

    private Logger logger = LoggerFactory.getLogger(PlanMyTripIntentHandler.class);

    private final AmazonDynamoDB dynamoDB;

    @Inject
    public PlanMyTripIntentHandler(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Override
    public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> request) throws SpeechletRequestHandlerException {
        Intent updatedIntent = request.getRequest().getIntent();

        SpeechletResponse response = null;

        DialogState dialogState = request.getRequest().getDialogState() == null ? DialogState.STARTED : request.getRequest().getDialogState();
        logger.info("dialogState={}", dialogState);
        switch (dialogState) {
            case STARTED:
                // Set defaults for slots (if any) and update intent
                updatedIntent.getSlot("fromCity").setValue("Seattle");
                // Fall through

            case IN_PROGRESS:
                response = SpeechletResponse.newDelegateResponse(updatedIntent);
                break;

            case COMPLETED:
                final PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();

                if(request.getRequest().getIntent().getConfirmationStatus() == ConfirmationStatus.CONFIRMED) {
                    // Do real work here like book the trip or update your database

                    outputSpeech.setText("All set.");
                } else {
                    outputSpeech.setText("Ok. I won't book it.");
                }
                response = SpeechletResponse.newTellResponse(outputSpeech);
                break;
        }

        return response;
    }
}
