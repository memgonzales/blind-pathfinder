package gold_miner;

import java.util.ArrayList;

/**
 * Class implementing the <b>board</b> consisting of square tiles explored by the miner
 * either rationally (as a smart agent) or randomly
 *
 * <p>Note that the row and column numbers in this class (as well as in all other classes
 * responsible for the back-end behavior) are zero-based.</p>
 */
public class Board {
    /* ArrayList of the squares comprising the board */
    private Square[][] squares;
    /* Square tile that the miner must step on to win the game */
    private Square goldSquare;
    /* Dimension of the board (its length is equal to its width) */
    private final int DIMENSION;

    /**
     * Character code for a beacon square tile, which returns the distance between this tile
     * and the gold tile, provided that the gold tile is in one of its four cardinal directions
     * and that there is no pit between this tile and the gold tile
     */
    public static final char BEACON = 'B';
    /**
     * Character code for the gold square tile, which results in a winning game-over situation
     * should the miner step on this tile
     */
    public static final char GOLD = 'G';
    /**
     * Character code for a pit square tile, which results in a losing game-over situation
     * should the miner step on this tile
     */
    public static final char PIT = 'P';
    /**
     * Character code for an empty square tile, which is a regular tile with no special effects
     * whatsoever
     */
    public static final char EMPTY = 'E';
    /**
     * Character code for an out-of-bounds square tile, which is a tile outside the dimensions
     * of the board (the miner can scan this tile but cannot visit it)
     */
    public static final char OUT_OF_BOUNDS = 'O';

