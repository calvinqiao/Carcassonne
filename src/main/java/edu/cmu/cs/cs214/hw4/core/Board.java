package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.core.Features.CityFeature;
import edu.cmu.cs.cs214.hw4.core.Features.Feature;
import edu.cmu.cs.cs214.hw4.core.Features.MonasteryFeature;
import edu.cmu.cs.cs214.hw4.core.Features.RoadFeature;
import edu.cmu.cs.cs214.hw4.core.Segments.Segment;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionedBorder;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

/**
 * Class that represents a board
 */
public class Board {
    private static final PositionOnBoard NORTH_ADJ
            = new PositionOnBoard(0,  -1);
    private static final PositionOnBoard SOUTH_ADJ
            = new PositionOnBoard(0,  1);
    private static final PositionOnBoard WEST_ADJ
            = new PositionOnBoard(-1, 0);
    private static final PositionOnBoard EAST_ADJ
            = new PositionOnBoard(1,  0);
    private static final PositionOnBoard NORTH_EAST_ADJ
            = new PositionOnBoard(1,  -1);
    private static final PositionOnBoard NORTH_WEST_ADJ
            = new PositionOnBoard(-1, -1);
    private static final PositionOnBoard SOUTH_EAST_ADJ
            = new PositionOnBoard(1,  1);
    private static final PositionOnBoard SOUTH_WEST_ADJ
            = new PositionOnBoard(-1, 1);
    private static final List<PositionOnBoard> ADJ_LIST = new ArrayList<>();

    private HashMap<PositionOnBoard, Tile> board;
    private List<Feature> featureList;
    private Tile currentTile;

    /**
     * Construct a board class
     */
    public Board() {
        board = new HashMap<>();
        featureList = new ArrayList<>();
        ADJ_LIST.add(NORTH_ADJ);
        ADJ_LIST.add(SOUTH_ADJ);
        ADJ_LIST.add(WEST_ADJ);
        ADJ_LIST.add(EAST_ADJ);
        ADJ_LIST.add(NORTH_EAST_ADJ);
        ADJ_LIST.add(NORTH_WEST_ADJ);
        ADJ_LIST.add(SOUTH_EAST_ADJ);
        ADJ_LIST.add(SOUTH_WEST_ADJ);
    }

    /**
     * place meeples on segments
     * @param player player who owns the meeples
     * @param numMeeples how many meeples to put
     * @param segment which segment to put meeples
     */
    public void placeMeeples(Player player, int numMeeples, Segment segment) {
        segment.setMeepleDistribution(player, numMeeples);
    }

