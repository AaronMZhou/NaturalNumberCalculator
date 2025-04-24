import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import components.naturalnumber.NaturalNumber;

/**
 * View class.
 *
 * @author Put your name here
 */
public final class NNCalcView1 extends JFrame implements NNCalcView {

    /**
     * Controller object registered with this view to observe user-interaction
     * events.
     */
    private NNCalcController controller;

    /**
     * State of user interaction: last event "seen".
     */
    private enum State {
        /**
         * Last event was clear, enter, another operator, or digit entry, resp.
         */
        SAW_CLEAR, SAW_ENTER_OR_SWAP, SAW_OTHER_OP, SAW_DIGIT
    }

    /**
     * State variable to keep track of which event happened last; needed to
     * prepare for digit to be added to bottom operand.
     */
    private State currentState;

    /**
     * Text areas.
     */
    private final JTextArea tTop, tBottom;

    /**
     * Operator and related buttons.
     */
    private final JButton bClear, bSwap, bEnter, bAdd, bSubtract, bMultiply, bDivide,
            bPower, bRoot;

    /**
     * Digit entry buttons.
     */
    private final JButton[] bDigits;

    /**
     * Useful constants.
     */
    private static final int TEXT_AREA_HEIGHT = 5, TEXT_AREA_WIDTH = 20,
            DIGIT_BUTTONS = 10, MAIN_BUTTON_PANEL_GRID_ROWS = 4,
            MAIN_BUTTON_PANEL_GRID_COLUMNS = 4, SIDE_BUTTON_PANEL_GRID_ROWS = 3,
            SIDE_BUTTON_PANEL_GRID_COLUMNS = 1, CALC_GRID_ROWS = 3, CALC_GRID_COLUMNS = 1;

