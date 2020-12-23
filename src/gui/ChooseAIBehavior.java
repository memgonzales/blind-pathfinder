package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Class implementing a window for the <b>selection of the intelligence level
 * of the agent </b>
 */
public class ChooseAIBehavior extends JFrame {

    /* User prompt */
    private JTextArea taPrompt;

    /* Text area and button for the first intelligence level */
    private JTextArea taOne;
    private JButton btnOne;

    /* Text area and button for the second intelligence level */
    private JTextArea taTwo;
    private JButton btnTwo;

    /**
     * Creates a window for the selection of the intelligence level of the agent
     */
    public ChooseAIBehavior() {

        /* Some formatting methods for the window are executed */
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(139, 0, 139), 8));
        setLayout(new BorderLayout());

        /* init() is called to initialize the elements of the window */
        init();

        /* Additional formatting methods for the window are executed */
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(false);
    }

    /**
     * Initializes the elements comprising this window
     */
    private void init() {
        /* Panels used to arrange the window elements */
        JPanel pMain;
        JPanel pLeft;
        JPanel pRight;
        JPanel pBox;
        JPanel pSouth;

        pMain = new JPanel();
        pMain.setLayout(new BorderLayout());
        pMain.setBackground(new Color(142, 229, 238));

        /* The user prompt for choosing the intelligence level is formatted and added to the window */
        taPrompt = new JTextArea("Choose AI behavior: ");
        taPrompt.setLineWrap(true);
        taPrompt.setWrapStyleWord(true);
        taPrompt.setEditable(false);
        taPrompt.setOpaque(false);
        taPrompt.setBorder(new EmptyBorder(10, 5, 5, 5));
        pMain.add (taPrompt, BorderLayout.NORTH);

        /* The pBox panel is formatted */
        pBox = new JPanel();
        pBox.setLayout(new BoxLayout(pBox, BoxLayout.X_AXIS));
        pBox.setOpaque(false);

        /* The pLeft panel is formatted */
        pLeft = new JPanel();
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        pLeft.setBorder(new EmptyBorder(10, 5, 5, 5));
        pLeft.setOpaque(false);

        /* The description and image for the first intelligence level are formatted and added to the window */
        taOne = new JTextArea("Move, scan, and rotate randomly, without using search strategies or " +
                "taking advantage of beacons.");
        taOne.setLineWrap(true);
        taOne.setWrapStyleWord(true);
        taOne.setEditable(false);
        taOne.setOpaque(false);
        pLeft.add(taOne);
        pLeft.add(setImage("assets/random.png"));
        pBox.add(pLeft);

        /* The pRight panel is formatted */
        pRight = new JPanel();
        pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
        pRight.setBorder(new EmptyBorder(10, 5, 5, 5));
        pRight.setOpaque(false);

        /* The description and image for second intelligence level are formatted and added to the window */
        taTwo = new JTextArea("Use decision-making strategies to minimize the number of actions needed to" +
                " find the gold.");
        taTwo.setLineWrap(true);
        taTwo.setWrapStyleWord(true);
        taTwo.setEditable(false);
        taTwo.setOpaque(false);
        pRight.add(taTwo);
        pRight.add(setImage("assets/smart.png"));
        pBox.add(pRight);

        pMain.add(pBox, BorderLayout.CENTER);

        /* The pSouth panel is formatted */
        pSouth = new JPanel();
        pSouth.setLayout(new GridLayout(1, 2));

        /* The buttons for the first and second intelligence levels are added to the window */
        btnOne = new JButton("Random");
        pSouth.add(btnOne);

        btnTwo = new JButton("Smart");
        pSouth.add(btnTwo);

        pMain.add(pSouth, BorderLayout.SOUTH);

        add (pMain, BorderLayout.CENTER);
    }

    /**
     * Returns a scaled version of an image given its path
     *
     * @param imagePath path to the image to be scaled
     * @return scaled version of the image as a label
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
        imageScaled = imageOrig.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);

        /* The scaled image is converted to a JLabel, further formatted, and returned */
        lblImage = new JLabel(new ImageIcon(imageScaled));
        lblImage.setBackground(Color.white);
        lblImage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        lblImage.setBorder(new EmptyBorder(5, 5, 5, 5));

        return lblImage;
    }

    /**
     * Sets the action listener for this graphical user interface
     *
     * @param listener action listener receiving action events
     */
    public void setActionListener(ActionListener listener) {
        btnOne.addActionListener(listener);
        btnTwo.addActionListener(listener);
    }
}
