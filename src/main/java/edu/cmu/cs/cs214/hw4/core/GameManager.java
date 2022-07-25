package edu.cmu.cs.cs214.hw4.core;

import edu.cmu.cs.cs214.hw4.core.Segments.Segment;
import edu.cmu.cs.cs214.hw4.core.Utils.PositionOnBoard;

import java.util.ArrayList;
import java.util.Random;


/**
 * Class that represents a game manager
 */
public class GameManager {
    private static final int BOARD_ROWS_FOR_SIMULATION = 100;
    private static final int BOARD_COLS_FOR_SIMULATION = 100;
    private static final int MIN_NUM_PLAYER = 2;
    private static final int MAX_NUM_PLAYER = 5;
    private ArrayList<Player> players;
    private Board board;
    private Deck deck;
    private int totalPlayerNumber;

    /**
     * Construct a game manager
     */
    public GameManager() {
        players = new ArrayList<>();
        board = new Board();
        deck = new Deck();
        totalPlayerNumber = 0;
    }

    /**
     * Check if game is over
     * @return true if game is over
     *         false if not
     */
    public boolean isGameOver() {
        return (deck.getNumberTilesLeft() == 0);
    }

    /**
     * Initialize the game
     */
    private void initializeGame() {
        deck.generateTileDeck();
        deck.shuffle();
    }

    /**
     * Add player to the game
     * @param playerName string that represents a player name
     */
    public void addPlayer(String playerName) {
        Player newPlayer = new Player(board, playerName);
        players.add(newPlayer);
    }

    /**
     * Update player scores
     * @param isGameOver is game over
     */
    public void updatePlayerScores(boolean isGameOver) {
        board.updatePlayerScores(isGameOver);
    }

    /**
     * Play the game
     */
    public void startGame() {
        initializeGame();

        System.out.println("Adding players");
        while (totalPlayerNumber < MIN_NUM_PLAYER) {
            addPlayer("player"+totalPlayerNumber);
            totalPlayerNumber += 1;
        }
        System.out.println(players.size());
        if (totalPlayerNumber > MAX_NUM_PLAYER || totalPlayerNumber < MIN_NUM_PLAYER) {
            throw new IllegalArgumentException("Total number of players is not valid for the game.");
        }

        System.out.println("Entering game loop");
        while (!isGameOver()) {
            System.out.println("Number of tiles left = "+deck.getNumberTilesLeft());
            int playerTurn = 0;

            for (Player currentPlayer : players) {
                System.out.println("Player turn = "+playerTurn);
                Tile newTile = deck.drawTile();
                PositionOnBoard newTilePosition = new PositionOnBoard();
                Random random = new Random();
                while (true) {
                    // For testing
                    int tileX = random.nextInt(BOARD_ROWS_FOR_SIMULATION);
                    int tileY = random.nextInt(BOARD_COLS_FOR_SIMULATION);
                    newTilePosition.setX(tileX);
                    newTilePosition.setY(tileY);
                    newTile.setTilePosition(newTilePosition);
                    System.out.println("Try putting the tile on position = "
                            +" X = "+tileX
                            +" Y = "+tileY);
                    boolean placeTileSuccessful = currentPlayer.placeTile(newTile, 0);
                    if (placeTileSuccessful) {
                        currentPlayer.setWantToPlaceMeeples(true);
                        if (currentPlayer.getWantToPlaceMeeples()) {
                            //Assume the player wants to place meeples on this segment of the new tile
                            Segment meepleSegment = newTile.getTileSegments().get(0);
                            currentPlayer.placeMeeples(meepleSegment, 1);
                        }
                        break;
                    }
                }
                playerTurn += 1;
            }
            updatePlayerScores(isGameOver());
        }
    }
}
