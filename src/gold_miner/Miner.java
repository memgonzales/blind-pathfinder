package gold_miner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Class implementing the <b>rational agent</b> whose actions are deliberated via more
 * sophisticated decision-making processes (explained in the technical report accompanying
 * this program)
 *
 * <p>Note that the row and column numbers in this class (as well as in all other classes
 * responsible for the back-end behavior) are zero-based.</p>
 */
public class Miner {
    /* Row-coordinate of the position of this miner */
    private int row;
    /* Column-coordinate of the position of this miner */
    private int col;

    /* Direction to which this miner is facing */
    private char front;

    /* List of out-of-bounds tiles scanned by this miner */
    private ArrayList<Square> scannedOutOfBounds;
    /* Stack storing the square tiles that will be part of the final path to the gold tile */
    private Stack<Square> path;
    /* Sequence of actions followed by this miner to reach the gold tile */
    private LinkedList<String> preLoadedMoves;

    /* String representation of the current action of this agent (for use in the GUI) */
    private ArrayList<String> currStack;

    /* Out-of-bounds tile currently being considered by this rational agent */
    private Square currOutOfBounds;

    /* Number of rotations */
    private static int numRotate = 0;
    /* Number of scans */
    private static int numScan = 0;
    /* Number of moves */
    private static int numMove = 0;
    /* Number of backtracks */
    private static int numBacktrack  = 0;

    /* Distance to the gold square tile as returned by the beacon (for use in the GUI) */
    private static int beaconDistance = -1;

    /**
     * Character code corresponding to a move to the tile above this miner's current position
     */
    public static final char UP = 'U';
    /**
     * Character code corresponding to a move to the tile to the right of this miner's current position
     */
    public static final char RIGHT = 'R';
    /**
     * Character code corresponding to a move to the tile to the left of this miner's current position
     */
    public static final char LEFT = 'L';
    /**
     * Character code corresponding to a move to the tile below this miner's current position
     */
    public static final char DOWN = 'D';

    /**
     * Character code signifying that the gold square tile has been reached by this miner
     */
    public static final char GOLD_FOUND = 'G';
    /**
     * Character code signifying that this miner cannot make a valid move (that is, move to an unvisited
     * within-bounds tile or to a non-pit tile) without rotating
     */
    public static final char STOP_FOUND = 'S';
    /**
     * Character code signifying that a beacon square tile has been reached by this miner
     */
    public static final char BEACON_FOUND = 'B';

    /**
     * Returned when method is unsuccessful (for example, scanning an out-of-bounds square)
     */
    public static char FAIL = 'F';

    /**
     * Creates a rational agent whose actions are deliberated via more sophisticated decision-making
     * processes (explained in the technical report accompanying this program)
     *
     * @param b board explored by this agent (miner)
     */
    public Miner(Board b) {
        row = 0;
        col = 0;

        /* Facing to the right or facing down are both viable initial choices for the miner.
        Right was chosen arbitrarily.
         */
        front = RIGHT;

        /* Each side/edge of the board gives n possible out-of-bounds tiles, where n is the dimension. */
        scannedOutOfBounds = new ArrayList<Square>(b.getDimension() * 4);
        path = new Stack<Square>();
        currStack = new ArrayList<String>();
        preLoadedMoves = new LinkedList<String>();

        currOutOfBounds = null;

        /* The first square tile visited by the miner is its initial position. */
        path.add(b.getSquares()[0][0]);
        currStack.add(b.getSquares()[0][0].toString() + "\n");
        b.getSquares()[0][0].visit();
    }

    /**
     * Returns the row-coordinate of this miner's current position
     *
     * @return row-coordinate of this miner's current position
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column-coordinate of this miner's current position
     *
     * @return column-coordinate of this miner's current position
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the direction to which this miner is currently facing
     *
     * @return direction to which this miner is currently facing
     */
    public char getFront() {
        return front;
    }

