package edu.cmu.cs.cs214.hw4.core.Features;

import edu.cmu.cs.cs214.hw4.core.Segments.CitySegment;
import edu.cmu.cs.cs214.hw4.core.Segments.Segment;


/**
 * City feature class
 */
public class CityFeature extends Feature{
    private static final int COMPLETE_SCORE = 2;
    private static final int INCOMPLETE_SCORE = 1;
    private static final int ARM_SCORE = 2;
    /**
     * Construct a city feature class
     */
    public CityFeature() {
        featureType = "City";
    }

    /**
     * Get number of coat of arms inside the feature
     * @return number of coat of arms
     */
    public int getNumberOfCoatOfArms() {
        int coatOfArms = 0;
        for (int i = 0; i < allSegments.size(); i++) {
            Segment thisSegment = allSegments.get(i);
            if (thisSegment.getSegmentType().equals("citySegment")) {
                CitySegment citySegment = (CitySegment) thisSegment;
                if (citySegment.getArms()) {
                    coatOfArms += 1;
                }
            }
        }
        return coatOfArms;
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
        int unitScore;
        if (isCompleted()) {
            unitScore = COMPLETE_SCORE;
        }
        else {
            unitScore = INCOMPLETE_SCORE;
        }

        int totalScore = unitScore * getTotalTileNumber();

        if (!isGameOver) {  // In game
            if (isCompleted()) {
                return totalScore + ARM_SCORE * getNumberOfCoatOfArms();
            }
            else {
                return 0;
            }
        }
        else { // Game ends
            return totalScore + ARM_SCORE * getNumberOfCoatOfArms();
        }
    }
}
