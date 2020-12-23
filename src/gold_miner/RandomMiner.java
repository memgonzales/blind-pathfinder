package gold_miner;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class implementing the <b>nonrational agent</b> whose actions are randomly decided
 * as long as it does not result in the miner going out of bounds
 *
 * <p>Note that the row and column numbers in this class (as well as in all other classes
 * responsible for the back-end behavior) are zero-based.</p>
 */
public class RandomMiner {
    /* Row-coordinate of the position of this miner */
    private int row;
    /* Column-coordinate of the position of this miner */
    private int col;

    /* Direction to which this miner is facing */
    private char front;

    /* Can either be a success (this miner reached the gold square tile)
    or a failure (this miner fell on a pit)
    */
    private char results;

    /* Stack storing the square tiles that will be part of the final path to the gold tile */
    private Stack<Square> path;
    /* Record of the square tiles currently comprising the path stack (for use in the GUI) */
    private ArrayList<String> currPath;
    /* String representation of the current action of this agent (for use in the GUI) */
    private String currMove;

    /* Number of rotations */
    private static int numRotate = 0;
    /* Number of scans */
    private static int numScan = 0;
    /* Number of moves */
    private static int numMove = 0;

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
     * Character code corresponding to a move by this miner
     */
    public static final char MOVE = 'M';
    /**
     * Character code corresponding to a scan by this miner
     */
    public static final char SCAN = 'S';
    /**
     * Character code corresponding to a rotate by this miner
     */
    public static final char ROTATE = 'R';

    /**
     * Character code indicating that this nonrational miner reached the gold square tile
     */
    public static final char SUCCESS = 'S';
    /**
     * Character code indicating that this nonrational miner fell on a pit
     */
    public static final char FAIL = 'F';

    /**
     * List of the possible directions to which this miner can face (that is, the four
     * cardinal directions)
     */
    public static final char[] POSSIBLE_FRONT = {UP, RIGHT, LEFT, DOWN};
    /**
     * List of the possible actions that this miner can execute (move, scan, and rotate)
     */
    public static final char[] POSSIBLE_ACTION = {MOVE, SCAN, ROTATE};

    /**
     * Creates a nonrational agent whose actions are randomly decided as long as it does
     * not result in this miner going out of bounds
     */
    public RandomMiner() {
        row = 0;
        col = 0;

        path = new Stack<Square>();
        currPath = new ArrayList<String>();

        /* Randomize the direction to which the miner is facing. */
        front = POSSIBLE_FRONT[getRandom(POSSIBLE_FRONT.length)];
        path.add(new Square(0, 0, 'E'));
        currPath.add(new Square(0, 0, 'E').toString() + "\n");
    }

    /**
     * Returns the row-coordinate of the current position of this miner
     *
     * @return row-coordinate of the current position of this miner
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column-coordinate of the current position of this miner
     *
     * @return column-coordinate of the current position of this miner
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
     * Action corresponding to a rotation by this nonrational agent
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

        /* Uncomment for debugging. */
        /* System.out.println("Rotate " + numRotate + ": " + this); */

        /* Handle GUI updates. */
        currMove = "Rotate: " + (row + 1) + " " + (col + 1) + " " + front;
    }

    /**
     * Action corresponding to a move by this nonrational agent
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
        currPath.add(b.getSquares()[row][col].toString() + "\n");

        /* Increment counter. */
        numMove++;

        /* Uncomment for debugging. */
        /* System.out.println("Move " + numMove + ": " + this); */

        /* Handle GUI updates. */
        currMove = "Move: " + (row + 1) + " " + (col + 1) + " " + front;
    }

    /**
     * Action corresponding to a scan by this nonrational agent
     *
     * <p>Per the machine project specifications, this miner can only scan an adjacent
     * tile in the direction to where it is facing. A scan changes neither the position
     * of this miner nor the current orientation; its effect is limited to determining
     * the designation of the tile in front of the miner.
     */
    public void scan() {
        /* Increment counter. */
        numScan++;

        /* Uncomment for debugging. */
        /* System.out.println("Scan " + numScan); */

        /* Handle GUI updates. */
        currMove = "Scan: " + (row + 1) + " " + (col + 1) + " " + front;
    }

    /**
     * Returns a random integer from 0 (inclusive) to the set upper bound (exclusive)
     * for use in the naive decision-making of the nonrational agent
     *
     * <p>Technically, the integer generated is pseudorandom since the library used
     * for its generation is Java's <code>ThreadLocalRandom</code>. Nevertheless,
     * this is sufficient for the purposes of this system. </p>
     *
     * @param hi exclusive upper bound
     * @return random integer from 0 (inclusive) to the set upper bound (exclusive)
     */
    public int getRandom(int hi) {
        return ThreadLocalRandom.current().nextInt(0, hi);
    }

    /**
     * Returns a string representation of the number of actions executed by this
     * nonrational agent (for use in the GUI)
     *
     * <p>Note that this nonrational agent does not perform any backtracking in the
     * technical/systematic sense of the word. Moreover, it does not take advantage
     * of the return value of beacon square tiles. </p>
     *
     * @return string representation of the number of actions executed by this
     * nonrational agent
     */
    public String getNumActions() {
        return  "Number of Moves: " + numMove + "\n" +
                "Number of Scans: " + numScan + "\n" +
                "Number of Rotations: " + numRotate + "\n" +
                "Number of Backtracks: N/A \n" +
                "Beacon Return Value: Irrelevant ";
    }

    /**
     * Returns a string representation of the current action of this nonrational agent
     * (for use in the GUI)
     *
     * @return string representation of the current action of this nonrational agent
     */
    public String getMoveSequence() {
        return currMove;
    }

    /**
     * Returns a string representation of the path stack (for use in the GUI)
     *
     * @return string representation of the path stack
     */
    public String getPathStack() {
        String pathString = "";

        for (int i = 0; i < currPath.size(); i++)
            pathString += currPath.get(i);

        return pathString;
    }

    /**
     * Returns a string representation of the scanned out-of-bounds tiles
     *
     * <p>Since this nonrational agent does not have a "memory" or record of scanned tiles,
     * the return value of this function is always <code>"N/A"</code>.</p>
     *
     * @return string representation of the scanned out-of-bounds tiles
     */
    public String getOutOfBoundsTiles() {
        return "N/A";
    }

    /**
     * Returns a string representation of this nonrational agent
     *
     * <p>The string representation contains the following details in order:</p>
     * <ul>
     *     <li>row-coordinate of its current position</li>
     *     <li>column-coordinate of its current position</li>
     *     <li>direction to where it is currently facing</li>
     * </ul>
     *
     * @return string representation of this nonrational agent
     */
    @Override
    public String toString() {
        return "Random Miner : " + row + " " + col + " " + front;
    }
}
