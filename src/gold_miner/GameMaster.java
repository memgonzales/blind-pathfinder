package gold_miner;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class implementing the <b>game master of this pathfinding system</b>, the central class
 * that provides access to all the methods necessary to run the system
 *
 * <p>Note that the row and column numbers in this class (as well as in all other classes
 * responsible for the back-end behavior) are zero-based.</p>
 */
public class GameMaster {
    /* Board consisting of square tiles explored by the miner */
    private Board b;
    /* Rational (smart) miner exploring the board */
    private Miner m;
    /* Nonrational (random) miner exploring the board */
    private RandomMiner rm;

    /* Identifies the speed at which the actions of the miner are shown
    (either step by step or fast) */
    private char systemMode;
    /* Identifies the intelligence level of the agent (miner) */
    private char AIMode;

    /**
     * Character code specifying that the actions of the miner are shown in a step-by-step
     * fashion, that is, the next action is not displayed until the pertinent button is clicked
     * by the user
     */
    public static final char STEP_BY_STEP = 'S';
    /**
     * Character code specifying that the actions of the miner are shown automatically, that is,
     * the next action is displayed after a set duration of pause, without the need
     * for a button click from the user
     */
    public static final char FAST = 'F';

    /**
     * Character code specifying that the agent (miner) is rational, that is, its actions
     * are deliberated via more sophisticated decision-making processes (explained in
     * the technical report accompanying this program)
     */
    public static final char RATIONAL = 'R';
    /**
     * Character code specifying that the agent (miner) is nonrational, that its, its actions
     * are randomly decided as long as it does not result in the miner going out of bounds
     */
    public static final char RANDOM = 'D';

    /**
     * Creates a game master object
     *
     * <p>Since initialization of attributes depends on the choice made by the user,
     * it is deferred and delegated to the setters, particularly to <code>initGame</code>,
     * <code>setAIMode</code>, and <code>setSystemMode</code>.</p>
     */
    public GameMaster() {
        /* Since initialization of attributes depends on the choice made by the user,
        it is deferred and delegated to the setters.
         */
    }