    /**
     * No argument constructor.
     */
    public NNCalcView1() {
        // Create the JFrame being extended
        super("Natural Number Calculator");

        // Set up initial state of GUI to behave like last event was "Clear"
        this.currentState = State.SAW_CLEAR;

        /*
         * Create widgets
         */
        // Text areas should wrap lines, and should be read-only
        this.tTop = new JTextArea(TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.tTop.setEditable(false);
        this.tTop.setLineWrap(true);
        this.tTop.setWrapStyleWord(true);

        this.tBottom = new JTextArea(TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.tBottom.setEditable(false);
        this.tBottom.setLineWrap(true);
        this.tBottom.setWrapStyleWord(true);

        // Operator and related buttons
        this.bClear = new JButton("Clear");
        this.bSwap = new JButton("Swap");
        this.bEnter = new JButton("Enter");
        this.bAdd = new JButton("+");
        this.bSubtract = new JButton("-");
        this.bMultiply = new JButton("*");
        this.bDivide = new JButton("/");
        this.bPower = new JButton("^");
        this.bRoot = new JButton("√");

        // Digit entry buttons
        this.bDigits = new JButton[DIGIT_BUTTONS];
        for (int i = 0; i < DIGIT_BUTTONS; i++) {
            this.bDigits[i] = new JButton(String.valueOf(i));
        }

        /*
         * Initially, disable divide (divisor must not be 0) and root (root must
         * be at least 2)
         */
        this.bDivide.setEnabled(false);
        this.bRoot.setEnabled(false);

        /*
         * Create scroll panes for the text areas in case number is long enough
         * to require scrolling
         */
        JScrollPane spTop = new JScrollPane(this.tTop);
        JScrollPane spBottom = new JScrollPane(this.tBottom);

        /*
         * Create main button panel
         */
        JPanel mainPanel = new JPanel(new GridLayout(MAIN_BUTTON_PANEL_GRID_ROWS,
                MAIN_BUTTON_PANEL_GRID_COLUMNS));

        /*
         * Add the buttons to the main button panel, from left to right and top
         * to bottom Layout: [7 8 9 +] [4 5 6 -] [1 2 3 *] [0 / ^ √]
         */
        mainPanel.add(this.bDigits[7]);
        mainPanel.add(this.bDigits[8]);
        mainPanel.add(this.bDigits[9]);
        mainPanel.add(this.bAdd);

        mainPanel.add(this.bDigits[4]);
        mainPanel.add(this.bDigits[5]);
        mainPanel.add(this.bDigits[6]);
        mainPanel.add(this.bSubtract);

        mainPanel.add(this.bDigits[1]);
        mainPanel.add(this.bDigits[2]);
        mainPanel.add(this.bDigits[3]);
        mainPanel.add(this.bMultiply);

        mainPanel.add(this.bDigits[0]);
        mainPanel.add(this.bDivide);
        mainPanel.add(this.bPower);
        mainPanel.add(this.bRoot);

        /*
         * Create side button panel
         */
        JPanel sidePanel = new JPanel(new GridLayout(SIDE_BUTTON_PANEL_GRID_ROWS,
                SIDE_BUTTON_PANEL_GRID_COLUMNS));
        /*
         * Add the buttons to the side button panel, from top to bottom
         */
        sidePanel.add(this.bClear);
        sidePanel.add(this.bSwap);
        sidePanel.add(this.bEnter);

        /*
         * Create combined button panel organized using flow layout
         */
        JPanel buttonPanel = new JPanel(new FlowLayout());
        /*
         * Add the other two button panels to the combined button panel
         */
        buttonPanel.add(mainPanel);
        buttonPanel.add(sidePanel);

        /*
         * Organize main window: 3 rows, 1 column
         */
        this.setLayout(new GridLayout(CALC_GRID_ROWS, CALC_GRID_COLUMNS));
        /*
         * Add scroll panes and button panel to main window
         */
        this.add(spTop);
        this.add(spBottom);
        this.add(buttonPanel);

        /*
         * Make sure the main window is appropriately sized, exits this program
         * on close, and becomes visible to the user
         */
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void registerObserver(NNCalcController controller) {
        /*
         * Register argument as observer/listener of this
         */
        this.controller = controller;

        // Register this object as the observer for all GUI events
        this.bClear.addActionListener(this);
        this.bSwap.addActionListener(this);
        this.bEnter.addActionListener(this);
        this.bAdd.addActionListener(this);
        this.bSubtract.addActionListener(this);
        this.bMultiply.addActionListener(this);
        this.bDivide.addActionListener(this);
        this.bPower.addActionListener(this);
        this.bRoot.addActionListener(this);
        for (JButton b : this.bDigits) {
            b.addActionListener(this);
        }
    }

    @Override
    public void updateTopDisplay(NaturalNumber n) {
        /*
         * Updates top operand display based on NaturalNumber provided as
         * argument.
         */
        this.tTop.setText(n.toString());
    }

    @Override
    public void updateBottomDisplay(NaturalNumber n) {
        /*
         * Updates bottom operand display based on NaturalNumber provided as
         * argument.
         */
        this.tBottom.setText(n.toString());
    }

    @Override
    public void updateSubtractAllowed(boolean allowed) {
        /*
         * Updates display of whether subtract operation is allowed.
         */
        this.bSubtract.setEnabled(allowed);
    }

    @Override
    public void updateDivideAllowed(boolean allowed) {
        /*
         * Updates display of whether divide operation is allowed.
         */
        this.bDivide.setEnabled(allowed);
    }

    @Override
    public void updatePowerAllowed(boolean allowed) {
        /*
         * Updates display of whether power operation is allowed.
         */
        this.bPower.setEnabled(allowed);
    }

    @Override
    public void updateRootAllowed(boolean allowed) {
        /*
         * Updates display of whether root operation is allowed.
         */
        this.bRoot.setEnabled(allowed);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /*
         * Set cursor to indicate computation on-going; this matters only if
         * processing the event might take a noticeable amount of time as seen
         * by the user
         */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        /*
         * Determine which event has occurred; only buttons are involved here
         */
        Object source = event.getSource();
        if (source == this.bClear) {
            this.controller.processClearEvent();
            this.currentState = State.SAW_CLEAR;
        } else if (source == this.bSwap) {
            this.controller.processSwapEvent();
            this.currentState = State.SAW_ENTER_OR_SWAP;
        } else if (source == this.bEnter) {
            this.controller.processEnterEvent();
            this.currentState = State.SAW_ENTER_OR_SWAP;
        } else if (source == this.bAdd) {
            this.controller.processAddEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bSubtract) {
            this.controller.processSubtractEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bMultiply) {
            this.controller.processMultiplyEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bDivide) {
            this.controller.processDivideEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bPower) {
            this.controller.processPowerEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bRoot) {
            this.controller.processRootEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else {
            for (int i = 0; i < DIGIT_BUTTONS; i++) {
                if (source == this.bDigits[i]) {
                    switch (this.currentState) {
                        case SAW_ENTER_OR_SWAP:
                            this.controller.processClearEvent();
                            break;
                        case SAW_OTHER_OP:
                            this.controller.processEnterEvent();
                            this.controller.processClearEvent();
                            break;
                        default:
                            break;
                    }
                    this.controller.processAddNewDigitEvent(i);
                    this.currentState = State.SAW_DIGIT;
                    break;
                }
            }
        }

        /*
         * Set the cursor back to normal
         */
        this.setCursor(Cursor.getDefaultCursor());
    }

}
