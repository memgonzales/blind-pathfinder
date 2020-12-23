package controller;

import gold_miner.GameMaster;
import gui.ChooseSystemBehavior;
import gui.MainScreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class providing the <b>controller</b> for the processes related to the <b>selection
 * of the speed at which the actions of the agent are displayed</b>
 */
public class SystemBehaviorController implements ActionListener {
    /* Graphical user interface for the selection of the speed of the display
    of the agent's actions
     */
    private ChooseSystemBehavior scr;
    /* Central class providing access to all the methods necessary to run the system */
    private GameMaster game;
    /* Controller for the processes related to the board configuration */
    private InitScreenController initCtrl;

    /**
     * Creates a controller object with the GUI for the selection of the speed of the
     * display of the agent's actions, the game master, and the controller for the
     * board configuration as parameters
     *
     * @param scr graphical user interface for the selection of the speed of the
     *            display of the agent's actions
     * @param game game master (providing access to all the methods necessary
     *             to run the system)
     * @param initCtrl controller for the processes related to the board configuration
     */
    public SystemBehaviorController(ChooseSystemBehavior scr, GameMaster game, InitScreenController initCtrl) {
        this.scr = scr;
        this.game = game;
        this.initCtrl = initCtrl;

        scr.setVisible(true);
        scr.setActionListener(this);
    }

    /**
     * Invoked when an action occurs
     *
     * @param e semantic event indicative that a component-defined action occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /* The speed can either be step by step or fast. */
        if (e.getActionCommand().equals("Step by Step")) {
            game.setSystemMode(GameMaster.STEP_BY_STEP);
            scr.setVisible(false);

            /* Launch the main screen. */
            MainScreen mainScr;
            MainScreenController ctrl;

            mainScr = new MainScreen(initCtrl.getDimension(), initCtrl.getRBeacon(), initCtrl.getCBeacon(),
                    initCtrl.getRPit(), initCtrl.getCPit(), initCtrl.getRGold(), initCtrl.getCGold(),
                    game.getFront());
            ctrl = new MainScreenController(mainScr, game, initCtrl);

        } else if (e.getActionCommand().equals("Fast")) {
            game.setSystemMode(GameMaster.FAST);
            scr.setVisible(false);

            /* Launch the main screen. */
            MainScreen mainScr;
            MainScreenController ctrl;

            mainScr = new MainScreen(initCtrl.getDimension(), initCtrl.getRBeacon(), initCtrl.getCBeacon(),
                    initCtrl.getRPit(), initCtrl.getCPit(), initCtrl.getRGold(), initCtrl.getCGold(),
                    game.getFront());
            ctrl = new MainScreenController(mainScr, game, initCtrl);
        }
    }
}
