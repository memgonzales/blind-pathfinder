package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class implementing a window for <b>setting up the board</b>: specifying the board dimensions
 * and locations of the beacon tiles, pit tiles, and the gold tile
 */
public class InitScreen extends JFrame {
    /* Confirmatory button */
    private JButton btnConfirm;

    /* Labels storing prompts for the input areas */
    private JLabel lblInputSize;
    private JLabel lblInputGold;
    private JLabel lblInputBeacons;
    private JLabel lblInputPits;

    /* Scrollable panes for the input areas related to the locations of the beacon and pits */
    private JScrollPane scrInputBeacons;
    private JScrollPane scrInputPits;

    /* Text fields and areas related to the size of the board and the locations of the
    specially designated tiles
     */
    private JTextField tfInputSize;
    private JTextField tfInputGold;
    private JTextArea taInputBeacons;
    private JTextArea taInputPits;

    /**
     * Creates a window for the selection of the speed at which the agent's moves
     * are displayed
     */
    public InitScreen() {
        /* "Gold Miner" is used as the window title */
        super("Gold Miner");

        /* Use Border layout. */
        setLayout(new BorderLayout());

        /* init() is called to initialize the elements of the window */
        init();

        /* Additional formatting methods for the window are executed */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Initializes the elements comprising this window
     */
    private void init () {
        JPanel pSouth;            // South panel for the button
        JPanel pCenter;           // Center panel for the actual text fields
        JPanel pSizeGoldPrompts;  // Panel storing the label prompts for the board size and gold tile location
        JPanel pSizeGoldFields;   // Panel storing the text boxes for the board size and gold tile location
        JPanel pSizeGold;         // Panel storing the prompts and text boxes for the board size and gold tile location
        JPanel pInputBeacons;     // Panel storing the prompts and text boxes for the beacon locations
        JPanel pInputPits;        // Panel storing the prompts and text boxes for the pit locations

        /* Construct the south panel */
        pSouth = new JPanel();

        /* The south panel uses Flow layout for the button */
        pSouth.setLayout(new FlowLayout());
        pSouth.setBackground(Color.pink);

        /* Create the confirmatory button */
        btnConfirm = new JButton("Start");

        /* The user is not allowed to click the button if complete details have not been entered */
        btnConfirm.setEnabled(false);

        /* Add the button to the south panel */
        pSouth.add(btnConfirm);

        /* Position the south panel in the overall Border layout */
        add(pSouth, BorderLayout.SOUTH);

        /* Add and format prompts */
        lblInputSize = new JLabel("Enter board size: ");
        lblInputSize.setBorder(new EmptyBorder(0, 0, 10, 0));
        lblInputGold = new JLabel("Enter gold position: ");
        lblInputGold.setBorder(new EmptyBorder(10, 0, 0, 0));
        lblInputBeacons = new JLabel("Enter beacon positions: ");
        lblInputBeacons.setBorder(new EmptyBorder(2, 0, 5, 0));
        lblInputPits = new JLabel("Enter pit positions: ");
        lblInputPits.setBorder(new EmptyBorder(5, 0, 5, 0));

        /* Construct the center panel for the text fields */
        pCenter = new JPanel();

        /* The center panel uses a Box Layout, with components displayed from top to bottom */
        pCenter.setLayout(new BoxLayout(pCenter, BoxLayout.Y_AXIS));
        pCenter.setBackground(Color.pink);
        pCenter.setBorder(new EmptyBorder(20, 20, 20, 20));

        /* Create and format the text fields, storing the text fields for the beacon and pit
        positions in scroll panes
        */
        tfInputSize = new JTextField(15);
        tfInputGold = new JTextField(15);
        taInputBeacons = new JTextArea(8, 40);
        scrInputBeacons = new JScrollPane(taInputBeacons);
        scrInputBeacons.setPreferredSize(new Dimension(400, 80));
        taInputPits = new JTextArea(8, 40);
        scrInputPits = new JScrollPane(taInputPits);
        scrInputPits.setPreferredSize(new Dimension(400, 80));

        /* Create and format the panel holding the prompts and text fields for the board size and
        gold tile location
        */
        pSizeGold = new JPanel();
        pSizeGold.setLayout(new BoxLayout(pSizeGold, BoxLayout.X_AXIS));
        pSizeGold.setPreferredSize(new Dimension(400, 15));
        pSizeGold.setOpaque(false);
        pSizeGold.setBorder(new EmptyBorder(0, 0, 5, 0));

        /* Create and format the panel holding the prompts for the board size and gold tile location */
        pSizeGoldPrompts = new JPanel();
        pSizeGoldPrompts.setLayout(new BoxLayout(pSizeGoldPrompts, BoxLayout.Y_AXIS));
        pSizeGoldPrompts.setPreferredSize(new Dimension(200, 15));
        pSizeGoldPrompts.setOpaque(false);
        pSizeGoldPrompts.add(lblInputSize);
        pSizeGoldPrompts.add(lblInputGold);

        /* Create and format the panel holding the text fields for the board size and gold tile location */
        pSizeGoldFields = new JPanel();
        pSizeGoldFields.setLayout(new BoxLayout(pSizeGoldFields, BoxLayout.Y_AXIS));
        pSizeGoldFields.setPreferredSize(new Dimension(200, 15));
        pSizeGoldFields.setOpaque(false);
        pSizeGoldFields.add(tfInputSize);
        pSizeGoldFields.add(tfInputGold);

        /* Add the prompts and text fields to the pSizeGold panel */
        pSizeGold.add(pSizeGoldPrompts);
        pSizeGold.add(pSizeGoldFields);

        /* Create and format the panel holding the prompt and text field for the beacon positions */
        pInputBeacons = new JPanel();
        pInputBeacons.setLayout(new BorderLayout());
        pInputBeacons.setPreferredSize(new Dimension(400, 100));
        pInputBeacons.setOpaque(false);
        pInputBeacons.add(lblInputBeacons, BorderLayout.NORTH);
        pInputBeacons.add(scrInputBeacons, BorderLayout.CENTER);

        /* Create and format the panel holding the prompt and text field for the pit positions */
        pInputPits = new JPanel();
        pInputPits.setLayout(new BorderLayout());
        pInputPits.setPreferredSize(new Dimension(400, 100));
        pInputPits.setOpaque(false);
        pInputPits.add(lblInputPits, BorderLayout.NORTH);
        pInputPits.add(scrInputPits, BorderLayout.CENTER);

        /* Add the prompts and text fields for the four user inputs to the center panel */
        pCenter.add(pSizeGold);
        pCenter.add(pInputBeacons);
        pCenter.add(pInputPits);

        /* Position the center panel in the overall Border layout. */
        add(pCenter, BorderLayout.CENTER);
    }

    /**
     * Sets the action listener for the button in this graphical user interface
     *
     * @param listener action listener receiving action events
     */
    public void setActionListener(ActionListener listener) {
        btnConfirm.addActionListener(listener);
    }

    /**
     * Sets the document listener for the button in this graphical user interface
     *
     * @param listener document listener receiving document-related events
     */
    public void setDocumentListener(DocumentListener listener) {
        /* Add a listener for each text field. */
        tfInputSize.getDocument().addDocumentListener(listener);
        tfInputGold.getDocument().addDocumentListener(listener);
        taInputBeacons.getDocument().addDocumentListener(listener);
        taInputPits.getDocument().addDocumentListener(listener);
    }

    /**
     * Returns the user input concerning the dimension of the board
     *
     * @return user input concerning the dimension of the board
     */
    public String getInputSize() {
        return tfInputSize.getText();
    }

    /**
     * Returns the user input concerning the positions of the gold tile
     *
     * @return user input concerning the positions of the gold tile
     */
    public String getInputGold() {
        return tfInputGold.getText();
    }

    /**
     * Returns the user input concerning the positions of the beacon tiles
     *
     * @return user input concerning the positions of the beacon tiles
     */
    public String getInputBeacons() {
        return taInputBeacons.getText();
    }

    /**
     * Returns the user input concerning the positions of the pit tiles
     *
     * @return user input concerning the positions of the pit tiles
     */
    public String getInputPits() {
        return taInputPits.getText();
    }

    /**
     * Enables (or disables) the confirmatory button depending on the <code>
     * boolean</code> parameter passed
     *
     * @param isEnabled <code>true</code> if the confirmatory button is to be enabled;
     *                  <code>false</code>, otherwise
     */
    public void setConfirmEnabled(boolean isEnabled) {
        btnConfirm.setEnabled(isEnabled);
    }
}

