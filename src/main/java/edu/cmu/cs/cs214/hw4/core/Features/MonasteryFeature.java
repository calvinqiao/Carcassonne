package edu.cmu.cs.cs214.hw4.core.Features;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;



/**
 * Class that represents a monastery
 */
public class MonasteryFeature extends Feature{
    private static final int NUM_SURROUNDING_TILES = 8;
    private Board board;

    /**
     * Construct a monastery feature
     * @param incomingBoard game board
     */
    public MonasteryFeature(Board incomingBoard) {
        featureType = "Monastery";
        board = incomingBoard;
    }

    /**
     * Calculate how many surrounding tiles there are
     * @return number of tiles that surround the monastery tile
     */
    private int getNumberOfAdjacentTiles() {
        int adjacentTileNumber;
        if (allSegments.size() == 1) {
            PositionOnBoard monasteryPos = allSegments.get(0).getSegmentPositionOnBoard();
            adjacentTileNumber = board.getAdjacentTileNumber(monasteryPos);
            return adjacentTileNumber;
        }
        else {
            throw new IllegalArgumentException("Monastery feature should only have one monastery segment!");
        }
    }

    @Override
    public boolean isCompleted() {

        return (getNumberOfAdjacentTiles() == NUM_SURROUNDING_TILES);
    }

    @Override
    public int computeScore(boolean isGameOver) {
        if (!isGameOver) {
            if (isCompleted()) {
                return getNumberOfAdjacentTiles()+1;
            }
            else {
                return 0;
            }
        }
        else {
            return getNumberOfAdjacentTiles() + 1;
        }
    }
}
