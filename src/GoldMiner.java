import controller.InitScreenController;
import gold_miner.GameMaster;
import gui.InitScreen;

/**
 * Class for activating the <b>Gold Miner system</b>
 */
public class GoldMiner {
    /**
     * Empty constructor
     */
    public GoldMiner() {

    }

    /**
     * Activates the Gold Miner system
     *
     * @param args array of command-line arguments
     */
    public static void main(String[] args) {
        GameMaster game;
        InitScreen scr;
        InitScreenController ctrl;

        game = new GameMaster();
        scr = new InitScreen();
        ctrl = new InitScreenController(scr, game);
    }
}
