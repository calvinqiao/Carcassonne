package edu.cmu.cs.cs214.hw4.core.Features;

import edu.cmu.cs.cs214.hw4.core.Player;
import edu.cmu.cs.cs214.hw4.core.Segments.Segment;
import edu.cmu.cs.cs214.hw4.core.Tile;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionedBorder;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;



/**
 * An abstract class that represents feature
 */
public abstract class Feature {
    protected HashSet<PositionOnBoard> tilePositionSet = new HashSet<>();
    protected List<Segment> allSegments = new ArrayList<>();
    protected List<PositionedBorder> featureBorders = new ArrayList<>();
    protected String featureType;
    protected Map<Player, Integer> featureMeeples = new HashMap<>();

    /**
     * Update the segments of the feature
     * Precondition is that the incoming segment is adjacent and compatible with the feature
     * @param newSegment an incoming adjacent and compatible segment piece
     */
    public void updateFeatureSegments(Segment newSegment) {
        allSegments.add(newSegment);
    }

    /**
     * Update the borders of the feature
     * Precondition is that the incoming segment is adjacent and compatible with the feature
     * 1. adjacent location 2. same segment type 3. valid border direction type
     * @param newSegment an incoming adjacent and compatible segment piece
     */
    public void updateFeatureBorders(Segment newSegment) {
        ArrayList<String> newBorders = newSegment.getBorders();
        ArrayList<PositionedBorder> newFeatureBorders = new ArrayList<>();

        // Transform into an array of feature borders
        for (int i = 0; i < newBorders.size(); i++) {
            PositionedBorder newFeatureBorder
                    = new PositionedBorder(newBorders.get(i),
                                           newSegment.getSegmentPositionOnBoard(),
                                           newSegment.getSegmentType());
            newFeatureBorders.add(newFeatureBorder);
        }

        // Add borders of the segment
        for (int i = 0; i < newFeatureBorders.size(); i++) {
            if (!featureBorders.contains(newFeatureBorders.get(i))) {
                featureBorders.add(newFeatureBorders.get(i));
            }
        }
        // Last step
        removeOverlappingBorders();
    }

    /**
     * Remove overlapping borders within the feature
     */
    public void removeOverlappingBorders() {
        // Remove overlapping borders
        ArrayList<PositionedBorder> removeList = new ArrayList<>();
        for (int i = 0; i < featureBorders.size(); i++) {
            for ( int j = i+1; j < featureBorders.size(); j++) {
                if (featureBordersOverlap(featureBorders.get(i), featureBorders.get(j))) {
                    if (!removeList.contains(featureBorders.get(i))) {
                        removeList.add(featureBorders.get(i));
                    }
                    if (!removeList.contains(featureBorders.get(j))) {
                        removeList.add(featureBorders.get(j));
                    }
                }
            }
        }
        if (removeList.size() > 0) {
            for (int i = 0; i < removeList.size(); i++) {
                featureBorders.remove(removeList.get(i));
            }
        }

    }

