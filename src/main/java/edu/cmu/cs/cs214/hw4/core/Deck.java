package edu.cmu.cs.cs214.hw4.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class that represents a deck
 */
public class Deck {
    private static final int TOTAL_NUM_TILES = 72;
    private List<Tile> tileDeck;
    private List<Integer> tileFreq;

    /**
     * Construct a deck
     */
    public Deck() {
        tileDeck = new ArrayList<>();
        tileFreq = new ArrayList<>();
    }

    /**
     * Get how many tiles are left
     * @return number of tiles left
     */
    public int getNumberTilesLeft() { return tileDeck.size(); }

    /**
     * Generate a deck of tiles
     */
    public void generateTileDeck() {
        StringBuilder tileFileNameBuilder = new StringBuilder();
        for (char typeLetter = 'A'; typeLetter <= 'X'; typeLetter++) {
            String typeStrName = "" + typeLetter;
            tileFileNameBuilder.append("src/main/resources/tile");
            tileFileNameBuilder.append(typeStrName);
            tileFileNameBuilder.append(".json");
            String tileFileName = tileFileNameBuilder.toString();
            tileFileNameBuilder.setLength(0);

            Tile newTile = new Tile(tileFileName);

            tileFreq.add(newTile.getNumTiles());
            for (int i = 0; i < newTile.getNumTiles(); i++) {
                tileDeck.add(newTile);
            }
        }
        System.out.println("Done");

    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        Collections.shuffle(tileDeck);
    }

    /**
     * Draw a tile from the deck
     * @return a new tile
     */
    public Tile drawTile() {
        Random random = new Random();
        if (tileDeck.size() > 0) {
            Tile thisTile = tileDeck.get(random.nextInt(tileDeck.size()));

            // Remove this tile from deck
            tileDeck.remove(thisTile);
            return thisTile;
        }
        else {
            throw new IllegalArgumentException("No more tiles left in the deck");
        }
    }
}