    /**
     * Action corresponding to a rotation by this rational agent
     *
     * <p>Per the machine project specifications, this miner can only rotate clockwise.
     * A rotation changes the direction to which this miner is facing although it does
     * alter its current position. </p>
     */
    public void rotate() {
        /* Reflects the clockwise direction of rotation */
        char[] order = {RIGHT, DOWN, LEFT, UP};

        int index;          // index with respect to the character array order
        index = 0;          // initialized to prevent error flag from the compiler
        switch (front) {
            case RIGHT:
                index = 0;
                break;
            case DOWN:
                index = 1;
                break;
            case LEFT:
                index = 2;
                break;
            case UP:
                index = 3;
                break;
        }

        /* Perform actual rotation. */
        index = (index + 1) % order.length;
        front = order[index];

        /* Increment counter. */
        numRotate++;

        /* Add to the sequence of actions. */
        preLoadedMoves.add("Rotate " + this + "\t"
                + getNumActions() + "\t"
                + "Rotate: " + getMoveSequence() + "\t"
                + getPathStack() + "\t"
                + getOutOfBoundsTiles());

        /* Uncomment for debugging. */
        /* System.out.println("Rotate " + numRotate + ": " + this); */
    }

    /**
     * Action undertaken by this rational agent in determining the designation of visited tiles
     * stored in its memory that share an edge with the square tile it currently occupies,
     * particularly in relation to the method <code>getNumRotateMemory</code>
     *
     * <p>Its under-the-hood is similar to a pseudo-rotation (since the agent can "recall"
     * neighboring tiles by "facing" in their direction). However, it is important to stress
     * that no actual rotation is taking place; it can be thought of as a rotation that
     * happens <b>only in its memory</b>. </p>
     *
     * <p>Therefore, it does <b>not</b> contribute to the total number of rotations made by
     * this agent.</p>
     */
    public void hiddenRotate() {
        /* Reflects the clockwise direction of rotation */
        char[] order = {RIGHT, DOWN, LEFT, UP};

        int index;          // index with respect to the character array order
        index = 0;          // initialized to prevent error flag from the compiler
        switch (front) {
            case RIGHT:
                index = 0;
                break;
            case DOWN:
                index = 1;
                break;
            case LEFT:
                index = 2;
                break;
            case UP:
                index = 3;
                break;
        }

        /* Perform pseudo-rotation, as explained in the method documentation. */
        index = (index + 1) % order.length;
        front = order[index];

        /* Do not increment counter, and do not add this to the sequence of actions. */

        /* Uncomment for debugging. */
        /* System.out.println("Remembering (rotate)..."); */
    }

    /**
     * Action corresponding to a move by this rational agent
     *
     * <p>Per the machine project specifications, this miner can only move to an adjacent
     * tile in the direction to where it is facing. A movement changes the position
     * of this miner although it does not alter its current orientation.
     *
     * @param b board on which this miner is moving
     */
    public void move(Board b) {
        /* This miner should not move to an out-of-bounds square tile. */
        switch (front) {
            case UP:
                if (!b.isOutOfBounds(row - 1, col))
                    row--;
                break;
            case RIGHT:
                if (!b.isOutOfBounds(row, col + 1))
                    col++;
                break;
            case LEFT:
                if (!b.isOutOfBounds(row, col - 1))
                    col--;
                break;
            case DOWN:
                if (!b.isOutOfBounds(row + 1, col))
                    row++;
                break;
        }

        /* Mark the new position of the miner as visited. */
        b.getSquares()[row][col].visit();
        /* Include the newly occupied tile to the path stack. */
        path.push(b.getSquares()[row][col]);
        currStack.add(b.getSquares()[row][col].toString() + "\n");

        /* Increment counter. */
        numMove++;

        /* Add to the sequence of actions. */
        preLoadedMoves.add("Move " + this + "\t"
                + getNumActions() + "\t"
                + "Move: " + getMoveSequence() + "\t"
                + getPathStack() + "\t"
                + getOutOfBoundsTiles());

        /* Uncomment for debugging. */
        /* System.out.println("Move " + numMove + ": " + this); */
    }

