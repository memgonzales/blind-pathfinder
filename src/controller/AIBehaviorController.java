package controller;

import gold_miner.GameMaster;
import gui.ChooseAIBehavior;
import gui.ChooseSystemBehavior;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class providing the <b>controller</b> for the processes related to the <b>selection
 * of the intelligence level of the agent</b>
 */
public class AIBehaviorController implements ActionListener {
    /* Graphical user interface for the selection of intelligence level */
    private ChooseAIBehavior scr;
    /* Central class providing access to all the methods necessary to run the system */
    private GameMaster game;
    /* Controller for the processes related to the board configuration */
    private InitScreenController initCtrl;

    /**
     * Creates a controller object with the GUI for the selection of intelligence level,
     * the game master, and the controller for the board configuration as parameters
     *
     * @param scr graphical user interface for the selection of intelligence level
     * @param game game master (providing access to all the methods necessary
     *             to run the system)
     * @param initCtrl controller for the processes related to the board configuration
     */
    public AIBehaviorController(ChooseAIBehavior scr, GameMaster game, InitScreenController initCtrl) {
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
        /* The intelligence level can either be random or rational. */
        if (e.getActionCommand().equals("Random")) {
            game.setAIMode(GameMaster.RANDOM);
            scr.setVisible(false);

            /* Launch the screen for the selection of the speed at which the
            agent's actions are displayed.
             */
            ChooseSystemBehavior chooseScr;
            SystemBehaviorController ctrl;

            chooseScr = new ChooseSystemBehavior();
            ctrl = new SystemBehaviorController(chooseScr, game, initCtrl);

        } else if (e.getActionCommand().equals("Smart")) {
            game.setAIMode(GameMaster.RATIONAL);
            scr.setVisible(false);

            /* Launch the screen for the selection of the speed at which the
            agent's actions are displayed.
             */
            ChooseSystemBehavior chooseScr;
            SystemBehaviorController ctrl;

            chooseScr = new ChooseSystemBehavior();
            ctrl = new SystemBehaviorController(chooseScr, game, initCtrl);
        }
    }
}
