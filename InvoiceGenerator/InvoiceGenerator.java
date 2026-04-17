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
    // ═══════════════════════════════════════════════════════
    //  TOP BAR — Title + Invoice Number
    // ═══════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        // Left — App title
        JLabel title = new JLabel("🧾  Invoice Generator");
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT);

        // Right — Invoice number
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setBackground(BG_CARD);

        JLabel invLabel = new JLabel("Invoice #:");
        invLabel.setFont(FONT_HEAD);
        invLabel.setForeground(TEXT_MUTED);

        invoiceNoField = styledTextField(String.valueOf(invoiceCounter), 80);
        invoiceNoField.setFont(FONT_MONO);
        invoiceNoField.setForeground(ACCENT);
        invoiceNoField.setEditable(false);

        rightPanel.add(invLabel);
        rightPanel.add(invoiceNoField);

        bar.add(title,      BorderLayout.WEST);
        bar.add(rightPanel, BorderLayout.EAST);
        return bar;
    }
    // ═══════════════════════════════════════════════════════
    //  MAIN PANEL — Two columns: Left form | Right table
    // ═══════════════════════════════════════════════════════
    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10);

        // Left column (forms)
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.38; gbc.weighty = 1.0;
        main.add(buildLeftColumn(), gbc);

        // Right column (table + summary)
        gbc.gridx = 1; gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.62;
        main.add(buildRightColumn(), gbc);

        return main;
    }

    // ═══════════════════════════════════════════════════════
    //  LEFT COLUMN — Shop Info | Customer Info | Add Item
    // ═══════════════════════════════════════════════════════
    private JPanel buildLeftColumn() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(BG_DARK);

        col.add(buildShopInfoCard());
        col.add(Box.createVerticalStrut(12));
        col.add(buildCustomerCard());
        col.add(Box.createVerticalStrut(12));
        col.add(buildAddItemCard());
        col.add(Box.createVerticalGlue());

        return col;
    }
 // ── Card: Shop Information ─────────────────────────────
    private JPanel buildShopInfoCard() {
        JPanel card = card("🏪  Shop Information");

        shopNameField    = styledTextField("My Little Shop", 0);
        shopAddressField = styledTextField("123, Main Street, City", 0);
        shopPhoneField   = styledTextField("+91 98765 43210", 0);

        addFormRow(card, "Shop Name:",    shopNameField);
        addFormRow(card, "Address:",      shopAddressField);
        addFormRow(card, "Phone:",        shopPhoneField);

        return card;
    }

    // ── Card: Customer Information ─────────────────────────
    private JPanel buildCustomerCard() {
        JPanel card = card("👤  Customer Details");

        custNameField  = styledTextField("Walk-in Customer", 0);
        custPhoneField = styledTextField("", 0);

        addFormRow(card, "Customer Name:", custNameField);
        addFormRow(card, "Phone:",         custPhoneField);

        return card;
    }
    // ── Card: Add Item ─────────────────────────────────────
    private JPanel buildAddItemCard() {
        JPanel card = card("➕  Add Item");

        // Item Name
        itemNameField = styledTextField("", 0);
        addFormRow(card, "Item Name:", itemNameField);

        // Category ComboBox — Learn: JComboBox
        categoryCombo = new JComboBox<>(new String[]{
            "Grocery", "Bakery", "Electronics", "Clothing",
            "Stationery", "Medicine", "Other"
        });
        styleCombo(categoryCombo);
        addFormRow(card, "Category:", categoryCombo);

        // Quantity Spinner — Learn: JSpinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 9999, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        styleSpinner(quantitySpinner);
        addFormRow(card, "Quantity:", quantitySpinner);

        // Price
        priceField = styledTextField("0.00", 0);
        addFormRow(card, "Unit Price (₹):", priceField);

        // Add button 
        card.add(Box.createVerticalStrut(10));
        JButton addBtn = accentButton("  ➕  Add to Invoice  ");
        addBtn.addActionListener(e -> addItemToTable());
        card.add(addBtn);

        return card;
    }

    