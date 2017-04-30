package plantripdialog;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * Created by dionh on 4/30/17.
 */
public class GuiceModule extends AbstractModule {
    protected void configure() {
        // Automatic dialog (using Dialog.Delegate directive)
        bind(IIntentHandler.class).annotatedWith(Names.named("PlanMyTripIntent")).to(PlanMyTripIntentHandler.class);
        // Switch handlers to try out manual dialog (not using Dialog.Delegate directive)
        // bind(IIntentHandler.class).annotatedWith(Names.named("PlanMyTripIntent")).to(PlanMyTripNoDelegateIntentHandler.class);
        bind(AmazonDynamoDB.class).to(AmazonDynamoDBClient.class);
    }
}
