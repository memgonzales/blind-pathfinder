package gui;

import gold_miner.Board;
import gold_miner.GameMaster;
import gold_miner.RandomMiner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.Timer;

/**
 * Class implementing the <b>main window</b> of the system, displaying the board explored by
 * the miner alongside important details regarding the agent's behavior (such as the counters
 * of its actions and the contents of its "memory" or records)
 */
public class MainScreen extends JFrame {
    /* Graphical components related to the board on which the miner and the tiles are located */
    private JPanel pBoard;
    private JScrollPane scrBoard;
    private JPanel[][] pSpaces;

    /* Graphical components related to the counters of the agent's actions and its memory */
    private JLabel lblCounts;
    private JTextArea taCounts;
    private JScrollPane scrCounts;
    private JLabel lblMoveSequence;
    private JTextArea taMoveSequence;
    private JScrollPane scrMoveSequence;
    private JLabel lblPathStack;
    private JTextArea taPathStack;
    private JScrollPane scrPathStack;
    private JLabel lblOutOfBounds;
    private JTextArea taOutOfBounds;
    private JScrollPane scrOutOfBounds;

    /* Button for proceeding to the next step (step by step) or starting the pathfinding (fast) */
    private JButton btnNextStep;

    /* Graphical components related to the specially designated tiles */
    private ArrayList<JLabel> beacons;
    private ArrayList<JLabel> pits;
    private JLabel lblGold;

    /* JLabels for the rotation sprites of the miner */
    private JLabel lblMinerDown;
    private JLabel lblMinerLeft;
    private JLabel lblMinerRight;
    private JLabel lblMinerUp;

    /* Swing timer for threading and animation */
    private Timer timer;

    /**
     * Creates the main window of the system, displaying the board explored by
     * the miner alongside important details regarding the agent's behavior (such as the
     * counters of its actions and the contents of its "memory" or records)
     *
     * @param size dimension of the board
     * @param beaconXCoors row numbers of the beacons
     * @param beaconYCoors column numbers of the beacons
     * @param pitXCoors row numbers of the pits
     * @param pitYCoors column numbers of the pits
     * @param goldXCoor row number of the gold tile
     * @param goldYCoor column number of the gold tile
     * @param front direction to which the miner is facing
     */
    public MainScreen(int size, ArrayList<Integer> beaconXCoors, ArrayList<Integer> beaconYCoors,
                      ArrayList<Integer> pitXCoors, ArrayList<Integer> pitYCoors,
                      int goldXCoor, int goldYCoor, char front) {

        /* "Gold Miner" is used as window title */
        super("Gold Miner");

        /* Use Border layout */
        setLayout(new BorderLayout());

        /* init() is called to initialize the elements of the window */
        init(size, beaconXCoors, beaconYCoors, pitXCoors, pitYCoors,
                goldXCoor, goldYCoor, front);

        /* Additional formatting methods for the window are executed */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Initializes the elements comprising this window
     *
     * @param size dimension of the board
     * @param beaconXCoors row numbers of the beacons
     * @param beaconYCoors column numbers of the beacons
     * @param pitXCoors row numbers of the pits
     * @param pitYCoors column numbers of the pits
     * @param goldXCoor row number of the gold tile
     * @param goldYCoor column number of the gold tile
     * @param front direction to which the miner is facing
     */
    private void init (int size, ArrayList<Integer> beaconXCoors, ArrayList<Integer> beaconYCoors,
                       ArrayList<Integer> pitXCoors, ArrayList<Integer> pitYCoors,
                       int goldXCoor, int goldYCoor, char front) {
        JPanel pCenter;     // panel holding the board representation
        JPanel pRight;      // panel holding the miner details and PROCEED button
        JPanel pTextAreas;  // panel holding the text fields for the miner details

        /* The center panel is created with the Box layout */
        pCenter = new JPanel();
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.X_AXIS));

        /* The pBoard panel is created with the GridBag layout */
        pBoard = new JPanel();
        pBoard.setLayout(new GridBagLayout());
        pBoard.setBackground(new Color(181, 101, 29));