    /**
     * Returns the square tile in front of this rational agent
     *
     * <p>This is distinct from a scan since this method is internally used by the
     * agent as part of its decision-making processes. Specifically, if this next tile
     * has already been scanned, then it is not rescanned to prevent redundancy; it suffices
     * to consult its memory. Otherwise, this next tile is scanned. </p>
     *
     * @param b board explored by this rational agent (miner)
     * @return square tile in front of this rational agent
     */
    public Square getNextTile(Board b) {
        /* Only the square tile in front of the miner is returned. */
        switch (front) {
            case UP:
                if (!b.isOutOfBounds(row - 1, col)) {
                    return b.getSquares()[row - 1][col];
                } else {
                    currOutOfBounds = new Square(row - 1, col, Board.OUT_OF_BOUNDS);
                }
                break;

            case RIGHT:
                if (!b.isOutOfBounds(row, col + 1)) {
                    return b.getSquares()[row][col + 1];
                } else {
                    currOutOfBounds = new Square(row, col + 1, Board.OUT_OF_BOUNDS);
                }
                break;

            case LEFT:
                if (!b.isOutOfBounds(row, col - 1)) {
                    return b.getSquares()[row][col - 1];
                } else {
                    currOutOfBounds = new Square(row, col - 1, Board.OUT_OF_BOUNDS);
                }
                break;

            case DOWN:
                if (!b.isOutOfBounds(row + 1, col)) {
                    return b.getSquares()[row + 1][col];
                } else {
                    currOutOfBounds = new Square(row + 1, col, Board.OUT_OF_BOUNDS);
                }
                break;
        }

        /* There is no within-bounds square tile in front of the miner. */
        return null;
    }

    public Square getNextTile(Board b, int row, int col) {
        /* Only the square tile in front of the miner is returned. */
        switch (front) {
            case UP:
                if (!b.isOutOfBounds(row - 1, col)) {
                    return b.getSquares()[row - 1][col];
                } else {
                    currOutOfBounds = new Square(row - 1, col, Board.OUT_OF_BOUNDS);
                }
                break;

            case RIGHT:
                if (!b.isOutOfBounds(row, col + 1)) {
                    return b.getSquares()[row][col + 1];
                } else {
                    currOutOfBounds = new Square(row, col + 1, Board.OUT_OF_BOUNDS);
                }
                break;

            case LEFT:
                if (!b.isOutOfBounds(row, col - 1)) {
                    return b.getSquares()[row][col - 1];
                } else {
                    currOutOfBounds = new Square(row, col - 1, Board.OUT_OF_BOUNDS);
                }
                break;

            case DOWN:
                if (!b.isOutOfBounds(row + 1, col)) {
                    return b.getSquares()[row + 1][col];
                } else {
                    currOutOfBounds = new Square(row + 1, col, Board.OUT_OF_BOUNDS);
                }
                break;
        }

        /* There is no within-bounds square tile in front of the miner. */
        return null;
    }

    /**
     * Action corresponding to the longest series of moves undertaken by this miner
     * without the need for any rotation; returns a character code corresponding to the
     * trigger halting its unidirectional movement
     *
     * <p>In particular, this method can return of any of these values:</p>
     * <ul>
     *     <li><code>GOLD_FOUND</code> - Gold square tile has been reached by this miner</li>
     *     <li><code>BEACON_FOUND</code> - A beacon square tile has been reached by this miner</li>
     *     <li><code>STOP_FOUND</code> - This miner cannot make a valid move (that is, move to an unvisited
     *      within-bounds tile or to a non-pit tile) without rotating</li>
     * </ul>
     *
     * @param b board explored by this rational agent (miner)
     * @return character code corresponding to the trigger halting the miner's unidirectional
     * movement
     */
    public char moveUntilAllowed(Board b) {
        Square nextTile;                // Square tile in front of the miner
        nextTile = getNextTile(b);

        /* The miner cannot move into an out-of-bounds tile nor can it move into a pit (since this
        would result in a losing game-over situation). Moreover, as part of its decision-making
        processes, it should not move (that is, re-visit) an already visited tile outside of
        backtracking.
        */
        while (nextTile != null && nextTile.getDesignation() != Board.PIT
                && !nextTile.getIsVisited()) {
            /* Move the miner. This is safe at the first iteration since the next tile has already
            been determined prior to loop initialization. */
            move(b);

            Square currTile;                        // Newly occupied tile
            currTile = b.getSquares()[row][col];

            /* Reaching either a gold or a beacon automatically halts the move sequence. */
            if (currTile.getDesignation() == Board.GOLD) {
                beaconDistance = -1;
                return GOLD_FOUND;
            } else if (currTile.getDesignation() == Board.BEACON) {
                beaconDistance = b.beaconScan(b.getSquares()[row][col]);
                return BEACON_FOUND;
            } else {
                /* This miner did not land on a beacon tile. */
                beaconDistance = -1;
            }

            /* Get the tile in front of the miner. */
            nextTile = getNextTile(b);

            /* Scan this next tile only if it has been scanned beforehand. */
            if (nextTile != null && !nextTile.getIsVisited()) {
                scan();
            } else if (nextTile == null) {
                /* The rational agent also keeps track of the scanned out-of-bounds tiles
                to prevent redundancy.
                */
                if (!scannedOutOfBounds.contains(currOutOfBounds)) {
                    scannedOutOfBounds.add(currOutOfBounds);
                    scan();
                }
            }

            /* The tile in front of the miner is a pit. */
            if (nextTile != null && nextTile.getDesignation() == Board.PIT) {

                /* Although the miner does not move to pit, this pit is still marked as visited
                so as to prevent redundant scanning (since the under-the-hood decision-making as
                to whether a tile is scanned or not involves checking if it has been visited. */
                nextTile.visit();
            }

            /* Uncomment for debugging. */
            /* System.out.println("---->" + nextTile); */
        }

        /* Uncomment for debugging. */
        /* System.out.println("-- move end --\n"); */

        /* The miner cannot anymore proceed in a unidirectional line of movement. */
        return STOP_FOUND;
    }