    /**
     * Creates a board object given the dimension, the row- and column-coordinates
     * of the beacons, the row- and column-coordinates of the pits, and the row- and
     * column-coordinates of the gold square tile
     *
     * @param dimension number of square tiles spanning the length of the board (which
     *                  is also equal to the number of tiles spanning its width)
     * @param rBeacon row-coordinates of the beacons
     * @param cBeacon column-coordinates of the beacons
     * @param rPit row-coordinates of the pits
     * @param cPit column-coordinates of the pits
     * @param rGold row-coordinate of the gold square tile
     * @param cGold column-coordinate of the gold square tile
     */
    public Board(int dimension, ArrayList<Integer> rBeacon, ArrayList<Integer> cBeacon, ArrayList<Integer> rPit,
                 ArrayList<Integer> cPit, int rGold, int cGold) {
        this.DIMENSION = dimension;
        squares = new Square[dimension][dimension];

        /* Initialize all square tiles to be empty. */
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                squares[i][j] = new Square(i, j, EMPTY);
            }
        }

        /* Set the beacons. */
        for (int i = 0; i < rBeacon.size(); i++) {
            squares[rBeacon.get(i)][cBeacon.get(i)].setDesignation(BEACON);
        }

        /* Set the pits. */
        for (int i = 0; i < rPit.size(); i++) {
            squares[rPit.get(i)][cPit.get(i)].setDesignation(PIT);
        }

        /* Set the gold square tile. */
        squares[rGold][cGold].setDesignation(GOLD);
        goldSquare = squares[rGold][cGold];
    }

    /**
     * Returns all the squares comprising this board
     *
     * @return squares comprising this board
     */
    public Square[][] getSquares() {
        return squares;
    }

    /**
     * Returns the dimension of this board
     *
     * @return dimension of this board
     */
    public int getDimension() {
        return DIMENSION;
    }

    /**
     * Returns all the square tiles sharing an edge with the given square tile
     *
     * @param s square tile whose neighboring square tiles are returned
     * @return square tiles sharing an edge with the given square tile
     */
    public ArrayList<Square> getNeighbors(Square s) {
        /* There are at most four neighboring tiles, corresponding to the four
        cardinal directions.
         */
        ArrayList<Square> ret;
        ret = new ArrayList<Square>(4);

        /* Row and column numbers of the given square */
        int row = s.getRow();
        int col = s.getCol();

        /* Exclude out-of-bounds square tiles. */
        if (!isOutOfBounds(row - 1, col)) {     // Above the square tile
            ret.add(squares[row - 1][col]);
        }

        if (!isOutOfBounds(row + 1, col)) {     // Below the square tile
            ret.add(squares[row + 1][col]);
        }

        if (!isOutOfBounds(row, col - 1)) {     // Left of the square tile
            ret.add(squares[row][col - 1]);
        }

        if (!isOutOfBounds(row, col + 1)) {     // Right of the square tile
            ret.add(squares[row][col + 1]);
        }

        return ret;
    }

    /**
     * Returns <code>true</code> if there is an unvisited square tile sharing an edge
     * with the given square tile; <code>false</code>, otherwise
     *
     * @param s square tile whose neighboring square tiles are checked
     * @return <code>true</code> if there is an unvisited square tile sharing an edge
     * with the given square tile; <code>false</code>, otherwise
     */
    public boolean hasUnvisitedNeighbors(Square s) {
        /* Neighboring square tiles of the given square tile */
        ArrayList<Square> neighbors;
        neighbors = getNeighbors(s);

        /* Check if there is an unvisited neighbor. */
        for (int i = 0; i < neighbors.size(); i++) {
            if (!neighbors.get(i).getIsVisited()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the distance between the given beacon and the gold square tile, provided
     * that the gold tile is in one of its four cardinal directions and that there is no pit
     * in between this tile and the gold tile
     *
     * <p>This function assumes that the given square tile is a beacon.</p>
     *
     * @param beacon beacon whose distance from the gold square tile is returned
     * @return distance of the given beacon from the gold square tile, subject to the
     * conditions explained
     */
    public int beaconScan(Square beacon) {
        /* Row- and column-coordinates of the given beacon and gold square tile. */
        int beaconRow;
        int beaconCol;
        int goldRow;
        int goldCol;

        beaconRow = beacon.getRow();
        beaconCol = beacon.getCol();
        goldRow = goldSquare.getRow();
        goldCol = goldSquare.getCol();

        /* Distance to be returned */
        int distance = 0;

        /* Check if the gold square tile belongs to the same column as the beacon. */
        if (beaconCol == goldCol) {

            /* Check the tiles above the beacon. */
            if (beaconRow > goldRow) {
                for (int i = beaconRow - 1; i >= 0; i--) {
                    /* Violation of the condition that there must be no pit in between
                    the beacon and the gold tile
                     */
                    if (squares[i][beaconCol].getDesignation() == PIT) {
                        return 0;
                    }

                    /* Return the distance since the gold tile has been found. */
                    if (squares[i][beaconCol].getDesignation() == GOLD) {
                        distance++;
                        break;
                    }

                    /* Uncomment for debugging. */
                    /* System.out.println("Up of Beacon: " + distance); */

                    distance++;
                }

            } else {  /* Scan below beacon. */
                for (int i = beaconRow + 1; i < DIMENSION; i++) {
                    /* Violation of the condition that there must be no pit in between
                    the beacon and the gold tile
                     */
                    if (squares[i][beaconCol].getDesignation() == PIT) {
                        return 0;
                    }

                    /* Return the distance since the gold tile has been found. */
                    if (squares[i][beaconCol].getDesignation() == GOLD) {
                        distance++;
                        break;
                    }

                    /* Uncomment for debugging. */
                    /* System.out.println("Down of Beacon: " + distance); */

                    distance++;
                }
            }

        } else if (beaconRow == goldRow) { /* Check if the gold tile belongs to the
                                              same row as the beacon. */

            /* Scan to the right of beacon. */
            if (beaconCol < goldCol) {
                for (int i = beaconCol + 1; i < DIMENSION; i++) {
                    /* Violation of the condition that there must be no pit in between
                    the beacon and the gold tile
                     */
                    if (squares[beaconRow][i].getDesignation() == PIT) {
                        return 0;
                    }

                    /* Return the distance since the gold tile has been found. */
                    if (squares[beaconRow][i].getDesignation() == GOLD) {
                        distance++;
                        break;
                    }

                    /* Uncomment for debugging. */
                    /* System.out.println("Right of Beacon: " + distance); */

                    distance++;
                }
            } else { /* Scan to the left of beacon. */
                for (int i = beaconCol - 1; i >= 0; i--) {
                    /* Violation of the condition that there must be no pit in between
                    the beacon and the gold tile
                     */
                    if (squares[beaconRow][i].getDesignation() == PIT) {
                        return 0;
                    }

                    /* Return the distance since the gold tile has been found. */
                    if (squares[beaconRow][i].getDesignation() == GOLD) {
                        distance++;
                        break;
                    }

                    /* Uncomment for debugging. */
                    /* System.out.println("Left of Beacon: " + distance); */

                    distance++;
                }
            }
        }

        return distance;
    }

    /**
     * Returns <code>true</code> if the given coordinates signify that the tile
     * is already outside the dimensions of this board; <code>false</code>, otherwise
     *
     * @param row row-coordinate of the tile
     * @param col column-coordinate of the tile
     * @return <code>true</code> if the given coordinates signify that the tile is already
     * outside the dimensions of this board; <code>false</code>, otherwise
     */
    public boolean isOutOfBounds(int row, int col) {
        if (0 <= row && row < DIMENSION && 0 <= col && col < DIMENSION) {
            return false;
        }

        return true;
    }

    /**
     * Returns a string representation of this board
     *
     * <p>The string representation contains the string representation of all the
     * squares comprising this board, alongside the neighboring tiles of each
     * square tile. </p>
     *
     * @return string representation of this board
     */
    @Override
    public String toString() {
        String ret = "";

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                ret += squares[i][j].toString() + "\t" + getNeighbors(squares[i][j]) + "\n";
            }
        }

        return ret;
    }
}
