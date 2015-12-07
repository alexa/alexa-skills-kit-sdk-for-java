package scorekeeper.storage;

import com.amazon.speech.speechlet.Session;

/**
 * Contains the methods to interact with the persistence layer for ScoreKeeper in DynamoDB.
 */
public class ScoreKeeperDao {
    private final ScoreKeeperDynamoDbClient dynamoDbClient;

    public ScoreKeeperDao(ScoreKeeperDynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    /**
     * Reads and returns the {@link ScoreKeeperGame} using user information from the session.
     * <p>
     * Returns null if the item could not be found in the database.
     * 
     * @param session
     * @return
     */
    public ScoreKeeperGame getScoreKeeperGame(Session session) {
        ScoreKeeperUserDataItem item = new ScoreKeeperUserDataItem();
        item.setCustomerId(session.getUser().getUserId());

        item = dynamoDbClient.loadItem(item);

        if (item == null) {
            return null;
        }

        return ScoreKeeperGame.newInstance(session, item.getGameData());
    }

    /**
     * Saves the {@link ScoreKeeperGame} into the database.
     * 
     * @param game
     */
    public void saveScoreKeeperGame(ScoreKeeperGame game) {
        ScoreKeeperUserDataItem item = new ScoreKeeperUserDataItem();
        item.setCustomerId(game.getSession().getUser().getUserId());
        item.setGameData(game.getGameData());

        dynamoDbClient.saveItem(item);
    }
}