    /**
     * Returns the minimum number of times that this miner must rotate in order to face
     * the first unscanned square tile
     *
     * <p>A return value equal to the number of cardinal directions, that is, 4, signifies
     * that there is no unvisited or unscanned tile among its neighboring tiles, signaling
     * the need to backtrack. </p>
     *
     * <p>Under the hood, this function calls the method <code>hiddenRotate</code>, which is
     * a pseudo-rotation (as opposed to an actual, locomotive rotation), in order to "recall"
     * whether it has visited or scanned neighboring tiles. Therefore, the execution of
     * this method does not contribute to the number of actions incurred. </p>
     *
     * @param b board explored by this rational agent (miner)
     * @return minimum number of times that this miner must rotate in order to face the
     * fist unscanned square tile
     */
    public int getNumRotateMemory(Board b) {
        int numRotate;                  // return value
        numRotate = 0;

        Square nextTile;                // neighboring tiles of the current tile
        nextTile = getNextTile(b);

        /* Uncomment for debugging. */
        /* System.out.println("******************************** " + nextTile); */

        /* If a miner needs to perform at least 4 rotations, then there is no unvisited
        or unscanned tile among its neighboring square tiles, signaling the need to backtrack.

        The miner cannot move into an out-of-bounds tile nor can it move into a pit (since this
        would result in a losing game-over situation). Moreover, as part of its decision-making
        processes, it should not move (that is, re-visit) an already visited tile outside of
        backtracking.
        */
        while (numRotate < 4 && (nextTile == null || (nextTile.getDesignation() == Board.PIT
                || nextTile.getIsVisited()))) {
            /* Pseudo-rotate and increment counter. */
            hiddenRotate();
            numRotate++;

            /* Consider the neighboring tile in another direction. */
            nextTile = getNextTile(b, row, col);
        }

        /* Since this function is merely for decision-making, it must not change the state
        of the miner in any way whatsoever. Therefore, the miner must perform pseudo-rotation
        to restore its original orientation. */
        int remRotate;
        remRotate = 4 - numRotate;              // there are four cardinal directions

        for (int i = 0; i < remRotate; i++) {
            hiddenRotate();
        }

        return numRotate;
    }

