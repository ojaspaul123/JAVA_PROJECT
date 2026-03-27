import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.*;
import java.util.*;


public class InvoiceGenerator extends JFrame {

    // ── Color Palette ──────────────────────────────────────
    private static final Color BG_DARK      = new Color(15, 23, 42);      // deep navy
    private static final Color BG_CARD      = new Color(30, 41, 59);      // card bg
    private static final Color ACCENT       = new Color(250, 189, 47);    // golden yellow
    private static final Color ACCENT_HOVER = new Color(251, 207, 100);
    private static final Color TEXT_PRIMARY  = new Color(248, 250, 252);
    private static final Color TEXT_MUTED    = new Color(148, 163, 184);
    private static final Color TABLE_ALT     = new Color(36, 50, 71);
    private static final Color BORDER_COLOR  = new Color(51, 65, 85);
    private static final Color DELETE_RED    = new Color(239, 68, 68);
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);

    // ── Fonts ──────────────────────────────────────────────
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font FONT_HEAD   = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_MONO   = new Font("Courier New", Font.BOLD, 12);
    private static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Data ───────────────────────────────────────────────
    private int invoiceCounter = 1001;
    private NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    // ── Shop Info Fields ───────────────────────────────────
    private JTextField shopNameField, shopAddressField, shopPhoneField;

    // ── Customer Info Fields ───────────────────────────────
    private JTextField custNameField, custPhoneField;

    // ── Item Entry Fields ──────────────────────────────────
    private JTextField itemNameField;
    private JSpinner   quantitySpinner;
    private JTextField priceField;
    private JComboBox<String> categoryCombo;

    // ── Items Table ────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable itemsTable;

    // ── Summary Labels ─────────────────────────────────────
    private JLabel subtotalLabel, taxLabel, totalLabel, invoiceNumLabel;

    // ── Invoice Number ─────────────────────────────────────
    private JTextField invoiceNoField;

    // ═══════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ═══════════════════════════════════════════════════════
    public InvoiceGenerator() {
        setTitle("🧾  Invoice Generator — Small Shop Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 750);
        setMinimumSize(new Dimension(850, 650));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        // Use BorderLayout for the main frame
        setLayout(new BorderLayout(0, 0));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildMainPanel(), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    