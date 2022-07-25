package edu.cmu.cs.cs214.hw4.core;
import com.google.gson.Gson;
import edu.cmu.cs.cs214.hw4.core.Segments.Segment;
import edu.cmu.cs.cs214.hw4.core.Segments.CitySegment;
import edu.cmu.cs.cs214.hw4.core.Segments.FieldSegment;
import edu.cmu.cs.cs214.hw4.core.Segments.RoadSegment;
import edu.cmu.cs.cs214.hw4.core.Segments.MonasterySegment;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Tile class
 */
public class Tile {
    private String tileType;
    private int numSegments;
    private int numTiles;
    private List<Segment> segments = new ArrayList<>();
    private HashMap<String, String> tileBorders = new HashMap<>();  // <borderDir, borderType>
    private static final String[] BORDER_DIRECTIONS = new String[]{"N", "W", "S", "E"};
    private PositionOnBoard tilePosition = new PositionOnBoard();

    /**
     * Construct a tile using a json file
     * @param tileFile string name of the json file
     */
    public Tile(String tileFile) {
        Gson gson = new Gson();
        try {
            Reader reader = new FileReader(new File(tileFile));
            JSONTileConfigReader.JSONTile result
                    = gson.fromJson(reader, JSONTileConfigReader.JSONTile.class);

            tileType = result.tileType;
            numSegments = result.numSegments;
            numTiles = result.numTiles;

            for (int i = 0; i < numSegments; i++) {
                String segmentType = result.segments.get(i).segmentType;
                if (segmentType.equals("citySegment")) {
                    CitySegment citySegment = new CitySegment(segmentType);
                    // segments.add(new CitySegment(segmentType));
                    boolean arms = result.segments.get(i).arms;
                    citySegment.setArms(arms);
                    segments.add(citySegment);
                }
                else if (segmentType.equals("roadSegment")) {
                    RoadSegment roadSegment = new RoadSegment(segmentType);
                    segments.add(roadSegment);
                }
                else if (segmentType.equals("fieldSegment")) {
                    FieldSegment fieldSegment = new FieldSegment(segmentType);
                    segments.add(fieldSegment);
                }
                else if (segmentType.equals("monasterySegment")) {
                    MonasterySegment monasterySegment = new MonasterySegment(segmentType);
                    segments.add(monasterySegment);
                }
                else {
                    throw new IllegalArgumentException("Oops...This is not a valid segment type!");
                }

                segments.get(i).setBorders(result.segments.get(i).borders);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading file: " + tileFile, e);
        }

        // Construct tile borders
        getTileBordersFromSegments();
    }

    /**
     * Rotate the tile a fixed number of times
     * @param times number of times to rotate the tile
     */
    public void rotateTile(int times) {
        for (int i = 0; i < times; i++) {
            String firstBorderType = tileBorders.get(BORDER_DIRECTIONS[BORDER_DIRECTIONS.length-1]);
            for (int j = BORDER_DIRECTIONS.length-1; j >= 0; j--) {
                if (j > 0) {
                    tileBorders.put(BORDER_DIRECTIONS[j], tileBorders.get(BORDER_DIRECTIONS[j-1]));
                }
                else {
                    tileBorders.put(BORDER_DIRECTIONS[j], firstBorderType);
                }
            }
        }
    }

    /**
     * Construct borders of tile from segments of tile
     * Save border direction as the key, border segment type as the value in a hash map
     */
    public void getTileBordersFromSegments() {
        for (int i = 0; i < segments.size(); i++) {
            Segment segmentTemp = segments.get(i);
            String segmentType = segmentTemp.getSegmentType();
            for (int j = 0; j < segmentTemp.getBorders().size(); j++) {
                String border = segmentTemp.getBorders().get(j);
                tileBorders.put(border, segmentType);
            }
        }
    }

    /**
     * Get type of the tile
     * @return type of the tile
     */
    public String getTileType() { return tileType; }

    /**
     * Get all segments that compose this tile
     * @return all segments that live in this tile
     */
    public List<Segment> getTileSegments() { return segments; }

    /**
     * Get number of this type of tiles that live in the deck
     * @return number of this type of tiles
     */
    public int getNumTiles() { return numTiles; }

    /**
     * Get borders of this tile
     * Each border is composed as border direction and border segment type
     * @return borders of this tile
     */
    public HashMap<String, String> getTileBorders() { return tileBorders; }

    /**
     * GEt tile position on the board
     * @return tile position on the board
     */
    public PositionOnBoard getTilePosition() { return tilePosition; }

    /**
     * Set tile position on the board
     * @param incomingPosition tile position on the board
     */
    public void setTilePosition(PositionOnBoard incomingPosition) {
        tilePosition.setX(incomingPosition.getX());
        tilePosition.setY(incomingPosition.getY());

        for (Segment tileSegment : segments) {
            tileSegment.setSegmentPositionOnBoard(tilePosition);
        }
    }
}