    /**
     * Returns <code>true</code> if this miner can perform a finite series of rotations
     * in order to face a tile that has neither been visited nor scanned; <code>false</code>,
     * otherwise (indicating the need to perform backtracking)
     *
     * @param b board explored by this rational agent (miner)
     * @return <code>true</code> if this miner can perform a finite series of rotations
     * in order to face a tile that has neither been visited nor scanned; <code>false</code>,
     * otherwise
     */
    public boolean rotateUntilAllowed(Board b) {
        /* The number of rotations is pre-computed in advance to remove unnecessary rotations. */
        int numRotate;
        numRotate = getNumRotateMemory(b);

        /* Uncomment for debugging. */
        /* System.out.println("Optimal rotates: " + numRotate); */

        /* If a miner needs to perform at least 4 rotations, then there is no unvisited
        or unscanned tile among its neighboring square tiles, signaling the need to backtrack.
         */
        if (numRotate == 4) {
            return false;
        } else {
            for (int i = 0; i < numRotate; i++) {
                /* Perform actual rotation. */
                rotate();

                /* Get the tile in front of the miner. */
                Square nextTile = getNextTile(b);

                /* Scan this next tile only if it has been scanned beforehand. */
                if (nextTile != null && !nextTile.getIsVisited()) {
                    scan();
                } else if (nextTile == null) {
                    /* The rational agent also keeps track of the scanned out-of-bounds tiles
                    to prevent redundancy.
                     */
                    if (!scannedOutOfBounds.contains(currOutOfBounds)) {
                        scannedOutOfBounds.add(currOutOfBounds);
                        scan();
                    }
                }
            }
        }

        /* No need to backtrack */
        return true;
    }

    /**
     * Performs a series of rotations given the square tile currently occupied by this miner
     * in order to face in the direction of the specified <b>neighboring</b> square tile
     *
     * @param from square tile currently occupied by the miner
     * @param to neighboring square tile
     */
    public void rotateUntilFacing(Square from, Square to) {
        /* Note that there are deliberately no break statements separating the cases
        since the implementation of this method takes advantage of the cascading
        nature of switch-case constructs.

        Consequently, the order in which the cases are written is also important.
         */
        if (from.isOtherDown(to)) {
            switch (front) {
                case LEFT:              // rotate once
                    rotate();
                case UP:                // rotate twice
                    rotate();
                case RIGHT:             // rotate thrice
                    rotate();
            }
        } else if (from.isOtherUp(to)) {
            switch (front) {
                case RIGHT:
                    rotate();
                case DOWN:
                    rotate();
                case LEFT:
                    rotate();
            }
        } else if (from.isOtherLeft(to)) {
            switch(front) {
                case UP:
                    rotate();
                case RIGHT:
                    rotate();
                case DOWN:
                    rotate();
            }
        } else if (from.isOtherRight(to)) {
            switch(front) {
                case DOWN:
                    rotate();
                case LEFT:
                    rotate();
                case UP:
                    rotate();
            }
        }
    }

    /**
     * Action corresponding to backtracking, that is, retracing the previously taken path
     * and finding the earliest square tile in the path stack with an unvisited neighbor;
     * returns <code>true</code> if such a tile can be found; <code>false</code>, otherwise
     * (signaling an invalid board configuration)
     *
     * <p>An invalid board configuration occurs when either the miner or the gold tile
     * is enclosed in a polygonal barricade of pits. In this case, the path stack is
     * emptied without finding the desired tile, triggering the <code>false</code>
     * return value. </p>
     *
     * @param b board explored by this rational agent (miner)
     * @return <code>true</code> if this agent can find a square tile in the path stack
     * with an unvisited neighbor (following the mechanics of backtracking); <code>false,
     * otherwise</code>
     */
    public boolean backtrack(Board b) {
        /* Add to the sequence of actions. */
        preLoadedMoves.add("Backtrack start " + "\t"
                + getNumActions() + "\t"
                + "Backtrack start " + "\t"
                + getPathStack() + "\t"
                + getOutOfBoundsTiles());

        /* Uncomment for debugging. */
        /* System.out.println("-- backtrack start --\n");
        System.out.println(path); */


        /* Pop the most recent move from the path stack (consider error catching
        for trying to pop from an empty stack for a more robust/smarter implementation).
         */
        try {
            path.pop();
            currStack.remove(currStack.size() - 1);

        } catch (Exception e) {     /* Invalid board configuration */
            /* Add to the sequence of actions. */
            preLoadedMoves.add("Invalid board layout" + "\t"
                    + getNumActions() + "\t"
                    + "Invalid board layout!" + "\t"
                    + getPathStack() + "\t"
                    + getOutOfBoundsTiles());

            /* Uncomment for debugging. */
            /* System.out.println("Invalid board layout!"); */
            return false;
        }

        /* Backtracking per se

        Continue popping from the stack until a tile with an unvisited neighbor can be found.
         */
        while (!path.isEmpty()) {
            Square toSquare;                // popped tile to which the miner should face
            toSquare = path.pop();

            /* Remove from the GUI display as well. */
            currStack.remove(currStack.size() - 1);

            Square currSquare;              // tile currently occupied by the miner
            currSquare = b.getSquares()[row][col];

            /* Face and move to the popped tile (that is, the previously occupied tile
            before backtracking occurred).
             */
            rotateUntilFacing(currSquare, toSquare);
            move(b);

            /* Backtracking is completed. */
            if (b.hasUnvisitedNeighbors(toSquare)) {
                /* Uncomment for debugging. */
                /* System.out.println(path); */

                break;
            } else {
                /* Since move pushes a tile into the path stack, this newly pushed tile
                must be popped again to continue the backtracking.
                 */
                path.pop();
                currStack.remove(currStack.size() - 1);
            }

        }

        /* Only the last step of the backtrack gives an unvisited tile. */
        scan();

        /* Increment counter. */
        numBacktrack++;

        /* Add to the sequence of actions. */
        preLoadedMoves.add("Backtrack end " + "\t"
                + getNumActions() + "\t"
                + "Backtrack end " + "\t"
                + getPathStack() + "\t"
                + getOutOfBoundsTiles());

        /* Uncomment for debugging. */
        /* System.out.println("-- backtrack " + numBacktrack + " end --\n"); */

        /* Successful backtracking */
        return true;
    }

