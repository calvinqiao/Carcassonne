package edu.cmu.cs.cs214.hw4.core.Features;



/**
 * Class that represents road feature
 */
public class RoadFeature extends Feature{

    /**
     * Construct a road feature
     */
    public RoadFeature() {
        featureType = "Road";
    }

    @Override
    public boolean isCompleted() {
        if (featureBorders.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int computeScore(boolean isGameOver) {
        if (!isGameOver) {
            if (isCompleted()) {
                return getTotalTileNumber();
            }
            else {
                return 0;
            }
        }
        else {
            return getTotalTileNumber();
        }
    }
}
