package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.core.Features.CityFeature;
import edu.cmu.cs.cs214.hw4.core.Features.Feature;
import edu.cmu.cs.cs214.hw4.core.Features.RoadFeature;
import edu.cmu.cs.cs214.hw4.core.Segments.CitySegment;
import edu.cmu.cs.cs214.hw4.core.Segments.Segment;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionedBorder;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TestFile {
    private Tile testTile;
    private PositionOnBoard testTilePosition;

    @Before
    public void initializeTest() { }

    @Test
    public void testJsonTile() {
        testTile = new Tile("src/main/resources/tileA.json");
        Assert.assertEquals(testTile.getTileType(), "A");
        Assert.assertEquals(testTile.getTileSegments().get(0).getSegmentType(), "fieldSegment");
        Assert.assertEquals(testTile.getTileSegments().get(0).getBorders().get(0), "N");
        Assert.assertEquals(testTile.getTileSegments().get(0).getBorders().get(1), "W");
        Assert.assertEquals(testTile.getTileSegments().get(0).getBorders().get(2), "E");
        Assert.assertEquals(testTile.getTileSegments().get(1).getSegmentType(), "monasterySegment");
        Assert.assertEquals(testTile.getTileSegments().get(2).getSegmentType(), "roadSegment");
        Assert.assertEquals(testTile.getTileSegments().get(2).getBorders().get(0), "S");
    }

    @Test
    public void testCityArms() {
        testTile = new Tile("src/main/resources/tileC.json");
        CitySegment citySegment;
        citySegment = (CitySegment) testTile.getTileSegments().get(0);
        Assert.assertEquals(citySegment.getSegmentType(), "citySegment");
        Assert.assertEquals(citySegment.getArms(), true);

        testTile = new Tile("src/main/resources/tileE.json");
        citySegment = (CitySegment) testTile.getTileSegments().get(1);
        Assert.assertEquals(citySegment.getSegmentType(), "citySegment");
        Assert.assertEquals(citySegment.getArms(), false);
    }

    @Test
    public void testDeck() {
        Deck deck = new Deck();
        deck.generateTileDeck();
        Assert.assertEquals(72, deck.getNumberTilesLeft());
        deck.shuffle();
        Tile firstTile = deck.drawTile();
        Assert.assertEquals(71, deck.getNumberTilesLeft());

        for (int i = 0; i < 71; i++) {
            Tile thisTile = deck.drawTile();
        }

        Assert.assertEquals(0, deck.getNumberTilesLeft());
    }

    @Test
    public void testRotateTile() {
        testTile = new Tile("src/main/resources/tileD.json");
        Assert.assertEquals(testTile.getTileType(), "D");
        Assert.assertEquals(testTile.getTileBorders().get("N"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("W"), "fieldSegment");
        Assert.assertEquals(testTile.getTileBorders().get("S"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("E"), "citySegment");

        int rotationTimes;
        rotationTimes= 1;
        testTile.rotateTile(rotationTimes);
        Assert.assertEquals(testTile.getTileBorders().get("N"), "citySegment");
        Assert.assertEquals(testTile.getTileBorders().get("W"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("S"), "fieldSegment");
        Assert.assertEquals(testTile.getTileBorders().get("E"), "roadSegment");

        testTile.rotateTile(rotationTimes);
        Assert.assertEquals(testTile.getTileBorders().get("N"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("W"), "citySegment");
        Assert.assertEquals(testTile.getTileBorders().get("S"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("E"), "fieldSegment");

        testTile = new Tile("src/main/resources/tileP.json");
        rotationTimes = 2;
        testTile.rotateTile(rotationTimes);
        Assert.assertEquals(testTile.getTileBorders().get("N"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("W"), "roadSegment");
        Assert.assertEquals(testTile.getTileBorders().get("S"), "citySegment");
        Assert.assertEquals(testTile.getTileBorders().get("E"), "citySegment");
    }

    @Test
    public void testFeatureBasic() {
        testTile = new Tile("src/main/resources/tileD.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(100); testTilePosition.setY(10);
        testTile.setTilePosition(testTilePosition);

        for (int i = 0; i < testTile.getTileSegments().size(); i++) {
            testTile.getTileSegments().get(i).setSegmentPositionOnBoard(testTile.getTilePosition());
        }

        CityFeature cityFeature = new CityFeature();
        RoadFeature roadFeature = new RoadFeature();
        cityFeature.updateTilePositionSet(testTile);
        roadFeature.updateTilePositionSet(testTile);

        for (int i = 0; i < testTile.getTileSegments().size(); i++) {
            if (testTile.getTileSegments().get(i).getSegmentType().equals("citySegment")) {
                cityFeature.updateFeatureSegments(testTile.getTileSegments().get(i));
                cityFeature.updateFeatureBorders(testTile.getTileSegments().get(i));
            }
            else if (testTile.getTileSegments().get(i).getSegmentType().equals("roadSegment")) {
                roadFeature.updateFeatureSegments(testTile.getTileSegments().get(i));
                roadFeature.updateFeatureBorders(testTile.getTileSegments().get(i));
            }
        }

        // Check how many segments there are in the feature
        Assert.assertEquals(cityFeature.getAllSegments().size(), 1);
        Assert.assertEquals(roadFeature.getAllSegments().size(), 1);
        // Check how many open borders there are in the feature
        Assert.assertEquals(cityFeature.getFeatureBorders().size(), 1);
        Assert.assertEquals(roadFeature.getFeatureBorders().size(), 2);
        // Check types of feature's open borders
        Assert.assertEquals(roadFeature.getFeatureBorders().get(0).getBorderDirection(), "N");
        Assert.assertEquals(roadFeature.getFeatureBorders().get(1).getBorderDirection(), "S");
        Assert.assertEquals(cityFeature.getFeatureBorders().get(0).getBorderDirection(), "E");
        // Check positions of the feature's open borders on the board
        Assert.assertEquals(roadFeature.getFeatureBorders().get(0).getPositionOnBoard().getX(), 100);
        Assert.assertEquals(roadFeature.getFeatureBorders().get(0).getPositionOnBoard().getY(), 10);
        Assert.assertEquals(roadFeature.getFeatureBorders().get(1).getPositionOnBoard().getX(), 100);
        Assert.assertEquals(roadFeature.getFeatureBorders().get(1).getPositionOnBoard().getY(), 10);
        Assert.assertEquals(cityFeature.getFeatureBorders().get(0).getPositionOnBoard().getX(), 100);
        Assert.assertEquals(cityFeature.getFeatureBorders().get(0).getPositionOnBoard().getY(), 10);
        // Check how many tiles there are in the feature
        Assert.assertEquals(cityFeature.getTotalTileNumber(), 1);
        Assert.assertEquals(roadFeature.getTotalTileNumber(), 1);

        // Add one more tile that is connected to the first tile
        // Initialize a new tile
        testTile = new Tile("src/main/resources/tileS.json");
        // Initialize a new position for the tile
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(101); testTilePosition.setY(10);  // X=100 Y=11???
        // The problem here is we assume that the two tiles are put next to each other in a valid way
        // Set tile's position
        testTile.setTilePosition(testTilePosition);
        // Set tile segments' positions
        for (int i = 0; i < testTile.getTileSegments().size(); i++) {
            testTile.getTileSegments().get(i).setSegmentPositionOnBoard(testTile.getTilePosition());
        }
        // Update city feature's tile position set
        cityFeature.updateTilePositionSet(testTile);
        // Update road feature's tile position set
        roadFeature.updateTilePositionSet(testTile);
        // Update feature's segments and feature's borders
        for (int i = 0; i < testTile.getTileSegments().size(); i++) {
            if (testTile.getTileSegments().get(i).getSegmentType().equals("citySegment")) {
                cityFeature.updateFeatureSegments(testTile.getTileSegments().get(i));
                cityFeature.updateFeatureBorders(testTile.getTileSegments().get(i));
            }
            else if (testTile.getTileSegments().get(i).getSegmentType().equals("roadSegment")) {
                //roadFeature.updateFeatureSegments(testTile.getTileSegments().get(i));
                //roadFeature.updateFeatureBorders(testTile.getTileSegments().get(i));
            }
        }

        Assert.assertEquals(2, cityFeature.getAllSegments().size());
        Assert.assertEquals(2, cityFeature.getFeatureBorders().size());
        Assert.assertEquals("N", cityFeature.getFeatureBorders().get(0).getBorderDirection());
        Assert.assertEquals("E", cityFeature.getFeatureBorders().get(1).getBorderDirection());
        Assert.assertEquals(101, cityFeature.getFeatureBorders().get(0).getPositionOnBoard().getX());
        Assert.assertEquals(101, cityFeature.getFeatureBorders().get(1).getPositionOnBoard().getX());
        Assert.assertEquals(10, cityFeature.getFeatureBorders().get(0).getPositionOnBoard().getY());
        Assert.assertEquals(10, cityFeature.getFeatureBorders().get(1).getPositionOnBoard().getY());
        Assert.assertEquals(2, cityFeature.getTotalTileNumber());
    }

    // Place tile D, S, H, I on the board to successfully form a city feature
    @Test
    public void testCityFeature() {
        Board board = new Board();

        // Place tile D
        System.out.println("Placing tile D");
        testTile = new Tile("src/main/resources/tileD.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(100); testTilePosition.setY(10);
        testTile.setTilePosition(testTilePosition);
        // Place first tile
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));
        Assert.assertEquals(2, board.getFeatureList().size());
        Assert.assertEquals("Road", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals("City", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(1).getAllSegments().size());

        // Test placing tile on an illegal position on the board
        testTile = new Tile("src/main/resources/tileS.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(100); testTilePosition.setY(11);
        testTile.setTilePosition(testTilePosition);
        Assert.assertEquals(false, board.checkPositionHasTile(testTilePosition));
        // Place second tile which cannot be placed
        board.placeTile(testTile, 0);
        // This tile is not placed because the borders are not compatible
        Assert.assertEquals(false, board.checkPositionHasTile(testTilePosition));

        // Place tile S
        System.out.println("Placing tile S");
        testTile = new Tile("src/main/resources/tileS.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(101); testTilePosition.setY(10);  // Test (100, 10)
        testTile.setTilePosition(testTilePosition);
        Assert.assertEquals(false, board.checkPositionHasTile(testTilePosition));
        // Place third tile which can be placed
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));

        // Test feature update and feature merge
        Assert.assertEquals(3, board.getFeatureList().size());
        Assert.assertEquals("Road", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());

        Assert.assertEquals("City", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(2, board.getFeatureList().get(1).getAllSegments().size());
        Assert.assertEquals(2, board.getFeatureList().get(1).getFeatureBorders().size());
        Assert.assertEquals(2, board.getFeatureList().get(1).getTilePositionSet().size());

        Assert.assertEquals("Road", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(2).getAllSegments().size());

        // Test feature completeness
        Assert.assertEquals(false, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(1).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(2).isCompleted());

        // Add some players on the board
        Player player1 = new Player(board, "player1");
        Player player2 = new Player(board, "player2");
        List<Player> players = new ArrayList<>();
        players.add(player1); players.add(player2);

        board.placeMeeples(player1, 1, testTile.getTileSegments().get(1));
        board.placeMeeples(player2, 2, testTile.getTileSegments().get(1));

        Segment thisSegment;
        thisSegment= testTile.getTileSegments().get(1);
        Map<Player, Integer> meepleDistribution = thisSegment.getMeepleDistribution();

        Assert.assertEquals(1, (int) meepleDistribution.get(player1));
        Assert.assertEquals(2, (int) meepleDistribution.get(player2));

        board.updatePlayerScores(false);
        Assert.assertEquals(0, player1.getScore());
        Assert.assertEquals(0, player2.getScore());

        // Place tile H
        System.out.println("Placing tile H");
        testTile = new Tile("src/main/resources/tileH.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(102); testTilePosition.setY(10);
        testTile.setTilePosition(testTilePosition);
        Assert.assertEquals(false, board.checkPositionHasTile(testTilePosition));
        // Place third tile which can be placed
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));

        // Test feature update and feature merge
        Assert.assertEquals(4, board.getFeatureList().size());
        Assert.assertEquals("Road", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(3, board.getFeatureList().get(1).getAllSegments().size());
        Assert.assertEquals(3, board.getFeatureList().get(1).getTilePositionSet().size());
        Assert.assertEquals("Road", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(2).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(3).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(3).getAllSegments().size());

        // Test feature completeness
        Assert.assertEquals(false, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(1).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(2).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(3).isCompleted());

        board.updatePlayerScores(false);
        Assert.assertEquals(0, player1.getScore());
        Assert.assertEquals(0, player2.getScore());

        // Place tile I
        System.out.println("Placing tile I");
        testTile = new Tile("src/main/resources/tileI.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(101); testTilePosition.setY(9);
        testTile.setTilePosition(testTilePosition);
        Assert.assertEquals(false, board.checkPositionHasTile(testTilePosition));
        // Place third tile which can be placed
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));

        // Test feature update and feature merge
        Assert.assertEquals(5, board.getFeatureList().size());
        Assert.assertEquals("Road", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(4, board.getFeatureList().get(1).getAllSegments().size());
        Assert.assertEquals(4, board.getFeatureList().get(1).getTilePositionSet().size());
        Assert.assertEquals("Road", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(2).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(3).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(3).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(4).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(4).getAllSegments().size());

        // Test feature completeness
        Assert.assertEquals(false, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(true, board.getFeatureList().get(1).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(2).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(3).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(4).isCompleted());

        board.updatePlayerScores(false);
        Assert.assertEquals(0, player1.getScore());
        Assert.assertEquals(10, player2.getScore());  // 4 tiles * 2 + 2 (coat of arms)

    }


    @Test
    public void testMonasteryFeature() {
        Board board = new Board();

        System.out.println("Placing tile A");
        testTile = new Tile("src/main/resources/tileA.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(10); testTilePosition.setY(10); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));
        Assert.assertEquals(2, board.getFeatureList().size());
        Assert.assertEquals("Monastery", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());
        Assert.assertEquals("Road", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(1).getAllSegments().size());
        Assert.assertEquals(false, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(1).isCompleted());

        System.out.println("Placing tile I");
        testTile = new Tile("src/main/resources/tileI.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(11); testTilePosition.setY(10); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(true, board.checkPositionHasTile(testTilePosition));
        Assert.assertEquals(4, board.getFeatureList().size());
        Assert.assertEquals("Monastery", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());
        Assert.assertEquals(0, board.getFeatureList().get(0).getFeatureBorders().size());
        Assert.assertEquals(1, board.getFeatureList().get(0).getTilePositionSet().size());
        Assert.assertEquals("Road", board.getFeatureList().get(1).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(1).getAllSegments().size());
        Assert.assertEquals(1, board.getFeatureList().get(1).getFeatureBorders().size());
        Assert.assertEquals(1, board.getFeatureList().get(1).getTilePositionSet().size());
        Assert.assertEquals("City", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(2).getAllSegments().size());
        Assert.assertEquals("City", board.getFeatureList().get(3).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(3).getAllSegments().size());
        Assert.assertEquals(false, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(1).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(2).isCompleted());
        Assert.assertEquals(false, board.getFeatureList().get(3).isCompleted());

        System.out.println("Placing tile G");
        testTile = new Tile("src/main/resources/tileG.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(9); testTilePosition.setY(10); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(5, board.getFeatureList().size());

        System.out.println("Placing tile I");
        testTile = new Tile("src/main/resources/tileI.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(9); testTilePosition.setY(9); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(6, board.getFeatureList().size());

        System.out.println("Placing tile Q");
        testTile = new Tile("src/main/resources/tileQ.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(10); testTilePosition.setY(9); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(6, board.getFeatureList().size());

        System.out.println("Placing tile R");
        testTile = new Tile("src/main/resources/tileR.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(11); testTilePosition.setY(9); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(6, board.getFeatureList().size());

        System.out.println("Placing tile M");
        testTile = new Tile("src/main/resources/tileM.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(9); testTilePosition.setY(11); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(6, board.getFeatureList().size());

        System.out.println("Placing tile U");
        testTile = new Tile("src/main/resources/tileU.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(10); testTilePosition.setY(11); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(6, board.getFeatureList().size());

        System.out.println("Placing tile J");
        testTile = new Tile("src/main/resources/tileJ.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(11); testTilePosition.setY(11); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(7, board.getFeatureList().size());

        Assert.assertEquals("Monastery", board.getFeatureList().get(0).getFeatureType());
        Assert.assertEquals(1, board.getFeatureList().get(0).getAllSegments().size());  // Monastery segment
        Assert.assertEquals(true, board.getFeatureList().get(0).isCompleted());
        Assert.assertEquals(9, board.getFeatureList().get(0).computeScore(false));

        Assert.assertEquals("City", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(2, board.getFeatureList().get(2).getAllSegments().size());
        Assert.assertEquals(2, board.getFeatureList().get(2).getTilePositionSet().size());
        Assert.assertEquals(true, board.getFeatureList().get(2).isCompleted());
        Assert.assertEquals(4, board.getFeatureList().get(2).computeScore(false));
    }

    @Test
    public void testRoadFeature() {
        Board board = new Board();

        System.out.println("Placing tile X");
        testTile = new Tile("src/main/resources/tileX.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(10); testTilePosition.setY(10); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(4, board.getFeatureList().size());

        System.out.println("Placing tile A");
        testTile = new Tile("src/main/resources/tileA.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(10); testTilePosition.setY(9); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(5, board.getFeatureList().size());

        System.out.println("Placing tile W");
        testTile = new Tile("src/main/resources/tileW.json");
        testTilePosition = new PositionOnBoard();
        testTilePosition.setX(11); testTilePosition.setY(10); testTile.setTilePosition(testTilePosition);
        board.placeTile(testTile, 0);
        Assert.assertEquals(7, board.getFeatureList().size());

//        for (Feature feature : board.getFeatureList()) {
//            System.out.println("Feature type = " + feature.getFeatureType()
//                    +" Feature completed? = "+feature.isCompleted()
//                    +" Feature tile position set = "+feature.getTilePositionSet());
//        }

        Assert.assertEquals("Road", board.getFeatureList().get(2).getFeatureType());
        Assert.assertEquals(2, board.getFeatureList().get(2).getAllSegments().size());
        Assert.assertEquals(2, board.getFeatureList().get(2).getTilePositionSet().size());
        Assert.assertEquals(true, board.getFeatureList().get(2).isCompleted());
        Assert.assertEquals(2, board.getFeatureList().get(2).computeScore(false));

        Assert.assertEquals("Road", board.getFeatureList().get(3).getFeatureType());
        Assert.assertEquals(2, board.getFeatureList().get(3).getAllSegments().size());
        Assert.assertEquals(2, board.getFeatureList().get(3).getTilePositionSet().size());
        Assert.assertEquals(true, board.getFeatureList().get(3).isCompleted());
        Assert.assertEquals(2, board.getFeatureList().get(3).computeScore(false));
    }

    @Test
    public void testPlayers() {
        Board board = new Board();
        Player player1 = new Player(board, "player1");
        Player player2 = new Player(board, "player2");

        Assert.assertEquals("player1", player1.getName());
        Assert.assertEquals("player2", player2.getName());

        Assert.assertEquals(7, player1.getNumMeeplesLeft());
        Assert.assertEquals(7, player2.getNumMeeplesLeft());
    }

    // Test game manager
    @Test
    public void testGameManager() {
        GameManager gameManager = new GameManager();
        // Simulate a game
        gameManager.startGame();

    }


}
