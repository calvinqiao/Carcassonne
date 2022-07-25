package edu.cmu.cs.cs214.hw4.core.Segments;

import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * Abstract class that represents a segment
 */
public abstract class Segment {
    private ArrayList<String> borders = new ArrayList<>();
    private String segmentType;
    private PositionOnBoard segmentPositionOnBoard = new PositionOnBoard();
    private Map<Player, Integer> playerMeeplesOnSegment = new HashMap<>();
    private static final int PRIME_CONTROL_INTERVAL = 3;
    private static final int ANOTHER_PRIME_CONTROL_INTERVAL = 5;
    private static final int PRIME_NUMBER = 31;
    private static final int[] PRIME_NUMBERS = new int[] {2, 3, 5, 7, 11};

    /**
     * Construct a segment class
     * @param incomingSegmentType segment type
     */
    public Segment(String incomingSegmentType) {
        segmentType = incomingSegmentType;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Segment) {
            Segment incomingSegment = (Segment) object;

            // Check segment position
            if (incomingSegment.getSegmentPositionOnBoard().equals(segmentPositionOnBoard)) {
                // Check segment type
                if (incomingSegment.getSegmentType().equals(segmentType)) {
                    // Check segment border direction type
                    for (int i = 0; i < borders.size(); i++) {
                        if (!incomingSegment.getBorders().get(i).equals(borders.get(i))) {
                            return false;
                        }
                    }
                    return true;
                }

            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 0;

        // Include hash code for segment position
        code += segmentPositionOnBoard.getX() * PRIME_NUMBER + segmentPositionOnBoard.getY();

        // Include hash code for segment type
        int ctr; ctr = 0;
        for (int i = 0; i < segmentType.length(); i++) {
            code += segmentType.charAt(i) * PRIME_NUMBERS[ctr%ANOTHER_PRIME_CONTROL_INTERVAL];
            ctr += 1;
        }

        // Include hash code for borders
        ctr = 0;
        for (String border : borders) {
            code += border.charAt(0) * PRIME_NUMBERS[ctr%PRIME_CONTROL_INTERVAL];
            ctr += 1;
        }
        return code;
    }

    /**
     * Set the segment type
     * @param incomingSegmentType segment type
     */
    public void setSegmentType(String incomingSegmentType) {
        segmentType = incomingSegmentType;
    }

    /**
     * Set the position of the segment on the board
     * @param incomingPosition incoming position
     */
    public void setSegmentPositionOnBoard(PositionOnBoard incomingPosition) {
        segmentPositionOnBoard = incomingPosition;
    }

    /**
     * Set the borders of the segment
     * @param incomingBorders segment borders
     */
    public void setBorders(ArrayList<String> incomingBorders) {
        for (int i = 0; i < incomingBorders.size(); i++) {
            borders.add(incomingBorders.get(i));
        }
    }

    /**
     * Get the borders of the segment
     * @return segment borders
     */
    public ArrayList<String> getBorders() {
        return borders;
    }

    /**
     * Get the segment type
     * @return segment type
     */
    public String getSegmentType() {
        return segmentType;
    }

    /**
     * Get the segment position on the board
     * @return segment position on the board
     */
    public PositionOnBoard getSegmentPositionOnBoard() { return segmentPositionOnBoard; }

    /**
     * Get meeple distribution on this segment
     * @return meeple-and-owner hash map on this segment
     */
    public Map<Player, Integer> getMeepleDistribution() {
        return playerMeeplesOnSegment;
    }

    /**
     * Set player's meeples on the segment
     * @param player player
     * @param meepleNumber number of meeples to put on this segment for this player
     */
    public void setMeepleDistribution(Player player, int meepleNumber) {
        playerMeeplesOnSegment.put(player, meepleNumber);
    }

    /**
     * Clean up meeples on the segment
     */
    public void cleanMeeples() {
        playerMeeplesOnSegment.clear();
    }
}
