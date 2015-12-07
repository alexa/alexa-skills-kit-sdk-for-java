package scorekeeper.storage;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.amazon.speech.speechlet.Session;

/**
 * Represents a score keeper game.
 */
public final class ScoreKeeperGame {
    private Session session;
    private ScoreKeeperGameData gameData;

    private ScoreKeeperGame() {
    }

    /**
     * Creates a new instance of {@link ScoreKeeperGame} with the provided {@link Session} and
     * {@link ScoreKeeperGameData}.
     * <p>
     * To create a new instance of {@link ScoreKeeperGameData}, see
     * {@link ScoreKeeperGameData#newInstance()}
     * 
     * @param session
     * @param gameData
     * @return
     * @see ScoreKeeperGameData#newInstance()
     */
    public static ScoreKeeperGame newInstance(Session session, ScoreKeeperGameData gameData) {
        ScoreKeeperGame game = new ScoreKeeperGame();
        game.setSession(session);
        game.setGameData(gameData);
        return game;
    }

    protected void setSession(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        return session;
    }

    protected ScoreKeeperGameData getGameData() {
        return gameData;
    }

    protected void setGameData(ScoreKeeperGameData gameData) {
        this.gameData = gameData;
    }

    /**
     * Returns true if the game has any players, false otherwise.
     * 
     * @return true if the game has any players, false otherwise
     */
    public boolean hasPlayers() {
        return !gameData.getPlayers().isEmpty();
    }

    /**
     * Returns the number of players in the game.
     * 
     * @return the number of players in the game
     */
    public int getNumberOfPlayers() {
        return gameData.getPlayers().size();
    }

    /**
     * Add a player to the game.
     * 
     * @param playerName
     *            Name of the player
     */
    public void addPlayer(String playerName) {
        gameData.getPlayers().add(playerName);
    }

    /**
     * Returns true if the player exists in the game, false otherwise.
     * 
     * @param playerName
     *            Name of the player
     * @return true if the player exists in the game, false otherwise
     */
    public boolean hasPlayer(String playerName) {
        return gameData.getPlayers().contains(playerName);
    }

    /**
     * Returns true if the game has any scores listed, false otherwise.
     * 
     * @return true if the game has any scores listed, false otherwise
     */
    public boolean hasScores() {
        return !gameData.getScores().isEmpty();
    }

    /**
     * Returns the score for a player.
     * 
     * @param playerName
     *            Name of the player
     * @return score for a player
     */
    public long getScoreForPlayer(String playerName) {
        return gameData.getScores().get(playerName);
    }

    /**
     * Adds the score passed to it to the current score for a player. Returns true if the player
     * existed, false otherwise.
     * 
     * @param playerName
     *            Name of the player
     * @param score
     *            score to be added
     * @return true if the player existed, false otherwise.
     */
    public boolean addScoreForPlayer(String playerName, long score) {
        if (!hasPlayer(playerName)) {
            return false;
        }

        long currentScore = 0L;
        if (gameData.getScores().containsKey(playerName)) {
            currentScore = gameData.getScores().get(playerName);
        }

        gameData.getScores().put(playerName, Long.valueOf(currentScore + score));
        return true;
    }

    /**
     * Resets the scores for all players to zero.
     */
    public void resetScores() {
        for (String playerName : gameData.getPlayers()) {
            gameData.getScores().put(playerName, Long.valueOf(0L));
        }
    }

    /**
     * Returns a {@link SortedMap} of player names mapped to scores with the map sorted in
     * decreasing order of scores.
     * 
     * @return a {@link SortedMap} of player names mapped to scores with the map sorted in
     *         decreasing order of scores
     */
    public SortedMap<String, Long> getAllScoresInDescndingOrder() {
		Map<String, Long> scores = gameData.getScores();

		for (String playerName : gameData.getPlayers()) {
			if (!gameData.getScores().containsKey(playerName)) {
				scores.put(playerName, Long.valueOf(0L));
			}
		}

        SortedMap<String, Long> sortedScores =
                new TreeMap<String, Long>(new ScoreComparator(scores));
        sortedScores.putAll(gameData.getScores());
        return sortedScores;
    }

    /**
     * This comparator takes a map of player name and scores and uses that to sort a collection of
     * player names in the descending order of their scores.
     * <p>
     * Note: this comparator imposes orderings that are inconsistent with equals.
     */
    private static final class ScoreComparator implements Comparator<String>, Serializable {
        private static final long serialVersionUID = 7849926209990327190L;
        private final Map<String, Long> baseMap;

        private ScoreComparator(Map<String, Long> baseMap) {
            this.baseMap = baseMap;
        }

        @Override
        public int compare(String a, String b) {
            int longCompare = Long.compare(baseMap.get(b), baseMap.get(a));
            return longCompare != 0 ? longCompare : a.compareTo(b);
        }
    }
}