        /* initBoard() is called to initialize the elements of the board representation */
        initBoard(size, beaconXCoors, beaconYCoors, pitXCoors, pitYCoors,
        goldXCoor, goldYCoor, front);

        /* The pBoard panel is stored in a scroll pane, which is added to the window */
        scrBoard = new JScrollPane(pBoard);
        pCenter.add(scrBoard);

        /* The right panel is created with the Border layout */
        pRight = new JPanel();
        pRight.setLayout(new BorderLayout());

        /* The pTextAreas panel is created with the Box layout */
        pTextAreas = new JPanel();
        pTextAreas.setLayout(new BoxLayout(pTextAreas, BoxLayout.Y_AXIS));

        /* A label for the miner details is formatted and added to the pTextAreas panel */
        lblCounts = new JLabel("Miner Details");
        lblCounts.setBorder(new EmptyBorder(10, 10, 0, 10));
        pTextAreas.add(lblCounts);

        /* A text field for storing the miner details is formatted, stored in a scroll pane,
        and added to the pTextAreas panel
        */
        taCounts = new JTextArea();
        taCounts.setEditable(false);
        taCounts.setLineWrap(true);
        taCounts.setWrapStyleWord(true);
        taCounts.setFont(new Font("Bold", Font.BOLD, 12));
        scrCounts = new JScrollPane(taCounts);
        scrCounts.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrCounts.setPreferredSize(new Dimension(300, 120));
        pTextAreas.add(scrCounts);

        /* A label for the action sequence is formatted and added to the pTextAreas panel */
        lblMoveSequence = new JLabel("Action Sequence");
        pTextAreas.add(lblMoveSequence);

        /* A text field for storing the action sequence is formatted, stored in a scroll pane,
        and added to the pTextAreas panel
        */
        taMoveSequence = new JTextArea();
        taMoveSequence.setEditable(false);
        taMoveSequence.setLineWrap(true);
        taMoveSequence.setWrapStyleWord(true);
        scrMoveSequence = new JScrollPane(taMoveSequence);
        scrMoveSequence.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrMoveSequence.setPreferredSize(new Dimension(300, 180));
        pTextAreas.add(scrMoveSequence);

        /* A label for the path stack is formatted and added to the pTextAreas panel */
        lblPathStack = new JLabel("Path Stack");
        pTextAreas.add(lblPathStack);

        /* A text field for storing the path stack is formatted, stored in a scroll pane,
        and added to the pTextAreas panel
        */
        taPathStack = new JTextArea();
        taPathStack.setEditable(false);
        taPathStack.setLineWrap(true);
        taPathStack.setWrapStyleWord(true);
        scrPathStack = new JScrollPane(taPathStack);
        scrPathStack.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrPathStack.setPreferredSize(new Dimension(300, 180));
        pTextAreas.add(scrPathStack);

        /* A label for the out of bounds tiles is formatted and added to the pTextAreas panel */
        lblOutOfBounds = new JLabel("Out Of Bounds Tiles");
        pTextAreas.add(lblOutOfBounds);

        /* A text field for storing the out of bounds tiles is formatted, stored in a scroll pane,
        and added to the pTextAreas panel
        */
        taOutOfBounds = new JTextArea();
        taOutOfBounds.setEditable(false);
        taOutOfBounds.setLineWrap(true);
        taOutOfBounds.setWrapStyleWord(true);
        scrOutOfBounds = new JScrollPane(taOutOfBounds);
        scrOutOfBounds.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrOutOfBounds.setPreferredSize(new Dimension(300, 180));
        pTextAreas.add(scrOutOfBounds);

        /* The pTextAreas panel is added to the right panel */
        pRight.add(pTextAreas, BorderLayout.CENTER);

        /* The PROCEED button is added to the right panel */
        btnNextStep = new JButton("Proceed");
        pRight.add(btnNextStep, BorderLayout.SOUTH);

