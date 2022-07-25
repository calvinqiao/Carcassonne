package edu.cmu.cs.cs214.hw4.core;


import edu.cmu.cs.cs214.hw4.core.Segments.Segment;



/**
 * Class that represents a player
 */
public class Player {
    private int score = 0;
    private static final int TOTAL_NUM_MEEPLES = 7;
    private static final int ROTATION_UNIT = 90;
    private int numMeeplesLeft = TOTAL_NUM_MEEPLES;
    private Board board;
    private String name;
    private boolean wantToPlaceMeeples;

    /**
     * Construct a player
     * @param incomingBoard game board
     * @param playerName name of the player
     */
    public Player(Board incomingBoard, String playerName) {
        board = incomingBoard;
        name = playerName;
        wantToPlaceMeeples = false;
    }

    /**
     * Player places a tile
     * @param newTile new tile
     * @param rotation tile rotation (degrees)
     * @return true if the tile is successfully placed on the board
     *         false if not
     */
    public boolean placeTile(Tile newTile, int rotation) {
        int rotationTimes = rotation / ROTATION_UNIT;
        boolean placeTileResult = board.placeTile(newTile, rotationTimes);
        return placeTileResult;
    }

    /**
     * Player places meeples
     * @param segment segment to put meeples
     * @param meepleNumbers how many meeples to place
     */
    public void placeMeeples(Segment segment, int meepleNumbers) {
        if (meepleNumbers > numMeeplesLeft) {
            throw new IllegalArgumentException("Illegal number of meeples to put");
        }
        board.placeMeeples(this, meepleNumbers, segment);
        numMeeplesLeft = numMeeplesLeft - meepleNumbers;
    }

    /**
     * Get player's score
     * @return the player score
     */
    public int getScore() { return score; }

    /**
     * Get the name of the player
     * @return player name
     */
    public String getName() { return name; }

    /**
     * Get the if the player wants to place meeples or not
     * @return true if player wants to place meeples
     *         false if not
     */
    public boolean getWantToPlaceMeeples() { return wantToPlaceMeeples; }

    /**
     * Update if the player wants to place meeples
     * @param placeOrNot place meeples or not
     */
    public void setWantToPlaceMeeples(boolean placeOrNot) {
        wantToPlaceMeeples = placeOrNot;
    }

    /**
     * GEt how many meeples left for the player
     * @return number of meeples left for the player to place
     */
    public int getNumMeeplesLeft() {
        return numMeeplesLeft;
    }

    /**
     * Get back meeples
     * @param returnedMeeples number of meeples that are returned to the player from the feature
     *                        onece a feature is complete
     */
    public void setNumMeeplesLeft(int returnedMeeples) {
        numMeeplesLeft += returnedMeeples;
    }

    /**
     * Update player's score
     * @param newScores new score for the player to add
     */
    public void addScore(int newScores) {
        score += newScores;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Player) {
            Player player = (Player) object;

            if (name.equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