    /**
     * Performs a series of actions triggered by landing on a square tile designated
     * as a beacon
     *
     * @param b board explored by this rational agent (miner)
     * @param distance distance returned by the beacon
     * @param beaconRow row-coordinate of the beacon
     * @param beaconCol column-coordinate of the beacon
     */
    public void movePerBeacon(Board b, int distance, int beaconRow, int beaconCol) {
        /* This guarantees that all four cardinal directions will be checked. */
        for (int j = 0; true; j++) {

            /* Check if all the tiles in a certain cardinal direction have been
            visited already. If this is the case, then this direction does not have
            to be checked/explored.
             */
            boolean isAllVisited;
            isAllVisited = true;

            Square nextTileTest;
            nextTileTest = getNextTile(b);

            for (int i = 0; i < distance; i++) {
                /* Uncomment for debugging. */
                /* System.out.println(nextTileTest); */

                /* Get the tile in front of the miner. */

                /* If there is an unvisited tile in this direction, terminate
                the loop already and proceed to the succeeding loop.
                 */
                if (nextTileTest == null) {
                    break;
                }

                if (!nextTileTest.getIsVisited()) {
                    isAllVisited = false;
                    break;
                } else {
                    nextTileTest = getNextTile(b, nextTileTest.getRow(), nextTileTest.getCol());
                }
            }

            /* Uncomment for debugging. */
            /* System.out.println(isAllVisited); */

            /* Move by a certain number of tiles, at most the distance returned by the beacon. */
            for (int i = 0; i < distance && !isAllVisited; i++) {
                /* Get the tile in front of the miner. */
                Square nextTile;
                nextTile = getNextTile(b);

                /* Scan this next tile only if it has been scanned beforehand. */
                if (nextTile != null && !nextTile.getIsVisited()) {
                    scan();
                } else if (nextTile == null) {
                     /* The rational agent also keeps track of the scanned out-of-bounds tiles
                    to prevent redundancy.
                     */
                    if (!scannedOutOfBounds.contains(currOutOfBounds)) {
                        scannedOutOfBounds.add(currOutOfBounds);
                        scan();
                    }
                }

                /* The miner is already at the edge of the board. */
                if (nextTile == null) {
                    /* Uncomment for debugging. */
                    /* System.out.println("Next tile is out of bounds"); */
                    break;
                }

                /* The next tile is the gold tile tile. */
                if (nextTile.getDesignation() == Board.GOLD) {
                    /* Uncomment for debugging. */
                    /* System.out.println("Next tile is gold"); */

                    /* Advance to the gold square tile. */
                    move(b);
                    return;

                } else if (nextTile.getDesignation() == Board.PIT) { /* The next tile is a pit. */
                    /* Uncomment for debugging. */
                    /* System.out.println("Next tile is pit"); */

                    /* Consider next cardinal direction already. */
                    break;

                } else if (nextTile.getDesignation() == Board.BEACON) { /* The next tile is another beacon. */
                    /* Uncomment for debugging. */
                    /* System.out.println("Next tile is beacon"); */

                    int secondDistance;         // distance returned by the second encountered beacon
                    secondDistance = b.beaconScan(nextTile);

                    /* If the distance returned by this other beacon is 0,
                    then miner is in the wrong direction; otherwise, it should have returned
                    a positive value that is smaller than the distance returned by the beacon
                    encountered earlier.

                    If the distance returned by this other beacon is greater than the
                    distance returned by the beacon encountered earlier, then the miner
                    is in the opposite of the correct direction.
                     */
                    if (secondDistance == 0 || secondDistance >= distance) {
                        /* Move to this tile. */
                        move(b);
                        /* Consider next cardinal direction already. */
                        break;
                    }

                    /* The miner is in the correct direction. */
                    move(b);

                } else { /* The next tile is just an empty tile. */
                    /* Uncomment for debugging. */
                    /* System.out.println("Next tile is empty"); */

                    /* The miner just proceeds to the next tile routinely. */
                    move(b);
                }
            }

            /* Uncomment for debugging. */
            /* System.out.println("###### " + j); */

            /* Add to the sequence of actions. */
            preLoadedMoves.add("Possible backtrack start " + "\t"
                    + getNumActions() + "\t"
                    + "Possible backtrack start " + "\t"
                    + getPathStack() + "\t"
                    + getOutOfBoundsTiles());


            /* For instance, if the miner is approaches the beacon facing up, then
            he follows this move order: move to the tiles above the beacon, then to
            those below the beacon, then to those to the right of the beacon, then
            (finally) to those to the left of the beacon.

            In particular, the sequence of locomotive actions is as follows:
            a. Miner goes up, reaching the last pertinent tile above the beacon.
            b. Miner rotates twice in order to face down.
            c. Miner goes down, back to the beacon.
            d. Miner goes down further, reaching the last pertinent tile below the beacon.
            e. Miner rotates twice in order to face up.
            f. Miner goes up, back to the beacon.
            g. Miner rotates once in order to face to the right.
            h. Miner goes to the right, reaching the last pertinent tile to the right
               of the beacon.
            i. Miner rotates twice in order to face to the left.
            j. Miner goes to the left, back to the beacon.
            k. Miner goes to the left further, reaching the last pertinent tile to the
               right of the beacon.
             */

            /* This corresponds to steps (b), (e), and (i). */
            if (j != 3) {
                rotate();
                rotate();
            }

            /* The previous size of the path stack is tracked in order to determine
            if backtracking has occurred.
             */
            int prevSize;
            prevSize = path.size();

            /* Go back to the beacon. */
            while (row != beaconRow || col != beaconCol) {     // THIS BLOCK IS SKIPPED
                path.pop();
                path.pop();
                currStack.remove(currStack.size() - 1);
                currStack.remove(currStack.size() - 1);
                move(b);
            }

            /* This corresponds to step (g). */
            if (j == 1) {
                rotate();
            }

            /* Add to the sequence of actions if backtracking did occur. */
            if (prevSize != path.size()) {
                /* For GUI */
                preLoadedMoves.add("Backtrack end " + "\t"
                        + getNumActions() + "\t"
                        + "Backtrack end " + "\t"
                        + getPathStack() + "\t"
                        + getOutOfBoundsTiles());

                /* Increment counter */
                numBacktrack++;
            } else {        /* Backtracking did not occur. */
                preLoadedMoves.add("No backtrack occurred " + "\t"
                        + getNumActions() + "\t"
                        + "No backtrack occurred " + "\t"
                        + getPathStack() + "\t"
                        + getOutOfBoundsTiles());
            }

            /* Uncomment for debugging. */
            /* System.out.println(path);
            System.out.println("-- backtrack " + numBacktrack + " end --\n"); */
        }
    }

