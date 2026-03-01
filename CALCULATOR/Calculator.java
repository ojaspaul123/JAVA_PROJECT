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
        // ── View Menu
        JMenu viewMenu = styledMenu("View");
        JMenuItem toggleHist = styledMenuItem("📜  Toggle History Panel");
        JMenuItem themeItem  = styledMenuItem("🌙  Toggle Dark / Light");
        toggleHist.addActionListener(e -> toggleHistoryPanel());
        themeItem.addActionListener(e -> toggleTheme());
        viewMenu.add(toggleHist);
        viewMenu.add(themeItem);

        // ── Help Menu
        JMenu helpMenu = styledMenu("Help");
        JMenuItem shortcutsItem = styledMenuItem("⌨  Keyboard Shortcuts");
        shortcutsItem.addActionListener(e -> showShortcuts());
        helpMenu.add(shortcutsItem);

        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JMenu styledMenu(String text) {
        JMenu m = new JMenu(text);
        m.setFont(FONT_BTN_SM);
        m.setForeground(TEXT_PRIMARY);
        m.setBackground(BG_APP);
        return m;
    }

    private JMenuItem styledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FONT_EXPR);
        item.setForeground(TEXT_PRIMARY);
        item.setBackground(BG_BTN_FUNC);
        return item;
    }    
    // ═══════════════════════════════════════════════════════
    //  MAIN UI BUILD
    // ═══════════════════════════════════════════════════════
    private void buildUI() {
        getContentPane().setBackground(BG_APP);
        setLayout(new BorderLayout());

        // ── JSplitPane dividing calc | history ─────────────
        calcPanel    = buildCalculatorPanel();
        historyPanel = buildHistoryPanel();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, calcPanel, historyPanel);
        splitPane.setResizeWeight(0.6);
        splitPane.setDividerSize(4);
        splitPane.setBackground(BG_APP);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);

        add(splitPane, BorderLayout.CENTER);
    }
    // ═══════════════════════════════════════════════════════
    //  CALCULATOR PANEL (left side)
    // ═══════════════════════════════════════════════════════
    private JPanel buildCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_CALC);

        panel.add(buildDisplay(),    BorderLayout.NORTH);
        panel.add(buildButtonGrid(), BorderLayout.CENTER);

        return panel;
    }

    // ── Display ────────────────────────────────────────────
    private JPanel buildDisplay() {
        JPanel display = new JPanel();
        display.setLayout(new BoxLayout(display, BoxLayout.Y_AXIS));
        display.setBackground(BG_DISPLAY);
        display.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_EQUALS),
            BorderFactory.createEmptyBorder(18, 22, 14, 22)
        ));

        // Top row: MEM indicator + theme toggle
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(BG_DISPLAY);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        memLabel = new JLabel("M: 0");
        memLabel.setFont(FONT_EXPR);
        memLabel.setForeground(COLOR_ACCENT);

        // JToggleButton — Learn: toggle button
        themeToggle = new JToggleButton("☀");
        themeToggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        themeToggle.setForeground(TEXT_MUTED);
        themeToggle.setBackground(BG_DISPLAY);
        themeToggle.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        themeToggle.setFocusPainted(false);
        themeToggle.addActionListener(e -> toggleTheme());

        topRow.add(memLabel,     BorderLayout.WEST);
        topRow.add(themeToggle,  BorderLayout.EAST);

        // Expression label (shows e.g. "12 + 34 =")
        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(FONT_EXPR);
        expressionLabel.setForeground(TEXT_EXPR);
        expressionLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Main display number
        displayLabel = new JLabel("0");
        displayLabel.setFont(FONT_DISPLAY);
        displayLabel.setForeground(TEXT_PRIMARY);
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        display.add(topRow);
        display.add(Box.createVerticalStrut(4));
        display.add(expressionLabel);
        display.add(Box.createVerticalStrut(2));
        display.add(displayLabel);

        return display;
    }

    // ── Button Grid ────────────────────────────────────────
    private JPanel buildButtonGrid() {
        JPanel grid = new JPanel(new GridLayout(6, 4, 6, 6));
        grid.setBackground(BG_CALC);
        grid.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Button layout:
        // Row 1: MC  MR  M+  M-
        // Row 2: AC  +/- %   ÷
        // Row 3:  7   8   9   ×
        // Row 4:  4   5   6   −
        // Row 5:  1   2   3   +
        // Row 6:  √   0   .   =

        String[][] labels = {
            { "MC",  "MR",  "M+",  "M−"  },
            { "AC",  "±",   "%",   "÷"   },
            { "7",   "8",   "9",   "×"   },
            { "4",   "5",   "6",   "−"   },
            { "1",   "2",   "3",   "+"   },
            { "√",   "0",   ".",   "="   },
        };

        for (String[] row : labels) {
            for (String label : row) {
                grid.add(makeButton(label));
            }
        }

        return grid;
    }    