        /* The center and right panels are positioned on the Border layout of the window */
        add(pCenter, BorderLayout.CENTER);
        add(pRight, BorderLayout.EAST);
    }

    /**
     * Initializes the elements comprising the board
     *
     * @param size dimension of the board
     * @param beaconXCoors row numbers of the beacons
     * @param beaconYCoors column numbers of the beacons
     * @param pitXCoors row numbers of the pits
     * @param pitYCoors column numbers of the pits
     * @param goldX row number of the gold tile
     * @param goldY column number of the gold tile
     * @param front direction to which the miner is facing
     */
    private void initBoard(int size, ArrayList<Integer> beaconXCoors, ArrayList<Integer> beaconYCoors,
                           ArrayList<Integer> pitXCoors, ArrayList<Integer> pitYCoors,
                           int goldX, int goldY, char front) {
        GridBagConstraints c; // GridBagConstraints object used to construct the board grid
        Dimension d;          // Dimension object storing the dimensions of each grid tile

        ArrayList<Integer> beaconsXCoors; // list holding the row numbers of the beacons
        ArrayList<Integer> beaconsYCoors; // list holding the column numbers of the beacons
        ArrayList<Integer> pitsXCoors;    // list holding the row numbers of the pits
        ArrayList<Integer> pitsYCoors;    // list holding the column numbers of the pits

        int goldXCoor; // row number of the gold
        int goldYCoor; // column number of the gold

        /* The local variables are initialized with the values of the passed parameters */
        beaconsXCoors = beaconXCoors;
        beaconsYCoors = beaconYCoors;
        pitsXCoors = pitXCoors;
        pitsYCoors = pitYCoors;
        goldXCoor = goldX;
        goldYCoor = goldY;

        /* The lists holding the beacon and pit images are instantiated */
        beacons = new ArrayList<>();
        pits = new ArrayList<>();

        /* Counter variables for creating and formatting the board spaces */
        int i, j;

        /* The GridBagConstraints and Dimension objects are instantiated */
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        d = new Dimension (90, 90);

        /* The pSpaces panel for storing the board grid is instantiated */
        pSpaces = new JPanel[size][size];

        /* A two-dimensional array of panels is formatted and added to the pSpaces panel, corresponding
        to the size of the board
        */
        for (i = 0; i < pSpaces.length; i++) {
            for (j = 0; j < pSpaces[i].length; j++) {
                pSpaces[i][j] = new JPanel(new BorderLayout());
                pSpaces[i][j].setPreferredSize(d);

                pSpaces[i][j].setBackground(new Color(1, 1, 1, 1));
                pSpaces[i][j].setBorder(BorderFactory.createLineBorder(new Color(1, 1, 1), 1));
                pBoard.add(pSpaces[i][j], c);

                c.gridx = c.gridx + 1;
            }

            c.gridx = 0;
            c.gridy = c.gridy + 1;
        }

        /* For every beacon on the board, a beacon image is added to the pertinent list */
        for (i = 0; i < beaconsXCoors.size(); i++) {
            beacons.add(setImage("assets/beacon.png"));
        }

        /* Beacon images are added to the appropriate tiles and their background colors are changed to green */
        for (i = 0; i < beaconsXCoors.size(); i++) {
            pSpaces[beaconsXCoors.get(i)][beaconsYCoors.get(i)].setBackground(Color.green);
            pSpaces[beaconsXCoors.get(i)][beaconsYCoors.get(i)].add(beacons.get(i), BorderLayout.NORTH);
        }

        /* For every pit on the board, a pit image is added to the pertinent list */
        for (i = 0; i < pitsXCoors.size(); i++) {
            pits.add(setImage("assets/pit.png"));
        }

        /* Pit images are added to the appropriate tiles and their background colors are changed to gray */
        for (i = 0; i < pitsXCoors.size(); i++) {
            pSpaces[pitsXCoors.get(i)][pitsYCoors.get(i)].setBackground(Color.lightGray);
            pSpaces[pitsXCoors.get(i)][pitsYCoors.get(i)].add(pits.get(i), BorderLayout.NORTH);
        }

        /* A gold image is instantiated and added to the appropriate tile, with its background color
        changed to orange
        */
        lblGold = setImage("assets/gold.png");
        pSpaces[goldXCoor][goldYCoor].setBackground(Color.orange);
        pSpaces[goldXCoor][goldYCoor].add(lblGold, BorderLayout.NORTH);

        /* Images for the miner facing each of the four directions are instantiated */
        lblMinerDown = setImage("assets/minerDown.png");
        lblMinerRight = setImage("assets/minerRight.png");
        lblMinerLeft = setImage("assets/minerLeft.png");
        lblMinerUp = setImage("assets/minerUp.png");

        /* As the first tile is always visited (since the miner begins at said tile), its background
        color is changed to dark brown
        */
        pSpaces[0][0].setBackground(new Color(101, 67, 33));

        /* Depending on the initial direction of the miner, the appropriate image is added to the
        first tile
        */
        if (front == 'R')
            pSpaces[0][0].add(lblMinerRight, BorderLayout.SOUTH);
        else if (front == 'D')
            pSpaces[0][0].add(lblMinerDown, BorderLayout.SOUTH);
        else if (front == 'L')
            pSpaces[0][0].add(lblMinerLeft, BorderLayout.SOUTH);
        else if (front == 'U')
            pSpaces[0][0].add(lblMinerUp, BorderLayout.SOUTH);
    }

    /**
     * Returns a scaled version of an image given its path
     *
     * @param imagePath path to the image to be scaled
     * @return scaled version of the image
     */
    private JLabel setImage (String imagePath) {
        /* Stores the original image */
        ImageIcon imageOrig;
        /* Stores a scaled version of the image */
        Image imageScaled;
        /* Stores a formatted version of the image to be displayed on the window */
        JLabel lblImage;

        /* The original image is retrieved and scaled */
        imageOrig = new ImageIcon(getClass().getResource(imagePath));
        imageScaled = imageOrig.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);

        /* The scaled image is converted to a JLabel, further formatted, and returned */
        lblImage = new JLabel(new ImageIcon(imageScaled));
        lblImage.setBackground(Color.white);
        lblImage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblImage.setBorder(new EmptyBorder(1, 1, 1, 1));
        return lblImage;
    }

    /**
     * Updates the display depending on the action executed by the rational agent
     *
     * @param preLoadedPath pre-loaded sequence of actions executed by the rational agent
     */
    public void beginRationalMovement(LinkedList<String> preLoadedPath) {

        /* As the fast view runs automatically, the user does not need to press the PROCEED button */
        setBtnEnabled(false);

        /* An action listener for iterating through the pre-loaded action sequence is instantiated */
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /* Once an action event is fired, the succeeding action on the pre-loaded path is
                retrieved and parsed
                */
                String details = preLoadedPath.poll();
                String[] tokDetails = details.split("\t");

                /* The segment of the pre-loaded path node storing the action command is further
                parsed
                */
                String currAction = tokDetails[0];
                String[] tokCurrAction = currAction.split(" ");

                /* For each of the possible action sequence codes, updateAll() is called to update
                the miner details on the right panel of the window
                */
                if (tokCurrAction[0].equals("Scan")) {
                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                } else if (tokCurrAction[0].equals("Move")) {
                    /* If the "move" sequence code is retrieved, the miner sprite on the board is
                    moved accordingly
                    */
                    moveMiner(Integer.parseInt(tokCurrAction[3]),
                            Integer.parseInt(tokCurrAction[4]), tokCurrAction[5].charAt(0));

                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                }  else if (tokCurrAction[0].equals("Rotate")) {
                    /* If the "rotate" sequence code is retrieved, the miner sprite on the board is
                    rotated accordingly
                    */
                    rotateMiner(Integer.parseInt(tokCurrAction[3]),
                            Integer.parseInt(tokCurrAction[4]), tokCurrAction[5].charAt(0));

                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                } else if (tokCurrAction[0].equals("Backtrack")) {
                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                } else if (tokCurrAction[0].equals("Invalid")) {
                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                } else if (tokCurrAction[0].equals("Possible")) {
                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

                } else if (tokCurrAction[0].equals("No")) {
                    updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);
                }

                /* Once the pre-loaded path is empty, the timer stops firing action events */
                if (preLoadedPath.isEmpty()) {
                    timer.stop();
                }
            }
        };

        /* A Timer object is created that fires an action event every 300 milliseconds to facilitate
        the fast view
        */
        timer = new Timer(300, a);
        timer.start();
    }

    /**
     * Updates the display depending on the random action chosen by the nonrational agent
     *
     * @param game class providing access to all the methods necessary to run the system
     */
    public void beginRandomMovement(GameMaster game) {

        /* As the fast view runs automatically, the user does not need to press the PROCEED button*/
        setBtnEnabled(false);

        /* An action listener for retrieving the next action of the random agent is instantiated */
        ActionListener a = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char action;    // the code for the action of the random agent
                char results;   // the code for the type of terminal phase the random agent triggers

                /* Once an action event is fired, the succeeding action of the random agent is retrieved */
                action = RandomMiner.POSSIBLE_ACTION[game.getRandom(RandomMiner.POSSIBLE_ACTION.length)];

                /* For each of the possible action sequence codes, updateAll() is called to update
                the miner details on the right panel of the window
                */
                switch(action) {
                    case RandomMiner.MOVE:
                        /* If the "move" sequence code is retrieved, the miner sprite on the board is
                        moved accordingly
                        */
                        game.randomMove();
                        moveMiner(game.getRandomRow(), game.getRandomCol(), game.getRandomFront());

                        updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                                game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                        break;

                    case RandomMiner.ROTATE:
                        /* If the "rotate" sequence code is retrieved, the miner sprite on the board is
                        rotated accordingly
                        */
                        game.randomRotate();
                        rotateMiner(game.getRandomRow(), game.getRandomCol(), game.getRandomFront());

                        updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                                game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                        break;

                    case RandomMiner.SCAN:
                        game.randomScan();
                        updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                                game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                        break;
                }

                /* If the miner reaches the gold tile; the terminal success phase is triggered;
                further actions are ceased and the string "The miner was successful!" is appended to the
                action sequence text field
                */
                if (game.getBoard().getSquares()[game.getRandomRow()][game.getRandomCol()].getDesignation()
                        == Board.GOLD) {
                    results = RandomMiner.SUCCESS;
                    timer.stop();
                    updateAll(game.getRandomNumActions(), "The miner was successful!",
                            game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());

                /* If the miner reaches a pit tile; the terminal fail phase is triggered;
                further actions are ceased and the string "The miner failed." is appended to the
                action sequence text field
                */
                } else if (game.getBoard().getSquares()[game.getRandomRow()][game.getRandomCol()].getDesignation()
                        == Board.PIT) {
                    results = RandomMiner.FAIL;
                    timer.stop();
                    updateAll(game.getRandomNumActions(), "The miner failed.",
                            game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());

                }
            }
        };

        /* A Timer object is created that fires an action event every 300 milliseconds to facilitate
        the fast view
        */
        timer = new Timer(300, a);
        timer.start();
    }

    /**
     * Updates the display to reflect a move by the miner
     *
     * @param x row number of the newly occupied square tile
     * @param y column number of the newly occupied square tile
     * @param dir direction to which the miner is facing
     */
    public void moveMiner(int x, int y, char dir) {

        /* Depending on the direction the miner is facing, the appropriate miner sprite is moved to its
        new location
        */
        if (dir == 'R')
            pSpaces[x][y].add(lblMinerRight, BorderLayout.SOUTH);
        else if (dir == 'D')
            pSpaces[x][y].add(lblMinerDown, BorderLayout.SOUTH);
        else if (dir == 'L')
            pSpaces[x][y].add(lblMinerLeft, BorderLayout.SOUTH);
        else if (dir == 'U')
            pSpaces[x][y].add(lblMinerUp, BorderLayout.SOUTH);

        /* As the miner visits a tile when it moves, the background color of the tile is changed to
        dark brown
        */
        pSpaces[x][y].setBackground(new Color(101, 67, 33));

        /* The board scroll pane is repainted to reflect the changes in the graphical user interface */
        scrBoard.repaint();
    }

    /**
     * Updates the display to reflect a (clockwise) rotation by the miner
     *
     * @param x row number of the square tile currently occupied by the miner
     * @param y column number of the square tile currently occupied by the miner
     * @param dir direction to which the miner is facing
     */
    public void rotateMiner(int x, int y, char dir) {

        /* Depending on the direction the miner is facing, its sprite is removed from the board,
        the board is revalidated to reflect the changes in the graphical user interface, and a
        new sprite reflecting the 90-degree clockwise rotation is added to the same position
        */
        if (dir == 'R') {
            pSpaces[x][y].remove(lblMinerUp);
            scrBoard.revalidate();
            pSpaces[x][y].add(lblMinerRight, BorderLayout.SOUTH);
        } else if (dir == 'D') {
            pSpaces[x][y].remove(lblMinerRight);
            scrBoard.revalidate();
            pSpaces[x][y].add(lblMinerDown, BorderLayout.SOUTH);
        } else if (dir == 'L') {
            pSpaces[x][y].remove(lblMinerDown);
            scrBoard.revalidate();
            pSpaces[x][y].add(lblMinerLeft, BorderLayout.SOUTH);
        } else if (dir == 'U') {
            pSpaces[x][y].remove(lblMinerLeft);
            scrBoard.revalidate();
            pSpaces[x][y].add(lblMinerUp, BorderLayout.SOUTH);
        }

        /* The board scroll pane is repainted to reflect the changes in the graphical user interface */
        scrBoard.repaint();
    }

    /**
     * Sets the action listener for the button in this graphical user interface
     *
     * @param listener action listener receiving action events
     */
    public void setActionListener(ActionListener listener) {
        btnNextStep.addActionListener(listener);
    }

    /**
     * Sets the window listener for this graphical user interface
     *
     * @param listener window listener receiving window events
     */
    public void setWindowListener(WindowListener listener) {
        addWindowListener(listener);
    }

    /**
     * Enables (or disables) the proceed button depending on the <code>
     * boolean</code> parameter passed
     *
     * @param b <code>true</code> if the PROCEED button is to be enabled;
     *          <code>false</code>, otherwise
     */
    public void setBtnEnabled(boolean b) {
        btnNextStep.setEnabled(b);
    }

    /**
     * Updates the text log to display the number of actions executed by the miner
     *
     * @param actions string representation of the number of actions executed the miner
     */
    public void updateNumActions(String actions) {
        taCounts.setText(actions);
    }

    /**
     * Updates the text log to display the action sequence executed by the miner
     *
     * @param sequence string representation of the latest entry in the miner's move sequence
     */
    public void updateMoveSequence(String sequence) {
        taMoveSequence.append(sequence + "\n");
    }

    /**
     * Updates the text log to display the miner's path stack
     *
     * @param path string representation of the path stack
     */
    public void updatePathStack(String path) {
        taPathStack.setText(path);
    }

    /**
     * Updates the text log to display the out-of-bounds tiles scanned by the miner
     *
     * @param tiles string representation of the out-of-bounds tiles scanned by the miner
     */
    public void updateOutOfBoundsTiles(String tiles) {
        taOutOfBounds.setText(tiles);
    }

    /**
     * Updates the text log to display the consolidated details pertinent to the behavior
     * of the agent, such as the counters of its actions and the contents of its "memory"
     * or records
     *
     * @param one counters of the agent's actions
     * @param two string representation of the latest entry in the miner's move sequence
     * @param three string representation of the path stack
     * @param four string representation of the out-of-bounds tiles scanned by the miner
     */
    public void updateAll(String one, String two, String three, String four) {
        /* The methods for updating the individual text logs are called and passed the
        appropriate text entries
        */
        updateNumActions(one);
        updateMoveSequence(two);
        updatePathStack(three);
        updateOutOfBoundsTiles(four);
    }
}

