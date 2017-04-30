package plantripdialog;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequestHandlerException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.interfaces.dialog.ConfirmationStatus;
import com.amazon.speech.speechlet.interfaces.dialog.DialogState;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Created by dionh on 4/30/17.
 */
public class PlanMyTripNoDelegateIntentHandler implements IIntentHandler {

    private Logger logger = LoggerFactory.getLogger(PlanMyTripNoDelegateIntentHandler.class);

    private final AmazonDynamoDB dynamoDB;

    @Inject
    public PlanMyTripNoDelegateIntentHandler(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    @Override
    public SpeechletResponse handle(SpeechletRequestEnvelope<IntentRequest> request) throws SpeechletRequestHandlerException {

        Intent updatedIntent = request.getRequest().getIntent();

        SpeechletResponse response = null;

        DialogState dialogState = request.getRequest().getDialogState() == null ? DialogState.STARTED : request.getRequest().getDialogState();
        logger.info("dialogState={}", dialogState);
        switch (dialogState) {
            default:
            case STARTED:
                // Set defaults for slots (if any) and update intent
                updatedIntent.getSlot("fromCity").setValue("Seattle");
                // Fall through

            case IN_PROGRESS:
                Slot slot = updatedIntent.getSlot("toCity");

                if (slot == null || Strings.isNullOrEmpty(slot.getValue()) || slot.getConfirmationStatus() == ConfirmationStatus.DENIED) {
                    logger.info("Dialog.ElicitSlot toCity");
                    response =  SpeechletResponse.newElicitSlotResponse("toCity", updatedIntent,
                            PlainTextOutputSpeech.builder().withText("Where do you want to go?").build(),
                            Reprompt.builder().withPlainText("What is your destination?").build());
                    break;
                } else if(slot.getConfirmationStatus() == ConfirmationStatus.NONE) {
                    response = SpeechletResponse.newConfirmSlotResponse("toCity", updatedIntent,
                            PlainTextOutputSpeech.builder().withText(String.format("You want to go to %s. Is that right?", slot.getValue())).build(),
                            null);
                    break;
                }

                slot = updatedIntent.getSlot("travelDate");
                if (slot == null || Strings.isNullOrEmpty(slot.getValue())) {
                    logger.info("Dialog.ElicitSlot travelDate");
                    response =  SpeechletResponse.newElicitSlotResponse("travelDate", updatedIntent,
                            PlainTextOutputSpeech.builder().withText("When do you want to leave?").build(), null);
                    break;
                }

                if(updatedIntent.getConfirmationStatus() == ConfirmationStatus.NONE) {
                    logger.info("Dialog.ConfirmIntent");
                    response = SpeechletResponse.newConfirmIntentResponse(updatedIntent,
                            PlainTextOutputSpeech.builder().withText(String.format("I'm saving your trip %s to %s on %s. Is that Ok?",
                                    updatedIntent.getSlot("toCity").getValue(), updatedIntent.getSlot("fromCity").getValue(),
                                    updatedIntent.getSlot("travelDate").getValue())).build(),
                            null);
                    break;
                }
                // Fall through

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