// ── Single Button Factory ─────────────────────────────
    private JButton makeButton(String label) {
        JButton btn = new JButton(label);

        // Choose color category
        Color bg, fg;
        if (label.equals("=")) {
            bg = COLOR_EQUALS;
            fg = Color.WHITE;
        } else if ("÷×−+".contains(label)) {
            bg = BG_BTN_OP;
            fg = COLOR_ACCENT;
        } else if ("AC±%√".contains(label) || label.startsWith("M")) {
            bg = BG_BTN_FUNC;
            fg = new Color(255, 200, 100);
        } else {
            bg = BG_BTN_NUM;
            fg = TEXT_PRIMARY;
        }

        btn.setFont(label.length() > 1 ? FONT_BTN_SM : FONT_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Rounded look via custom painting
        final Color normalBg  = bg;
        final Color hoverBg   = brighten(bg, 30);
        final Color pressedBg = darken(bg, 20);

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ButtonModel model = ((JButton) c).getModel();
                Color drawBg = model.isPressed() ? pressedBg : model.isRollover() ? hoverBg : normalBg;
                g2.setColor(drawBg);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 14, 14);
                g2.dispose();
                super.paint(g, c);
            }
        });

        final Color origFg = fg;
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(label.equals("=") ? Color.WHITE : Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(origFg); }
        });

        btn.addActionListener(e -> handleButton(label));
        return btn;
    }
    // ═══════════════════════════════════════════════════════
    //  HISTORY PANEL (right side)
    //  Learn: JList, DefaultListModel, JScrollPane
    // ═══════════════════════════════════════════════════════
    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_HISTORY);
        panel.setPreferredSize(new Dimension(240, 0));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HISTORY);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 2, 0, BORDER_COL),
            BorderFactory.createEmptyBorder(12, 14, 10, 10)
        ));
        JLabel title = new JLabel("📜  History");
        title.setFont(FONT_HEAD);
        title.setForeground(TEXT_MUTED);

        JButton clearBtn = new JButton("✕");
        clearBtn.setFont(FONT_BTN_SM);
        clearBtn.setForeground(TEXT_MUTED);
        clearBtn.setBackground(BG_HISTORY);
        clearBtn.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 4));
        clearBtn.setFocusPainted(false);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setToolTipText("Clear history");
        clearBtn.addActionListener(e -> clearHistory());

        header.add(title,    BorderLayout.WEST);
        header.add(clearBtn, BorderLayout.EAST);

        // JList — Learn: DefaultListModel + JList
        historyList = new JList<>(listModel);
        historyList.setBackground(BG_HISTORY);
        historyList.setForeground(TEXT_PRIMARY);
        historyList.setFont(FONT_HIST);
        historyList.setFixedCellHeight(52);
        historyList.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        historyList.setSelectionBackground(BG_HIST_SEL);
        historyList.setSelectionForeground(Color.WHITE);

        // Custom cell renderer for history items
        historyList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {

                JPanel cell = new JPanel();
                cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                cell.setBackground(isSelected ? BG_HIST_SEL : (index % 2 == 0 ? BG_HIST_ITEM : BG_HISTORY));
                cell.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, isSelected ? COLOR_EQUALS : BG_HISTORY),
                    BorderFactory.createEmptyBorder(7, 10, 7, 10)
                ));

                String entry = value.toString();
                String[] parts = entry.split("=");
                String expr = parts[0].trim();
                String res  = parts.length > 1 ? "= " + parts[1].trim() : "";

                JLabel exprLbl = new JLabel(expr);
                exprLbl.setFont(FONT_HIST);
                exprLbl.setForeground(isSelected ? TEXT_EXPR : TEXT_MUTED);
                exprLbl.setAlignmentX(LEFT_ALIGNMENT);

                JLabel resLbl = new JLabel(res);
                resLbl.setFont(FONT_HIST_RES);
                resLbl.setForeground(isSelected ? Color.WHITE : TEXT_PRIMARY);
                resLbl.setAlignmentX(LEFT_ALIGNMENT);

                cell.add(exprLbl);
                cell.add(resLbl);
                return cell;
            }
        });

        // Click history to restore value
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selected = historyList.getSelectedValue();
                    if (selected != null) {
                        String[] parts = selected.split("=");
                        if (parts.length > 1) {
                            currentInput = parts[1].trim();
                            updateDisplay();
                        }
                    }
                }
            }
        });

        // JScrollPane wrapping the JList
        JScrollPane scroll = new JScrollPane(historyList);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COL));
        scroll.setBackground(BG_HISTORY);
        scroll.getViewport().setBackground(BG_HISTORY);
        scroll.getVerticalScrollBar().setBackground(BG_HISTORY);

        // Footer hint
        JLabel hint = new JLabel("Double-click to restore result");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        hint.setForeground(TEXT_MUTED);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 0, 0, BORDER_COL),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        hint.setBackground(BG_HISTORY);
        hint.setOpaque(true);

        panel.add(header, BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);
        panel.add(hint,    BorderLayout.SOUTH);

        return panel;
    }


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