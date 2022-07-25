package edu.cmu.cs.cs214.hw4.core.Utils;


/**
 * Border with position on the board
 */
public class PositionedBorder {
    private static final int PRIME_NUMBER = 31;
    private static final int[] PRIME_NUMBERS = new int[] {2, 3, 5, 7, 11};
    private static final int PRIME_CONTROL_INTERVAL = 3;
    private static final int ANOTHER_PRIME_CONTROL_INTERVAL = 3;
    private PositionOnBoard positionOnBoard = new PositionOnBoard();
    private String borderDirection;
    private String segmentType;

    /**
     * Construct positioned border
     * @param incomingBorderDirection border direction
     * @param position border position on the board
     * @param incomingSegmentType border segment type
     */
    public PositionedBorder(
            String incomingBorderDirection,
            PositionOnBoard position,
            String incomingSegmentType) {
        positionOnBoard.setX(position.getX());
        positionOnBoard.setY(position.getY());
        borderDirection = incomingBorderDirection;
        segmentType = incomingSegmentType;
    }

    /**
     * Get border position on the board
     * @return border position on the board
     */
    public PositionOnBoard getPositionOnBoard() { return positionOnBoard; }

    /**
     * Get border direction
     * @return border direction
     */
    public String getBorderDirection() {
        return borderDirection;
    }

    /**
     * Get border segment type
     * @return border segment type
     */
    public String getSegmentType() {
        return segmentType;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PositionedBorder) {
            PositionedBorder positionedBorder = (PositionedBorder) object;

            if (borderDirection.equals(positionedBorder.borderDirection)) {
                if (segmentType.equals(positionedBorder.segmentType)) {
                    if (positionOnBoard.getX() == positionedBorder.getPositionOnBoard().getX()) {
                        if (positionOnBoard.getY() == positionedBorder.getPositionOnBoard().getY()) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 0;

        code += positionOnBoard.getX() * PRIME_NUMBER + positionOnBoard.getY();

        // Include hash code for segment type
        int ctr; ctr = 0;
        for (int i = 0; i < segmentType.length(); i++) {
            code += segmentType.charAt(i) * PRIME_NUMBERS[ctr%ANOTHER_PRIME_CONTROL_INTERVAL];
            ctr += 1;
        }

        // Include hash code for borders
        ctr = 0;
        code += borderDirection.charAt(0) * PRIME_NUMBERS[ctr%PRIME_CONTROL_INTERVAL];


        return code;
    }

}
