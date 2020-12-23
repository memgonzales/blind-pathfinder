package controller;

import gold_miner.GameMaster;
import gui.ChooseAIBehavior;
import gui.InitScreen;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class providing the <b>controller</b> for the processes related to <b>setting up
 * the board</b>: specifying the dimensions and the locations of the beacon tiles,
 * pit tiles, and the gold tile
 *
 * <p>Note that, although the back-end implementation uses a zero-based index,
 * the front-end (user-facing) follows a one-based index in line with the machine project
 * specifications. </p>
 */
public class InitScreenController implements ActionListener, DocumentListener {
    /* Graphical user interface for the initialization screen */
    private InitScreen scr;
    /* Central class providing access to all the methods necessary to run the system */
    private GameMaster game;

    /* dimension of the board */
    private int dimension;
    /* Row-coordinates of the beacons */
    private ArrayList<Integer> rBeacon;
    /* Column-coordinates of the beacons */
    private ArrayList<Integer> cBeacon;
    /* Row-coordinates of the pits */
    private ArrayList<Integer> rPit;
    /* Column-coordinates of the pits */
    private ArrayList<Integer> cPit;
    /* Row-coordinate of the gold square tile */
    private int rGold;
    /* Column-coordinate of the gold square tile */
    private int cGold;

    /**
     * Creates a controller object with the initialization screen GUI and the game
     * master as parameters
     *
     * @param scr graphical user interface for the initialization screen
     * @param game game master (providing access to all the methods necessary
     *             to run the system)
     */
    public InitScreenController(InitScreen scr, GameMaster game) {
        this.scr = scr;
        this.game = game;

        rBeacon = new ArrayList<Integer>();
        cBeacon = new ArrayList<Integer>();
        rPit = new ArrayList<Integer>();
        cPit = new ArrayList<Integer>();

        scr.setActionListener(this);
        scr.setDocumentListener(this);
    }