    /**
     * Action corresponding to a scan
     *
     * <p>Under the hood, the actual scanning (that is, getting the designation
     * of the next tile and storing pertinent details into the agent's memory)
     * is embedded in the other methods as part of the implementation of the
     * underlying algorithm. Therefore, the only effect of calling this method
     * is incrementing the number of scans and handling the GUI display. </p>
     */
    public void scan() {
        /* Increment counter. */
        numScan++;

        /* Add to the sequence of actions. */
        preLoadedMoves.add("Scan " + this + "\t"
                        + getNumActions() + "\t"
                        + "Scan: " + getMoveSequence() + "\t"
                        + getPathStack() + "\t"
                        + getOutOfBoundsTiles());

        /* Uncomment for debugging. */
        /* System.out.println("Scan " + numScan); */
    }

    public LinkedList<String> searchForGold(Board b) {
        scan();

        while (b.getSquares()[row][col].getDesignation() != Board.GOLD) {
            char moveCode = moveUntilAllowed(b);

            if (moveCode == GOLD_FOUND) {
                /* Game over */
                break;
            } else if (moveCode == BEACON_FOUND) {
                int distance;
                distance = b.beaconScan(b.getSquares()[row][col]);

                /* Uncomment for debugging. */
                /* System.out.println("---------------> Dist: " + distance); */

                /* Check the cardinal directions. */
                if (distance != 0) {
                    movePerBeacon(b, distance, row, col);

                    /* Game over */
                    break;
                }

                /* If distance is 0, then just ignore the beacon. */
            }

            if (!rotateUntilAllowed(b)) {
                boolean isValidBoard;
                isValidBoard = backtrack(b);

                if (!isValidBoard) {
                    break;
                }
            };
        }

        /* Uncomment for debugging. */
        /* System.out.println(this); */

        /* Uncomment for debugging. */
        /* System.out.println("Final Path: " + path);
        System.out.println("Scanned Out of Bound: " + scannedOutOfBounds); */

        /* Uncomment for debugging. */
        /* System.out.println("Pre-loaded Moves: " + preLoadedMoves); */

        return preLoadedMoves;
    }

