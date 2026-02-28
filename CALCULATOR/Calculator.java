import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * ╔═══════════════════════════════════════════════════════════════╗
 *   Calculator with History  —  Java Swing GUI Project
 *   Perfect for Beginners! Learn These Components & Concepts:
 *
 *   ✅ JFrame              - Main application window
 *   ✅ JPanel              - Layout containers
 *   ✅ JButton             - Calculator keys (styled)
 *   ✅ JLabel              - Display, expression, mode labels
 *   ✅ JTextField          - Editable display field
 *   ✅ JList               - History sidebar list
 *   ✅ DefaultListModel    - Dynamic data model for JList
 *   ✅ JScrollPane         - Scrollable history panel
 *   ✅ JSplitPane          - Resizable left/right split
 *   ✅ JToggleButton       - Toggle dark/light mode
 *   ✅ JMenuBar / JMenu    - Menu bar at the top
 *   ✅ JMenuItem           - Menu items with actions
 *   ✅ KeyAdapter          - Keyboard input handling
 *   ✅ ActionListener      - Button click events
 *   ✅ GridLayout          - Button grid arrangement
 *   ✅ BorderLayout        - Main frame structure
 *   ✅ BoxLayout           - Vertical stacking
 *   ✅ CardLayout          - Switch between panels
 *   ✅ Color & Font        - Custom styling
 *   ✅ Graphics2D          - Custom painting (rounded panel)
 *   ✅ ArrayList           - Storing history entries
 *   ✅ DecimalFormat       - Formatting numbers nicely
 *   ✅ MouseAdapter        - Hover effects
 *   ✅ ClipboardOwner      - Copy to clipboard
 * ╚═══════════════════════════════════════════════════════════════╝
 *
 *  HOW TO COMPILE & RUN:
 *    javac CalculatorWithHistory.java
 *    java CalculatorWithHistory
 *
 *  KEYBOARD SHORTCUTS:
 *    0–9, +, -, *, /  →  Type directly
 *    Enter / =        →  Evaluate
 *    Backspace        →  Delete last character
 *    Escape           →  Clear (AC)
 *    Ctrl+C           →  Copy result
 */
public class CalculatorWithHistory extends JFrame {

    // ── Theme Colors (Dark Mode) ────────────────────────────
    private static Color BG_APP       = new Color(18, 18, 24);
    private static Color BG_DISPLAY   = new Color(10, 10, 15);
    private static Color BG_CALC      = new Color(26, 26, 36);
    private static Color BG_BTN_NUM   = new Color(40, 40, 58);
    private static Color BG_BTN_OP    = new Color(60, 60, 88);
    private static Color BG_BTN_FUNC  = new Color(34, 34, 50);
    private static Color COLOR_EQUALS = new Color(255, 107, 53);   // warm orange
    private static Color COLOR_ACCENT = new Color(100, 200, 255);  // ice blue
    private static Color BG_HISTORY   = new Color(22, 22, 32);
    private static Color BG_HIST_ITEM = new Color(32, 32, 46);
    private static Color BG_HIST_SEL  = new Color(50, 50, 75);
    private static Color TEXT_PRIMARY = new Color(240, 240, 255);
    private static Color TEXT_MUTED   = new Color(130, 130, 165);
    private static Color TEXT_EXPR    = new Color(160, 160, 200);
    private static Color BORDER_COL   = new Color(50, 50, 72);

    private boolean isDarkMode = true;

    // ── Fonts ───────────────────────────────────────────────
    private static final Font FONT_DISPLAY  = new Font("Segoe UI", Font.BOLD,  36);
    private static final Font FONT_EXPR     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BTN      = new Font("Segoe UI", Font.BOLD,  17);
    private static final Font FONT_BTN_SM   = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font FONT_HEAD     = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font FONT_HIST     = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FONT_HIST_RES = new Font("Courier New", Font.BOLD,  13);

    // ── Calculator State ────────────────────────────────────
    private String currentInput  = "0";
    private String expression    = "";
    private double operand1      = 0;
    private String operator      = "";
    private boolean freshResult  = false;
    private boolean hasError     = false;

    // ── History ─────────────────────────────────────────────
    private ArrayList<String> historyData = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    // ── UI Components ───────────────────────────────────────
    private JLabel  displayLabel;
    private JLabel  expressionLabel;
    private JLabel  memLabel;
    private JList<String>  historyList;
    private JSplitPane splitPane;
    private JToggleButton themeToggle;
    private JPanel  calcPanel;
    private JPanel  historyPanel;
    private double  memory = 0;
    private DecimalFormat df = new DecimalFormat("#.##########");

    // ═══════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ═══════════════════════════════════════════════════════
    public CalculatorWithHistory() {
        setTitle("Calculator  ·  with History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 580);
        setMinimumSize(new Dimension(520, 500));
        setLocationRelativeTo(null);
        setBackground(BG_APP);

        buildMenuBar();
        buildUI();
        addKeyboardListener();

        setVisible(true);
    }

    // ═══════════════════════════════════════════════════════
    //  MENU BAR  — Learn: JMenuBar, JMenu, JMenuItem
    // ═══════════════════════════════════════════════════════
    private void buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BG_APP);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COL));

        // ── Edit Menu
        JMenu editMenu = styledMenu("Edit");
        JMenuItem copyItem   = styledMenuItem("📋  Copy Result     Ctrl+C");
        JMenuItem clearHist  = styledMenuItem("🗑  Clear History");
        JMenuItem pasteItem  = styledMenuItem("📌  Paste to Input  Ctrl+V");

        copyItem.addActionListener(e -> copyToClipboard());
        clearHist.addActionListener(e -> clearHistory());
        pasteItem.addActionListener(e -> pasteFromClipboard());

        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(clearHist);

    // ══════════════════════════════════════════════════════
    //  MAIN — Entry point
    // ══════════════════════════════════════════════════════
    public static void main(String[] args) {
        // SwingUtilities.invokeLater — always launch Swing on the EDT
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
            new CalculatorWithHistory();
        });
    }
}        