    /**
     * Invoked when an action occurs
     *
     * @param e semantic event indicative that a component-defined action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            String inputSize;           // dimension of the board
            String inputGold;           // coordinates of the gold tiles
            String inputBeacons;        // coordinates of the beacons
            String inputPits;           // coordinates of the pits

            inputSize = scr.getInputSize();
            inputGold = scr.getInputGold();
            inputBeacons = scr.getInputBeacons();
            inputPits = scr.getInputPits();

            /* Perform error-catching to handle malformed input. */
            if (checkInput(inputSize, inputGold, inputBeacons, inputPits)) {
                /* Set up the components of the board. */
                game.initGame(dimension, rBeacon, cBeacon, rPit, cPit, rGold, cGold);
                scr.setVisible(false);

                /* Launch the screen for the selection of the AI behavior. */
                ChooseAIBehavior AIScr;
                AIBehaviorController ctrl;

                AIScr = new ChooseAIBehavior();
                ctrl = new AIBehaviorController(AIScr, game, this);
            }
        }
    }

    /**
     * Returns the dimension of the board
     *
     * @return dimension of the board
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Returns the row-coordinates of the beacon tiles
     *
     * @return row-coordinates of the beacon tiles
     */
    public ArrayList<Integer> getRBeacon() {
        return rBeacon;
    }

    /**
     * Returns the column-coordinates of the beacon tiles
     *
     * @return column-coordinates of the beacon tiles
     */
    public ArrayList<Integer> getCBeacon() {
        return cBeacon;
    }

    /**
     * Returns the row-coordinates of the pit tiles
     *
     * @return row-coordinates of the pit tiles
     */
    public ArrayList<Integer> getRPit() {
        return rPit;
    }

    /**
     * Returns the column-coordinates of the pit tiles
     *
     * @return column-coordinates of the pit tiles
     */
    public ArrayList<Integer> getCPit() {
        return cPit;
    }

    /**
     * Returns the row-coordinate of the gold square tile
     *
     * @return row-coordinate of the gold square tile
     */
    public int getRGold() {
        return rGold;
    }

    /**
     * Returns the column-coordinate of the gold square tile
     *
     * @return column-coordinate of the gold square tile
     */
    public int getCGold() {
        return cGold;
    }

    /**
     * Gives notification that an attribute or set of attributes changed
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    /**
     * Gives notification that a potion of the document has been removed
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        scr.setConfirmEnabled(true);
    }

    /**
     * Gives notification that there was an insert into the document
     *
     * @param e document event corresponding to a document change
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        scr.setConfirmEnabled(true);
    }

    /**
     * Returns <code>true</code> if the user input is logical and in conformity with the
     * format recognized by the system; <code>false</code>, otherwise
     *
     * <p>This method also parses the input and displays the appropriate error message
     * as needed, informing the user of the nature of the malformed input. </p>
     *
     * <p>Note that, although the back-end implementation uses a zero-based index,
     * the front-end (user-facing) follows a one-based index in compliance with the machine
     * project specifications. </p>
     *
     * @param inputSize dimension of the board
     * @param inputGold coordinates of the gold square tile
     * @param inputBeacons coordinates of the beacons
     * @param inputPits coordinates of the pits
     * @return <code>true</code> if the user input is logical and in conformity with
     * the format recognized by the system; <code>false</code>, otherwise
     */
    public boolean checkInput(String inputSize, String inputGold, String inputBeacons, String inputPits) {
        /* Inputting the dimensions and the coordinates of the gold square tiles is mandatory. */
        if (inputSize.equals("") || inputGold.equals("")) {
            JOptionPane.showMessageDialog(null, "Insufficient information to construct the board",
                    "Incomplete input", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        /* ERROR CATCHING FOR THE DIMENSIONS OF THE BOARD */
        try {
            dimension = Integer.parseInt(inputSize);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Enter numerical characters only (board size)",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* The machine project specifications indicate that the dimensions must be
        in the interval [8, 64].
         */
        if (!(dimension >= 8 && dimension <= 64)) {
            JOptionPane.showMessageDialog(null, "Board size must be between 8 and 64 (inclusive)",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        /* ERROR CATCHING FOR THE COORDINATES OF THE GOLD SQUARE TILE */
        String[] arrInputGold;          // split coordinates
        arrInputGold = inputGold.split(" ");

        /* Only one coordinate was given. */
        if (arrInputGold.length == 1) {
            JOptionPane.showMessageDialog(null, "Enter the column position (gold) as well",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* Any input after the column-coordinate is ignored. */
        try {
            rGold = Integer.parseInt(arrInputGold[0].replaceAll("\\s", ""));
            cGold = Integer.parseInt(arrInputGold[1].replaceAll("\\s", ""));

            /* Note that the input follows one-based indexing. */
            rGold = rGold - 1;
            cGold = cGold - 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Enter numerical characters only (gold)",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        /* Note that the back-end follows zero-based indexing. */
        if (rGold > dimension - 1 || cGold > dimension - 1 || rGold < 0 || cGold < 0) {
            JOptionPane.showMessageDialog(null, "Out-of-bounds positions (gold)",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        /* ERROR CATCHING FOR THE COORDINATES OF THE BEACONS
        The system allows no beacons to be specified.
         */
        if (!inputBeacons.equals("")) {
            /* Input beacons */
            String[] arrInputBeacons;           // each ordered pair is one element
            arrInputBeacons = inputBeacons.split("\n");

            /* Iterate over the coordinates entered by the user. */
            for (int i = 0; i < arrInputBeacons.length; i++) {
                String[] coordinates;           // split coordinates
                coordinates = arrInputBeacons[i].split(" ");

                /* Only one coordinate was given. */
                if (coordinates.length == 1) {
                    JOptionPane.showMessageDialog(null, "Enter the column position (beacon) as well",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                int r, c;       // parsed row- and column-coordinates

                try {
                    r = Integer.parseInt(coordinates[0].replaceAll("\\s", ""));
                    c = Integer.parseInt(coordinates[1].replaceAll("\\s", ""));

                    /* Note that the input follows one-based indexing. */
                    r = r - 1;
                    c = c - 1;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Enter numerical characters only (beacon)",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                /* Note that the back-end follows zero-based indexing. */
                if (r > dimension - 1 || c > dimension - 1 || r < 0 || c < 0) {
                    JOptionPane.showMessageDialog(null, "Out-of-bounds positions (beacon)",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                /* Ignore if entered coordinates are same as those of gold tile. */
                if (!(r == rGold && c == cGold)) {
                    rBeacon.add(r);
                    cBeacon.add(c);
                }
            }
        }


        /* ERROR CATCHING FOR THE COORDINATES OF THE PITS
        The system allows no pits to be specified.
         */
        if (!inputPits.equals("")) {
            String[] arrInputPits;              // each ordered pair is one element
            arrInputPits = inputPits.split("\n");

            /* Iterate over the coordinates entered by the user. */
            for (int i = 0; i < arrInputPits.length; i++) {
                String[] coordinates;           // split coordinates
                coordinates = arrInputPits[i].split(" ");

                /* Only one coordinate was given. */
                if (coordinates.length == 1) {
                    JOptionPane.showMessageDialog(null, "Enter the column position (pit) as well",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                int r, c;       // parsed row- and column-coordinates

                try {
                    r = Integer.parseInt(coordinates[0].replaceAll("\\s", ""));
                    c = Integer.parseInt(coordinates[1].replaceAll("\\s", ""));

                    /* Note that the input follows one-based indexing. */
                    r = r - 1;
                    c = c - 1;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Enter numerical characters only (pit)",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                /* Note that the back-end follows zero-based indexing. */
                if (r > dimension - 1 || c > dimension - 1 || r < 0 || c < 0) {
                    JOptionPane.showMessageDialog(null, "Out-of-bounds positions (pit)",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                /* Ignore if entered coordinates are same as those of gold tile. */
                if (!(r == rGold && c == cGold)) {
                    rPit.add(r);
                    cPit.add(c);
                }
            }
        }

        /* The board is successfully created. */
        return true;
    }
}