    /**
     * Returns a string representation of the number of actions executed by this
     * rational agent (for use in the GUI)
     *
     * @return string representation of the number of actions executed by this
     * rational agent
     */
    public String getNumActions() {
        return  "Number of Moves: " + numMove + "\n" +
                "Number of Scans: " + numScan + "\n" +
                "Number of Rotations: " + numRotate + "\n" +
                "Number of Backtracks: " + numBacktrack + "\n" +
                "Beacon Return Value: " + beaconDistance;
    }

    /**
     * Returns a string representation of the current action of this rational agent
     * (for use in the GUI)
     *
     * @return string representation of the current action of this rational agent
     */
    public String getMoveSequence() {
        return "" + (row + 1) + " " + (col + 1) + " " + front;
    }

    /**
     * Returns a string representation of the path stack (for use in the GUI)
     *
     * @return string representation of the path stack
     */
    public String getPathStack() {
        String pathString = "";

        for (int i = 0; i < currStack.size(); i++)
            pathString += currStack.get(i);

        return pathString;
    }

    /**
     * Returns a string representation of the scanned out-of-bounds tiles
     *
     * @return string representation of the scanned out-of-bounds tiles
     */
    public String getOutOfBoundsTiles() {
        String currOOB;     // string representation of the scanned out-of-bounds tiles
        currOOB = "";

        /* Concatenate the newly scanned out-of-bounds tile. */
        if (scannedOutOfBounds.size() > 0) {
            for (int i = 0; i < scannedOutOfBounds.size(); i++) {
                currOOB += scannedOutOfBounds.get(i).toString() + "\n";
            }
        } else {        // No scanned out-of-bounds tiles yet
            currOOB = "NONE";
        }

        return currOOB;
    }

    /**
     * Returns a string representation of this rational agent
     *
     * <p>The string representation contains the following details in order:</p>
     * <ul>
     *     <li>row-coordinate of its current position</li>
     *     <li>column-coordinate of its current position</li>
     *     <li>direction to where it is currently facing</li>
     * </ul>
     *
     * @return string representation of this rational agent
     */
    @Override
    public String toString() {
        return "Miner : " + row + " " + col + " " + front;
    }
}