    /**
     * Initializes the attributes of the board given the dimension, the row- and column-
     * coordinates of the beacons, the row- and column-coordinates of the pits, and the
     * row- and column-coordinates of the gold square tile
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
    public void initGame(int dimension, ArrayList<Integer> rBeacon, ArrayList<Integer> cBeacon, ArrayList<Integer> rPit,
                         ArrayList<Integer> cPit, int rGold, int cGold){
        b = new Board(dimension, rBeacon, cBeacon, rPit, cPit, rGold, cGold);

        /* Uncomment for debugging. */
        /* System.out.println(b); */
    }

    /**
     * Sets the intelligence of the agent (miner) depending on the specified mode
     *
     * <p>This method should only be invoked once. Once the mode has been set,
     * then it is already immutable.</p>
     *
     * <p>The following are the possible modes:</p>
     * <ul>
     *     <li><b>Rational</b> - The actions are deliberated via more sophisticated
     *     decision-making processes (explained in the technical report accompanying
     *     this program)</li>
     *     <li><b>Nonrational</b> - The actions are randomly decided as long as it does
     *     not result in the miner going out of bounds</li>
     * </ul>
     *
     * @param mode character code specifying the intelligence of the agent (miner)
     */
    public void setAIMode(char mode) {
        this.AIMode = mode;

        /* Initialize the agent depending on the specified mode */
        switch(mode) {
            case RATIONAL:
                m = new Miner(b);
                break;
            case RANDOM:
                rm = new RandomMiner();
                break;
        }
    }

    /**
     * Sets the speed at which the actions of the miner are shown depending on the
     * specified mode
     *
     * <p>This method should only be invoked once. Once the mode has been set,
     * then it is already immutable.</p>
     *
     * <p>The following are the possible modes:</p>
     * <ul>
     *     <li><b>Step by step</b> - The next action is not displayed until the pertinent
     *     button is clicked by the user</li>
     *     <li><b>Fast</b> - The next action is displayed after a set duration
     *     of pause, without the need for a button click from the user </li>
     * </ul>
     *
     * @param mode character code specifying the speed at which the actions of the miner
     *             are shown
     */
    public void setSystemMode(char mode) {
        this.systemMode = mode;
    }

    /**
     * Returns the character code specifying the intelligence of the agent (miner)
     *
     * <p>The following are the possible modes:</p>
     * <ul>
     *     <li><b>Rational</b> - The actions are deliberated via more sophisticated
     *     decision-making processes (explained in the technical report accompanying
     *     this program)</li>
     *     <li><b>Nonrational</b> - The actions are randomly decided as long as it does
     *     not result in the miner going out of bounds</li>
     * </ul>
     *
     * @return character code specifying the intelligence of the agent (miner)
     */
    public char getAIMode() {
        return AIMode;
    }

    /**
     * Returns the character code specifying the speed at which the actions of the miner
     *
     * <p>The following are the possible modes:</p>
     * <ul>
     *     <li><b>Step by step</b> - The next action is not displayed until the pertinent
     *     button is clicked by the user</li>
     *     <li><b>Fast</b> - The next action is displayed after a set duration
     *     of pause, without the need for a button click from the user </li>
     * </ul>
     *
     * @return character code specifying the speed at which the actions of the miner
     * are shown
     */
    public char getSystemMode() {
        return systemMode;
    }

    /**
     * Returns the board consisting of square tiles explored by the miner
     *
     * @return board consisting of square tiles explored by the miner
     */
    public Board getBoard() {
        return b;
    }

    /**
     * Returns a record of the actions undertaken by the rational agent in searching
     * for the gold square tile
     *
     * @return record of the actions undertaken by the rational agent in searching
     * for the gold square tile
     */
    public LinkedList<String> searchForGold() {
        /* A meaningful record is only created by the rational agent.

        The nonrational agent cannot create a meaningful record since its decision-making
        is based only on random choices.
        */
        switch(AIMode) {
            case RATIONAL:
                return m.searchForGold(b);
        }

        /* Return value for nonrational agent */
        return null;
    }

    /**
     * Action corresponding to a move by the nonrational agent
     */
    public void randomMove() {
        rm.move(b);
    }

    /**
     * Action corresponding to a scan by the nonrational agent
     */
    public void randomScan() {
        rm.scan();
    }

    /**
     * Action corresponding to a rotation by the nonrational agent
     */
    public void randomRotate() {
        rm.rotate();
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
        return rm.getRandom(hi);
    }

    /**
     * Returns the row-coordinate of the current position of the nonrational agent
     *
     * @return row-coordinate of the current position of the nonrational agent
     */
    public int getRandomRow() {
        return rm.getRow();
    }

    /**
     * Returns the column-coordinate of the current position of the nonrational agent
     *
     * @return column-coordinate of the current position of the nonrational agent
     */
    public int getRandomCol() {
        return rm.getCol();
    }

    /**
     * Returns the direction to which the nonrational agent is currently facing
     *
     * @return direction to which the nonrational agent is currently facing
     */
    public char getRandomFront() {
        return rm.getFront();
    }

    /**
     * Returns the direction to which the agent (either nonrational or rational)
     * is currently facing
     *
     * @return direction to which the agent (either nonrational or rational)
     * is currently facing
     */
    public char getFront() {
        switch(AIMode) {
            case RATIONAL:
                /* The rational agent always faces to the right. Facing downwards is equally
                rational; the right direction is arbitrarily chosen.
                 */
                return Miner.RIGHT;
            case RANDOM:
                return getRandomFront();
        }

        /* Should be unreachable code if system is set up properly */
        return '\0';
    }

    /**
     * Returns a string representation of the number of actions executed by the
     * nonrational agent (for use in the GUI)
     *
     * @return string representation of the number of actions executed by the
     * nonrational agent
     */
    public String getRandomNumActions() {
        return rm.getNumActions();
    }

    /**
     * Returns a string representation of the current action of the nonrational agent
     * (for use in the GUI)
     *
     * @return string representation of the current action of the nonrational agent
     */
    public String getRandomMoveSequence() {
        return rm.getMoveSequence();
    }

    /**
     * Returns a string representation of the path stack (for use in the GUI)
     *
     * @return string representation of the path stack
     */
    public String getRandomPathStack() {
        return rm.getPathStack();
    }

    /**
     * Returns a string representation of the scanned out-of-bounds tiles
     *
     * <p>Since the nonrational agent does not have a "memory" or record of scanned tiles,
     * the return value of this function is always <code>"N/A"</code>.</p>
     *
     * @return string representation of the scanned out-of-bounds tiles
     */
    public String getRandomOutOfBoundsTiles() {
        return rm.getOutOfBoundsTiles();
    }
}
