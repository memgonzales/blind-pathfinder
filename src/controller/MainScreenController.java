package controller;

import gold_miner.Board;
import gold_miner.GameMaster;
import gold_miner.RandomMiner;
import gui.MainScreen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;

/**
 * Class providing the <b>controller</b> for the processes related to the main
 * screen of the system (that is, the board together with the pertinent statistics)
 *
 * <p>Note that, although the back-end implementation uses a zero-based index,
 * the front-end (user-facing) follows a one-based index in line with the machine project
 * specifications. </p>
 */
public class MainScreenController implements ActionListener, WindowListener {
    /* Graphical user interface for the main screen */
    private MainScreen scr;
    /* Central class providing access to all the methods necessary to run the system */
    private GameMaster game;
    /* Controller for the processes related to the board configuration */
    private InitScreenController initCtrl;

    /* Pre-loaded move sequence of the rational agent */
    private LinkedList<String> preLoadedPath;

    public MainScreenController(MainScreen scr, GameMaster game, InitScreenController initCtrl) {
        this.scr = scr;
        this.game = game;
        this.initCtrl = initCtrl;

        scr.setVisible(true);
        scr.setActionListener(this);
        scr.setWindowListener(this);

        /* Pre-load the move sequence of the rational agent. */
        if (game.getAIMode() == GameMaster.RATIONAL) {
            preLoadedPath = game.searchForGold();
        }
    }

    /**
     * Invoked when the user attempts to close the window from the window's system menu
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int confirm;            // response to the confirmation dialog
        confirm = JOptionPane.showConfirmDialog(scr, "Are you sure you want to exit?",
                "Quit", JOptionPane.YES_NO_OPTION);

        /* Exit system only if user response is affirmative. */
        switch(confirm) {
            case JOptionPane.YES_OPTION:
                scr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                break;
            case JOptionPane.NO_OPTION:
                scr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                break;
        }
    }

    /**
     * Invoked when the Window is set to be the active Window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a window has been closed as the result of calling dispose on the window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when a window is no longer the active window
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized to a normal state
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a minimized state
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is made visible
     *
     * @param e window event indicative of a change in status
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when an action occurs
     *
     * @param e semantic event indicative that a component-defined action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Proceed")) {
            /* Check both the AI intelligence and the system mode. */
            if (game.getAIMode() == GameMaster.RATIONAL) {
                if (game.getSystemMode() == GameMaster.FAST) {
                    scr.beginRationalMovement(preLoadedPath);
                } else {
                    rationalStepByStep();
                }
            } else if (game.getAIMode() == GameMaster.RANDOM) {
                if (game.getSystemMode() == GameMaster.FAST) {
                    scr.beginRandomMovement(game);
                } else {
                    randomStepByStep();
                }
            }
        }
    }

    /**
     * Controls the processes related to the step-by-step demonstration
     * of the actions of the rational agent
     */
    public void rationalStepByStep() {
        String details;             // details of the agent's action
        details = preLoadedPath.poll();

        String[] tokDetails;        // tokenized details of the agent's action
        tokDetails = details.split("\t");

        /* The action per se of the agent is the first token. */
        String currAction = tokDetails[0];
        String[] tokCurrAction = currAction.split(" ");

        /* Update the details displayed on the GUI depending on the action. */
        if (tokCurrAction[0].equals("Scan")) {
            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("Move")) {
            scr.moveMiner(Integer.parseInt(tokCurrAction[3]),
                    Integer.parseInt(tokCurrAction[4]), tokCurrAction[5].charAt(0));

            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("Rotate")) {
            scr.rotateMiner(Integer.parseInt(tokCurrAction[3]),
                    Integer.parseInt(tokCurrAction[4]), tokCurrAction[5].charAt(0));

            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("Backtrack")) {
            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("Invalid")) {
            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("Possible")) {
            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);

        } else if (tokCurrAction[0].equals("No")) {
            scr.updateAll(tokDetails[1], tokDetails[2], tokDetails[3], tokDetails[4]);
        }

        /* Disable the proceed button since demonstration is finished. */
        if (preLoadedPath.isEmpty()) {
            scr.setBtnEnabled(false);
        }
    }

    /**
     * Controls the processes related to the step-by-step demonstration
     * of the actions of the rational agent
     */
    public void randomStepByStep() {
        /* Randomly decide on the agent's action. */
        char action;
        action = RandomMiner.POSSIBLE_ACTION[game.getRandom(RandomMiner.POSSIBLE_ACTION.length)];

        /* Update the status of the agent alongside the details displayed on the GUI
        depending on the action.
         */
        switch(action) {
            case RandomMiner.MOVE:
                game.randomMove();
                scr.moveMiner(game.getRandomRow(), game.getRandomCol(), game.getRandomFront());
                scr.updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                        game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                break;

            case RandomMiner.ROTATE:
                game.randomRotate();
                scr.rotateMiner(game.getRandomRow(), game.getRandomCol(), game.getRandomFront());
                scr.updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                        game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                break;

            case RandomMiner.SCAN:
                game.randomScan();
                scr.updateAll(game.getRandomNumActions(), game.getRandomMoveSequence(),
                        game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
                break;
        }

        /* The nonrational agent is susceptible to landing on pit tiles since its
        actions are randomly decided (no intelligent strategy or decision-making is involved).
         */
        if (game.getBoard().getSquares()[game.getRandomRow()][game.getRandomCol()].getDesignation()
                == Board.GOLD) {
            scr.setBtnEnabled(false);

            /* The goal of the agent is achieved (successful game over). */
            scr.updateAll(game.getRandomNumActions(), "The miner was successful!",
                    game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());

        } else if (game.getBoard().getSquares()[game.getRandomRow()][game.getRandomCol()].getDesignation()
                == Board.PIT) {
            scr.setBtnEnabled(false);

            /* The goal of the agent is not achieved (unsuccessful game over). */
            scr.updateAll(game.getRandomNumActions(), "The miner failed.",
                    game.getRandomPathStack(), game.getRandomOutOfBoundsTiles());
        }
    }
}