    /**
     * Check if two borders inside a feature overlap at the same location
     * @param featureBorder1 border 1 from the feature
     * @param featureBorder2 another border from the feature
     * @return true if these two borders overlap
     *         false if they do not overlap
     */
    public boolean featureBordersOverlap(PositionedBorder featureBorder1, PositionedBorder featureBorder2) {
        boolean answer = false;

        // If these two borders are adjacent, we look further into their direction types
        if (featureBorder1.getPositionOnBoard().isPositionAdjacent(featureBorder2.getPositionOnBoard())) {
            switch (featureBorder1.getBorderDirection()) {
                case "N":
                    if (featureBorder2.getBorderDirection().equals("S")) {
                        answer = true;
                    }
                    break;
                case "S":
                    if (featureBorder2.getBorderDirection().equals("N")) {
                        answer = true;
                    }
                    break;
                case "W":
                    if (featureBorder2.getBorderDirection().equals("E")) {
                        answer = true;
                    }
                    break;
                case "E":
                    if (featureBorder2.getBorderDirection().equals("W")) {
                        answer = true;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("This is not a valid border direction!");
            }
        }
        return answer;
    }

    /**
     * Update tile positions that are stored inside the feature
     * @param newTile incoming new tile
     */
    public void updateTilePositionSet(Tile newTile) {
        tilePositionSet.add(newTile.getTilePosition());
    }

    /**
     * Merge tile positions
     * @param incomingFeature another feature
     */
    public void mergeFeatureTilePositionSet(Feature incomingFeature) {
        tilePositionSet.addAll(incomingFeature.getTilePositionSet());
    }

    /**
     * Merge feature segments
     * @param incomingFeature another feature
     */
    public void mergeFeatureSegments(Feature incomingFeature) {
        for (int i = 0; i < incomingFeature.allSegments.size(); i++) {
            if (!allSegments.contains(incomingFeature.allSegments.get(i))) {
                allSegments.add(incomingFeature.allSegments.get(i));
            }
        }
    }

    /**
     * Merge feature borders
     * @param incomingFeature another feature
     */
    public void mergeFeatureBorders(Feature incomingFeature) {

        for (int i = 0; i < incomingFeature.featureBorders.size(); i++) {
            if (!featureBorders.contains(incomingFeature.featureBorders.get(i))) {
                if (!tilePositionSet.contains(incomingFeature.featureBorders.get(i).getPositionOnBoard()))
                {
                    featureBorders.add(incomingFeature.featureBorders.get(i));
                }
            }
        }
        // Also need to remove overlapping borders here
        removeOverlappingBorders();
    }

    /**
     * Merge with another feature
     * @param incomingFeature incoming feature
     */
    public void mergeFeature(Feature incomingFeature) {
        // Merge feature's tile position set
        mergeFeatureTilePositionSet(incomingFeature);

        // Merge feature's segments
        mergeFeatureSegments(incomingFeature);

        // Merge feature's open borders
        mergeFeatureBorders(incomingFeature);
    }

    /**
     * Build feature meeple distribution from segments
     */
    public void collectFeatureSegmentMeeples() {
        featureMeeples.clear();
        for (Segment segment : allSegments) {
            for (Map.Entry<Player, Integer> entry : segment.getMeepleDistribution().entrySet())
            {
                Player player = entry.getKey();
                Integer numMeeples = entry.getValue();
                featureMeeples.put(player, numMeeples);
            }
        }
    }

    /**
     * Update player scores
     * @param isGameOver is game over
     */
    public void updatePlayerScore(boolean isGameOver) {
        int newScore = computeScore(isGameOver);

        // Update player's score accordingly
        // Get player - meeples distribution
        collectFeatureSegmentMeeples();
        // Determine who is the owner or there is a tier
        int maxNumMeeples = -1;
        for (Map.Entry<Player, Integer> entry : featureMeeples.entrySet()) {
            if (entry.getValue() > maxNumMeeples) {
                maxNumMeeples = entry.getValue();
            }
        }

        // If there are meeples on the feature
        if (maxNumMeeples != -1) {
            List<Player> owners = new ArrayList();

            for (Map.Entry<Player, Integer> entry : featureMeeples.entrySet()) {
                if (entry.getValue() == maxNumMeeples) {
                    owners.add(entry.getKey());
                }

            }
            // Update the owner's scores
            for (Player player : owners) {
                player.addScore(newScore);
                int returnedMeeples = featureMeeples.get(player);
                player.setNumMeeplesLeft(returnedMeeples);  // Give back meeples to player
            }

            // Clean meeples on the feature if this feature is completed
            if (isCompleted()) {
                cleanMeeples();
            }
        }
    }

    /**
     * Clean up meeples on the feature
     */
    public void cleanMeeples() {
        for (Segment segment : allSegments) {
            segment.cleanMeeples();
        }
    }

    /**
     * Get the tile position set
     * @return tile position set
     */
    public HashSet<PositionOnBoard> getTilePositionSet() { return tilePositionSet; }

    /**
     * Get feature segments
     * @return feature segments
     */
    public List<Segment> getAllSegments() { return allSegments; }

    /**
     * Get feature open borders
     * @return feature open borders
     */
    public List<PositionedBorder> getFeatureBorders() { return featureBorders; }

    /**
     * Get the number of tiles within the feature
     * @return the number of tiles within the feature
     */
    public int getTotalTileNumber() {
        return tilePositionSet.size();
    }

    /**
     * Get feature meeples
     * @return feature meeples
     */
    public Map<Player, Integer> getFeatureMeeples() { return featureMeeples; }

    /**
     * Get type of the feature
     * @return type of the feature
     */
    public String getFeatureType() {
        return featureType;
    }

    /**
     * Check if a feature is completed
     * @return true if the feature is completed
     *         false if not
     */
    abstract public boolean isCompleted();

    /**
     * Compute the score for this feature
     * @param isGameOver is game over
     * @return score
     */
    abstract public int computeScore(boolean isGameOver);
}