    /**
     * Place a tile on the board
     * @param incomingTile new tile
     * @param rotation tile rotation
     * @return true if the tile is successfully placed on the board
     *         false if not
     */
    public boolean placeTile(Tile incomingTile, int rotation) {
        incomingTile.rotateTile(rotation);
        currentTile = incomingTile;
        if (!checkPositionHasTile(incomingTile.getTilePosition())) {
            if (checkAdjacentTiles(incomingTile)) {
                // Place the tile on the board
                board.put(incomingTile.getTilePosition(), incomingTile);

                if (featureList.size() > 0) {
                    int currentFeatureListSize = featureList.size();
                    HashSet<Segment> usedSegments = new HashSet<>();  // Avoid using redundant segments

                    // Loop through each pair of feature and tile segment to update features
                    for (int j = 0; j < currentFeatureListSize; j++) {  // In test, there are two features
                        for (int i = 0; i < incomingTile.getTileSegments().size(); i++) {  // In test, there're two segments
                            Segment currentSegment = incomingTile.getTileSegments().get(i);
                            // If the tile segment is connected to a feature in the feature list
                            if (canSegmentAndFeatureBeConnected(currentSegment, featureList.get(j))) {
                                updateFeature(currentSegment, featureList.get(j));
                            }
                            // Otherwise, create new features
                            else {
                                  if (!usedSegments.contains(currentSegment)) {
                                      createNewFeature(currentSegment);
                                      usedSegments.add(currentSegment);
                                  }
                            }
                        }
                    }
                }
                // If there are no features yet in the feature list
                else {
                    for (int i = 0; i < incomingTile.getTileSegments().size(); i++) {
                        Segment currentSegment = incomingTile.getTileSegments().get(i);
                        createNewFeature(currentSegment);
                    }
                }

                // Update feature list in the last step
                updateBoardFeatureList();

                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    /**
     * Create a new feature using the corresponding segment
     * @param currentSegment segment for constructing a new feature
     */
    public void createNewFeature(Segment currentSegment) {
        switch (currentSegment.getSegmentType()) {
            case "citySegment":
                CityFeature cityFeature = new CityFeature();
                updateFeature(currentSegment, cityFeature);
                featureList.add(cityFeature);
                break;
            case "roadSegment":
                RoadFeature roadFeature = new RoadFeature();
                updateFeature(currentSegment, roadFeature);
                featureList.add(roadFeature);
                break;
            case "monasterySegment":
                MonasteryFeature monasteryFeature = new MonasteryFeature(this);
                updateFeature(currentSegment, monasteryFeature);
                featureList.add(monasteryFeature);
                break;
            case "fieldSegment":
                break;
            default:
                throw new IllegalArgumentException(
                        "This is not a valid segment type!");
        }

    }

    /**
     * Check if this position on board is already taken
     * @param positionOnBoard position on the board
     * @return true if this position on board is already taken
     *         false if not
     */
    public boolean checkPositionHasTile(PositionOnBoard positionOnBoard) {
        if (board.containsKey(positionOnBoard)) {
            return true;
        }
        return false;
    }

    /**
     * Check if the incoming tile is compatible with its surrounding tiles (N,W,S,E)
     * in terms of border direction, border segment type
     * @param incomingTile an incoming tile
     * @return true if the incoming tile is compatible with its surrounding tiles on the board
     *         false if not
     */
    public boolean checkAdjacentTiles(Tile incomingTile) {
        PositionOnBoard adjPosition;

        for (String border : incomingTile.getTileBorders().keySet()) {
            if (border.equals("N")) {
                adjPosition
                        = new PositionOnBoard(
                                incomingTile.getTilePosition().getX()+NORTH_ADJ.getX(),
                                incomingTile.getTilePosition().getY()+NORTH_ADJ.getY());
                if (board.containsKey(adjPosition)) {
                    Tile northAdjTile = board.get(adjPosition);
                    String borderType = northAdjTile.getTileBorders().get("S");
                    if (!incomingTile.getTileBorders().get("N").equals(borderType)) { return false; }
                }
            }
            else if (border.equals("S")) {
                adjPosition
                        = new PositionOnBoard(
                                incomingTile.getTilePosition().getX()+SOUTH_ADJ.getX(),
                                incomingTile.getTilePosition().getY()+SOUTH_ADJ.getY());
                if (board.containsKey(adjPosition)) {
                    Tile southAdjTile = board.get(adjPosition);
                    String borderType = southAdjTile.getTileBorders().get("N");
                    if (!incomingTile.getTileBorders().get("S").equals(borderType)) { return false; }
                }
            }
            else if (border.equals("E")) {
                adjPosition
                        = new PositionOnBoard(
                                incomingTile.getTilePosition().getX()+EAST_ADJ.getX(),
                                incomingTile.getTilePosition().getY()+EAST_ADJ.getY());
                if (board.containsKey(adjPosition)) {
                    Tile eastAdjTile = board.get(adjPosition);
                    String borderType = eastAdjTile.getTileBorders().get("W");
                    if (!incomingTile.getTileBorders().get("E").equals(borderType)) { return false; }
                }
            }
            else if (border.equals("W")) {
                adjPosition
                        = new PositionOnBoard(
                                incomingTile.getTilePosition().getX()+WEST_ADJ.getX(),
                                incomingTile.getTilePosition().getY()+WEST_ADJ.getY());
                if (board.containsKey(adjPosition)) {
                    Tile westAdjTile = board.get(adjPosition);
                    String borderType = westAdjTile.getTileBorders().get("E");
                    if (!incomingTile.getTileBorders().get("W").equals(borderType)) { return false; }
                }
            }
            else {
                throw new IllegalArgumentException("This is not a valid border direction type!");
            }
        }
        return true;
    }

    /**
     * Get how many surrounding tiles there are for this particular position on the board
     * @param position position on the board
     * @return number of surrounding tiles on the board
     */
    public int getAdjacentTileNumber(PositionOnBoard position) {
        PositionOnBoard testPosition; int testX; int testY;
        int numAdjacentTiles = 0;

        for (int i = 0; i < ADJ_LIST.size(); i++) {
            testX = ADJ_LIST.get(i).getX() + position.getX();
            testY = ADJ_LIST.get(i).getY() + position.getY();
            testPosition = new PositionOnBoard(testX, testY);
            if (board.containsKey(testPosition)) {
                numAdjacentTiles += 1;
            }
        }

        return numAdjacentTiles;
    }

    /**
     * Check if segment and feature can be connected
     * @param incomingSegment segment
     * @param feature feature
     * @return true if this segment and this feature can be connected
     *         false if not
     */
    public boolean canSegmentAndFeatureBeConnected(Segment incomingSegment, Feature feature) {
        boolean result = false;
        PositionOnBoard segmentPosition = incomingSegment.getSegmentPositionOnBoard();

        for (int i = 0; i < feature.getFeatureBorders().size(); i++) {
            PositionedBorder featureBorder = feature.getFeatureBorders().get(i);
            PositionOnBoard featureBorderPosition = featureBorder.getPositionOnBoard();

            // Check if this feature border is adjacent to the incoming segment
            if (featureBorderPosition.isPositionAdjacent(segmentPosition)) {
                String featureBorderDirection = featureBorder.getBorderDirection();
                if (segmentPosition.getX() > featureBorderPosition.getX()) {
                    if (featureBorderDirection.equals("E")) {
                        for (String segmentBorder : incomingSegment.getBorders()) {
                            if (segmentBorder.equals("W")) {
                                result = true;
                            }
                        }
                    }
                }
                else if (segmentPosition.getX() < featureBorderPosition.getX()) {
                    if (featureBorderDirection.equals("W")) {
                        for (String segmentBorder : incomingSegment.getBorders()) {
                            if (segmentBorder.equals("E")) {
                                result = true;
                            }
                        }
                    }
                }
                else if (segmentPosition.getY() > featureBorderPosition.getY()) {
                    if (featureBorderDirection.equals("S")) {
                        for (String segmentBorder : incomingSegment.getBorders()) {
                            if (segmentBorder.equals("N")) {
                                result = true;
                            }
                        }
                    }
                }
                else if (segmentPosition.getY() < featureBorderPosition.getY()) {
                    if (featureBorderDirection.equals("N")) {
                        for (String segmentBorder : incomingSegment.getBorders()) {
                            if (segmentBorder.equals("S")) {
                                result = true;
                            }
                        }
                    }
                }
                else {
                    throw new IllegalArgumentException("Invalid adjacent position");
                }
            }
        }
        return result;
    }

    /**
     * Update one feature using a new connected segment
     * @param segment a new connected segment
     * @param feature the feature to update
     */
    public void updateFeature(Segment segment, Feature feature) {
        if (!segment.getSegmentType().equals("fieldSegment")) {
            feature.updateFeatureBorders(segment);
            feature.updateFeatureSegments(segment);
            feature.updateTilePositionSet(currentTile);
        }
    }

    /**
     * Update feature list of the board
     * Merge features if necessary
     */
    public void updateBoardFeatureList() {
        for (int i = 0; i < featureList.size(); i++) {
            for (int j = i+1; j < featureList.size(); j++) {
                Feature f1 = featureList.get(i);
                Feature f2 = featureList.get(j);

                // Always make the larger feature merge the smaller feature
                if (f1.getAllSegments().size() >= f2.getAllSegments().size()) {
                    for (int k = 0; k < f2.getAllSegments().size(); k++) {
                        if (f1.getAllSegments().contains(f2.getAllSegments().get(k))) {
                            // Merge features
                            f1.mergeFeature(f2);
                            featureList.remove(f2);
                            break;
                        }
                    }
                }
                else {
                    for (int k = 0; k < f1.getAllSegments().size(); k++) {
                        if (f2.getAllSegments().contains(f1.getAllSegments().get(k))) {
                            // Merge features
                            f2.mergeFeature(f1);
                            featureList.remove(f1);
                            break;
                        }
                    }

                }
            }
        }
    }

    /**
     * Update player scores
     * @param isGameover is game over
     */
    public void updatePlayerScores(boolean isGameover) {
        for (Feature feature : featureList) {
            feature.updatePlayerScore(isGameover);
        }
    }

    /**
     * Get the current tile (active tile)
     * @return current tile (active tile)
     */
    public Tile getCurrentTile() { return currentTile; }

    /**
     * Get feature list of the board
     * @return feature list of the board
     */
    public List<Feature> getFeatureList() { return featureList; }

    /**
     * Get the board
     * @return board
     */
    public HashMap<PositionOnBoard, Tile> getBoard() { return board; }

}
