package edu.cmu.cs.cs214.hw4.core;


import java.util.ArrayList;

// CHECKSTYLE:OFF

/**
 * JSON reader
 */
public class JSONTileConfigReader {

    /**
     * Construct a JSON reader
     */
    public class JSONSegment {
        public ArrayList<String> borders;
        public String segmentType;
        public boolean arms;
    }

    /**
     * Define a JSON file
     */
    public class JSONTile {
        public String tileType;
        public int numSegments;
        public int numTiles;
        public ArrayList<JSONSegment> segments;
    }
}
// CHECKSTYLE:ON
