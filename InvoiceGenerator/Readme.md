# Invoice Generator
# 🧾 Invoice Generator — Small Shop Manager

A desktop billing application built with **Java Swing** for small shop owners who need a fast, offline-friendly way to generate and print invoices — no internet, no subscription, no complexity.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=flat-square&logo=java)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=flat-square)

---

## 📸 Screenshots

| Main Interface | Invoice Preview |
|---|---|
| <img width="699" height="554" alt="Screenshot 2026-05-15 225534" src="https://github.com/user-attachments/assets/1dabe9f5-f838-4782-9446-f9cda84750a3" />
| <img width="695" height="551" alt="Screenshot 2026-05-15 225559" src="https://github.com/user-attachments/assets/190231af-9aa0-474a-afa0-8ac902021639" />
 |

---

## 🔍 What Does It Do?

Invoice Generator is a single-file Java desktop app that lets shop owners:

- **Enter shop details** — name, address, and phone number (saved as defaults)
- **Add customer info** — name and phone number per invoice
- **Build an itemized bill** — add items with name, category, quantity, and unit price
- **Auto-calculate totals** — subtotal, 18% GST, and final total update live as items are added
- **Preview the invoice** — a clean, formatted receipt preview pops up before printing
- **Print directly** — sends the invoice to any connected printer via Java's `Printable` API
- **Manage invoices** — remove items, clear the current bill, or start a new invoice with an auto-incremented invoice number (starting at #1001)

### Supported Item Categories
`Grocery` · `Bakery` · `Electronics` · `Clothing` · `Stationery` · `Medicine` · `Other`

---

## 👥 Who Uses It?

This tool is built for:

- 🏪 **Small kirana / grocery store owners** who need quick billing without a costly POS system
- 🥐 **Bakery and food stall operators** handling walk-in customers
- 📦 **Solo retailers and micro-businesses** in Tier 2/3 cities where internet connectivity is unreliable
- 🧑‍💻 **Java learners** looking for a real-world Swing project that covers `JTable`, `JSpinner`, `JComboBox`, `JOptionPane`, `Printable`, and custom UI theming

---

## 🌍 Real-World Impact

India has over **63 million micro and small enterprises** (MSME). Most of them still rely on handwritten bills or expensive proprietary billing software. This app addresses that gap:

- **Zero cost** — no subscription, no cloud dependency, no hidden fees
- **Works offline** — runs entirely on the local machine; no Wi-Fi needed
- **GST-ready** — automatically applies 18% GST and clearly separates subtotal from tax, matching Indian compliance requirements
- **Instant setup** — a single `.java` file; compile once and run anywhere Java is installed
- **Printable receipts** — generates a formatted receipt that can be printed on any printer, including basic thermal printers via system dialog

Even a marginal improvement in billing speed (say, 2–3 minutes saved per transaction) translates to significant time savings for shops handling 30–50 customers a day.

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher installed ([Download JDK](https://adoptium.net/))

### Compile & Run

```bash
# Clone the repo
git clone https://github.com/your-username/invoice-generator.git
cd invoice-generator

# Compile
javac InvoiceGenerator.java

# Run
java InvoiceGenerator
```

That's it — no build tools, no dependencies, no configuration files.

---

## 🛠️ Features at a Glance

| Feature | Details |
|---|---|
| Invoice numbering | Auto-increments from #1001 |
| Tax calculation | 18% GST applied on subtotal |
| Currency format | Indian Rupee (₹), `en_IN` locale |
| Row management | Add, remove, and auto-renumber rows |
| Print preview | Monospaced receipt preview before printing |
| Input validation | Warns on empty item name or invalid price |
| Theme | Dark navy + golden accent (`#FABF2F`) |

---

## ⚠️ Challenges I Faced & How I Resolved Them

### 1. 🖨️ Getting Java's `Printable` API to Work
**Problem:** Java's `Printable` interface requires rendering content manually onto a `Graphics2D` context, which is complex for text-heavy layouts. Initial attempts produced clipped or misaligned print output.

**Resolution:** Instead of rendering directly to `Graphics2D`, I built the invoice content as a `JTextArea` with a monospaced font and called `JTextArea.print()`, which handles pagination and scaling automatically. This traded fine-grained control for reliability and simplicity.

---

### 2. 💰 Parsing Currency Strings Back to Doubles
**Problem:** When calculating the subtotal, the table stores formatted strings like `"₹ 178.00"`. Parsing these directly with `Double.parseDouble()` threw a `NumberFormatException` because of the ₹ symbol.

**Resolution:** Added a `.replace("₹ ", "")` strip before parsing, and wrapped the call in a `try-catch` block. A cleaner long-term fix would be storing raw `double` values in the model and only formatting for display — something planned for a future refactor.

---

### 3. 🎨 Custom Dark Theme in Swing
**Problem:** Java Swing defaults to the system look-and-feel, which overrides custom background colors on some components (especially `JComboBox`, `JSpinner`, and `JScrollPane`).

**Resolution:** Applied `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` at startup and then manually overrode colors on individual components using `setBackground()`, `setForeground()`, and custom `Border` factories. For the table, a custom `DefaultTableCellRenderer` was used to implement alternating row colors and right-aligned price columns.

---

### 4. 🔢 Row Renumbering After Deletion
**Problem:** After deleting a row from the middle of the table, the `#` column numbers became non-sequential (e.g., 1, 3, 4 after deleting row 2).

**Resolution:** After every deletion, a simple loop iterates through all remaining rows and resets column 0 to `i + 1`, keeping the numbering clean and consistent.

---

### 5. 📐 Layout Stability on Window Resize
**Problem:** Using nested `BoxLayout` panels caused the left column (forms) to stretch disproportionately when the window was resized, pushing the item entry fields out of view.

**Resolution:** Switched the main panel to `GridBagLayout` with explicit `weightx` ratios (`0.38` for the form column, `0.62` for the table column) and set `fill = BOTH`. This keeps the proportions stable across different window sizes.

---

## 🗺️ Planned Improvements

- [ ] Save invoices to a local SQLite database
- [ ] Export invoice as PDF
- [ ] Configurable GST rate (5%, 12%, 18%, 28%)
- [ ] Customer history / repeat customer lookup
- [ ] Barcode / QR code on printed receipt
- [ ] Thermal printer (80mm) optimized layout

---

## 📄 License

This project is licensed under the [MIT License](LICENSE). Free to use, modify, and distribute.

---

## 🙋 About

Built by a developer learning Java Swing through practical projects. Feedback, issues, and pull requests are welcome!

> *"The best way to learn is to build something real."*
