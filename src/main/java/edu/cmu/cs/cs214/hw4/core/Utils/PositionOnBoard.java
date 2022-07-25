package edu.cmu.cs.cs214.hw4.core.Utils;



/**
 * Position on the board
 */
public class PositionOnBoard {
    private int x; private int y;
    private static final int PRIME_NUMBER = 31;

    /**
     * Construct a position without initial values
     */
    public PositionOnBoard() {}

    /**
     * Construct a position with initial x and y values
     * @param incomingX x coordinate
     * @param incomingY y coordinate
     */
    public PositionOnBoard(int incomingX, int incomingY) {
        x = incomingX; y = incomingY;
    }

    /**
     * Get the x coordinate of the position
     * @return x coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y coordinate of the position
     * @return y coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * Set the x coordinate of the position
     * @param incomingX x coordinate to set
     */
    public void setX(int incomingX) {
        x = incomingX;
    }

    /**
     * Set the y coordinate of the position
     * @param incomingY y coordinate to set
     */
    public void setY(int incomingY) {
        y = incomingY;
    }

    /**
     * Check if another position on the board is adjacent to this position
     * @param anotherPosition  another position on the board
     * @return true if the incoming position is adjacent to this position (N, W, S, or E)
     *         false if not
     */
    public boolean isPositionAdjacent(PositionOnBoard anotherPosition) {
        int diffX = Math.abs(x-anotherPosition.getX());
        int diffY = Math.abs(y-anotherPosition.getY());

        // Check if this feature border is adjacent to the incoming segment
        if ((diffX+diffY) == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Override the toString method
     * @return string that represents the x and y coordinate
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("x = ");
        stringBuilder.append(x);
        stringBuilder.append(" ");
        stringBuilder.append(y);

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PositionOnBoard) {
            PositionOnBoard positionOnBoard = (PositionOnBoard) object;

            if (x == positionOnBoard.getX() && y == positionOnBoard.getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x * PRIME_NUMBER + y;
    }
}
