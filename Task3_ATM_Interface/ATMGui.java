import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Task 3 Requirement 2:
 * "Design the user interface for the ATM, including options such as
 * withdrawing, depositing, and checking the balance."
 * 
 * Modern, responsive Java Swing Graphical User Interface with custom banking
 * dark theme.
 */
public class ATMGui extends JFrame {
    private final ATM atm;
    private final CardLayout cardLayout;
    private final JPanel mainContainer;

    // Theme Colors
    public static final Color BG_DARK = new Color(10, 25, 60);
    public static final Color BG_CARD = new Color(20, 45, 90);
    public static final Color BG_CARD_HOVER = new Color(30, 65, 120);
    public static final Color ACCENT_BLUE = new Color(30, 144, 255);
    public static final Color ACCENT_GREEN = new Color(34, 197, 94);
    public static final Color ACCENT_RED = new Color(239, 68, 68);
    public static final Color TEXT_WHITE = new Color(255, 255, 255);
    public static final Color TEXT_MUTED = new Color(180, 195, 220);

    private final DecimalFormat currencyFormat = new DecimalFormat("₹#,##0.00");

    // UI State
    private JLabel balanceLabel;
    private boolean isBalanceVisible = true;
    private JLabel statusToast;
    private JPasswordField pinField;

    public ATMGui(ATM atm) {
        this.atm = atm;
        setTitle("Axis Bank - ATM Interface");
        setSize(850, 680);
        setMinimumSize(new Dimension(800, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Apply global UI font
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(BG_DARK);

        // Create the two primary screens
        JPanel authPanel = createAuthScreen();
        JPanel dashboardPanel = createDashboardScreen();

        mainContainer.add(authPanel, "AUTH");
        mainContainer.add(dashboardPanel, "DASHBOARD");

        add(mainContainer);
        cardLayout.show(mainContainer, "AUTH");
    }

    /**
     * Screen 1: PIN Authentication with Interactive Keypad
     */
    private JPanel createAuthScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel brandLabel = new JLabel("AXIS BANK");
        brandLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        brandLabel.setForeground(ACCENT_BLUE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("Automated Teller Machine • Terminal " + atm.getAtmId());
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subLabel.setForeground(TEXT_MUTED);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(brandLabel);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subLabel);
        headerPanel.add(Box.createVerticalStrut(25));

        panel.add(headerPanel, BorderLayout.NORTH);

        // Center card with PIN input & Keypad
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(BG_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BG_CARD_HOVER, 1, true),
                new EmptyBorder(25, 30, 25, 30)));
        cardPanel.setMaximumSize(new Dimension(380, 420));

        JLabel insertPrompt = new JLabel("💳 Inserted Card: " + atm.getAccount().getMaskedAccountNumber());
        insertPrompt.setFont(new Font("SansSerif", Font.BOLD, 15));
        insertPrompt.setForeground(TEXT_WHITE);
        insertPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel holderPrompt = new JLabel("Cardholder: " + atm.getAccount().getAccountHolderName());
        holderPrompt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        holderPrompt.setForeground(TEXT_MUTED);
        holderPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel pinPrompt = new JLabel("Enter your 4-digit Security PIN:");
        pinPrompt.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pinPrompt.setForeground(TEXT_WHITE);
        pinPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        pinField = new JPasswordField(8);
        pinField.setHorizontalAlignment(JTextField.CENTER);
        pinField.setFont(new Font("SansSerif", Font.BOLD, 22));
        pinField.setBackground(BG_DARK);
        pinField.setForeground(TEXT_WHITE);
        pinField.setCaretColor(ACCENT_BLUE);
        pinField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT_BLUE, 1, true),
                new EmptyBorder(8, 15, 8, 15)));
        pinField.setMaximumSize(new Dimension(220, 45));

        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    verifyPin();
                }
            }
        });

        // Numeric Keypad
        JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10));
        keypad.setOpaque(false);
        keypad.setMaximumSize(new Dimension(260, 200));

        String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "CLR", "0", "ENT" };
        for (String key : keys) {
            JButton btn = new JButton(key);
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (key.equals("ENT")) {
                btn.setBackground(ACCENT_GREEN);
                btn.setForeground(Color.BLACK);
                btn.addActionListener(e -> verifyPin());
            } else if (key.equals("CLR")) {
                btn.setBackground(ACCENT_RED);
                btn.setForeground(Color.WHITE);
                btn.addActionListener(e -> pinField.setText(""));
            } else {
                btn.setBackground(BG_CARD_HOVER);
                btn.setForeground(TEXT_WHITE);
                btn.addActionListener(e -> {
                    char[] current = pinField.getPassword();
                    if (current.length < 6) {
                        pinField.setText(new String(current) + key);
                    }
                });
            }
            keypad.add(btn);
        }

        cardPanel.add(insertPrompt);
        cardPanel.add(Box.createVerticalStrut(4));
        cardPanel.add(holderPrompt);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(pinPrompt);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(pinField);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(keypad);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(cardPanel);

        panel.add(centerWrapper, BorderLayout.CENTER);

        // Security Footer
        JLabel footer = new JLabel("🔒 256-Bit End-to-End Encrypted Session • Do not share your PIN with anyone");
        footer.setFont(new Font("SansSerif", Font.ITALIC, 12));
        footer.setForeground(TEXT_MUTED);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private int authAttempts = 3;

    private void verifyPin() {
        String entered = new String(pinField.getPassword()).trim();
        if (atm.getAccount().validatePin(entered)) {
            authAttempts = 3;
            pinField.setText("");
            updateDashboardData();
            cardLayout.show(mainContainer, "DASHBOARD");
            showToast("Welcome back, " + atm.getAccount().getAccountHolderName() + "!", ACCENT_GREEN);
        } else {
            authAttempts--;
            pinField.setText("");
            if (authAttempts > 0) {
                JOptionPane.showMessageDialog(this,
                        "Incorrect PIN. You have " + authAttempts + " attempt(s) remaining.",
                        "Authentication Failed",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Maximum PIN attempts exceeded. Your card has been temporarily locked for security.",
                        "Card Locked",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    /**
     * Screen 2: ATM Dashboard (Check Balance, Deposit, Withdraw, Mini Statement,
     * etc.)
     */
    private JPanel createDashboardScreen() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel logo = new JLabel("AXIS BANK • ATM");
        logo.setFont(new Font("SansSerif", Font.BOLD, 20));
        logo.setForeground(ACCENT_BLUE);

        JButton logoutBtn = createStyledButton("Eject Card & Exit ⏏", ACCENT_RED, Color.WHITE);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to eject your card and end your session?",
                    "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainContainer, "AUTH");
                showToast("Card ejected safely. Thank you!", ACCENT_BLUE);
            }
        });

        topBar.add(logo, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // 2. Center Content: Virtual Debit Card + Action Grid
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);

        // Virtual Bank Card Display
        JPanel cardDisplay = createVirtualCardPanel();
        centerContent.add(cardDisplay);
        centerContent.add(Box.createVerticalStrut(20));

        // Action Buttons Grid (6 Primary Actions)
        JPanel actionGrid = new JPanel(new GridLayout(2, 3, 15, 15));
        actionGrid.setOpaque(false);

        JButton checkBalBtn = createMenuActionButton("💳 Check Balance", "View live account balance", ACCENT_BLUE);
        checkBalBtn.addActionListener(e -> handleCheckBalance());

        JButton depositBtn = createMenuActionButton("💰 Deposit Funds", "Deposit cash into your account", ACCENT_GREEN);
        depositBtn.addActionListener(e -> handleDepositDialog());

        JButton withdrawBtn = createMenuActionButton("💵 Withdraw Cash", "Instant cash withdrawal",
                new Color(245, 158, 11)); // Amber
        withdrawBtn.addActionListener(e -> handleWithdrawDialog());

        JButton statementBtn = createMenuActionButton("📜 Mini Statement", "View recent transaction records",
                new Color(168, 85, 247)); // Purple
        statementBtn.addActionListener(e -> handleShowStatement());

        JButton receiptBtn = createMenuActionButton("🧾 Print Receipt", "Generate digital transaction slip",
                new Color(14, 165, 233)); // Light blue
        receiptBtn.addActionListener(e -> handlePrintReceipt());

        JButton changePinBtn = createMenuActionButton("🔒 Change PIN", "Update your 4-digit security PIN",
                new Color(100, 116, 139)); // Slate
        changePinBtn.addActionListener(e -> handleChangePinDialog());

        actionGrid.add(checkBalBtn);
        actionGrid.add(depositBtn);
        actionGrid.add(withdrawBtn);
        actionGrid.add(statementBtn);
        actionGrid.add(receiptBtn);
        actionGrid.add(changePinBtn);

        centerContent.add(actionGrid);
        panel.add(centerContent, BorderLayout.CENTER);

        // 3. Status Notification Bar
        statusToast = new JLabel("System Ready. Select an operation above.");
        statusToast.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusToast.setForeground(TEXT_WHITE);
        statusToast.setOpaque(true);
        statusToast.setBackground(BG_CARD);
        statusToast.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BG_CARD_HOVER, 1, true),
                new EmptyBorder(10, 15, 10, 15)));
        panel.add(statusToast, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the sleek virtual debit card component
     */
    private JPanel createVirtualCardPanel() {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(new Color(20, 30, 48)); // Deep metallic gradient look
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(56, 189, 248, 80), 2, true),
                new EmptyBorder(18, 25, 18, 25)));
        card.setMaximumSize(new Dimension(800, 130));

        // Left info
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel bankTitle = new JLabel("AXIS BANK • Platinum");
        bankTitle.setFont(new Font("SansSerif", Font.BOLD, 13));
        bankTitle.setForeground(ACCENT_BLUE);

        JLabel cardNum = new JLabel(atm.getAccount().getMaskedAccountNumber() + "   [CHIP ■))) ]");
        cardNum.setFont(new Font("Monospaced", Font.BOLD, 18));
        cardNum.setForeground(TEXT_WHITE);

        JLabel holder = new JLabel("HOLDER: " + atm.getAccount().getAccountHolderName().toUpperCase());
        holder.setFont(new Font("SansSerif", Font.PLAIN, 12));
        holder.setForeground(TEXT_MUTED);

        left.add(bankTitle);
        left.add(Box.createVerticalStrut(8));
        left.add(cardNum);
        left.add(Box.createVerticalStrut(6));
        left.add(holder);

        // Right info (Balance display + Toggle eye)
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);

        JLabel balHeader = new JLabel("AVAILABLE BALANCE");
        balHeader.setFont(new Font("SansSerif", Font.BOLD, 11));
        balHeader.setForeground(TEXT_MUTED);
        balHeader.setAlignmentX(Component.RIGHT_ALIGNMENT);

        balanceLabel = new JLabel(currencyFormat.format(atm.getAccount().getBalance()));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        balanceLabel.setForeground(ACCENT_GREEN);
        balanceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton toggleEye = new JButton("👁 Toggle Visibility");
        toggleEye.setFont(new Font("SansSerif", Font.PLAIN, 11));
        toggleEye.setForeground(TEXT_MUTED);
        toggleEye.setBackground(BG_CARD);
        toggleEye.setBorderPainted(false);
        toggleEye.setContentAreaFilled(false);
        toggleEye.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleEye.setAlignmentX(Component.RIGHT_ALIGNMENT);
        toggleEye.addActionListener(e -> {
            isBalanceVisible = !isBalanceVisible;
            updateDashboardData();
        });

        right.add(balHeader);
        right.add(Box.createVerticalStrut(4));
        right.add(balanceLabel);
        right.add(Box.createVerticalStrut(4));
        right.add(toggleEye);

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JButton createMenuActionButton(String title, String subtitle, Color accentColor) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(5, 5));
        btn.setBackground(BG_CARD);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BG_CARD_HOVER, 1, true),
                new EmptyBorder(12, 15, 12, 15)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLbl.setForeground(accentColor);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLbl.setForeground(TEXT_MUTED);

        btn.add(titleLbl, BorderLayout.NORTH);
        btn.add(subLbl, BorderLayout.CENTER);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD_HOVER);
                btn.setBorder(new LineBorder(accentColor, 1, true));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_CARD);
                btn.setBorder(new LineBorder(BG_CARD_HOVER, 1, true));
            }
        });

        return btn;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateDashboardData() {
        if (isBalanceVisible) {
            balanceLabel.setText(currencyFormat.format(atm.getAccount().getBalance()));
        } else {
            balanceLabel.setText("••••••••");
        }
    }

    private void showToast(String message, Color color) {
        statusToast.setText(message);
        statusToast.setForeground(color);
    }

    /**
     * Action 1: Check Balance (Requirements 3 & 7)
     */
    private void handleCheckBalance() {
        double currentBalance = atm.checkBalance(); // Requirement 3 & 7 method
        updateDashboardData();
        showToast("Balance checked: " + currencyFormat.format(currentBalance), ACCENT_BLUE);

        JOptionPane.showMessageDialog(this,
                "<html><div style='font-family: sans-serif; padding: 10px; width: 280px;'>"
                        + "<h2 style='color:#0284C7; margin:0;'>Account Balance</h2>"
                        + "<hr style='border:0; border-top:1px solid #CBD5E1;'/>"
                        + "<p><b>Account Holder:</b> " + atm.getAccount().getAccountHolderName() + "</p>"
                        + "<p><b>Account Number:</b> " + atm.getAccount().getMaskedAccountNumber() + "</p>"
                        + "<p style='font-size:16px;'><b>Available Balance:</b> <span style='color:#16A34A;'>"
                        + currencyFormat.format(currentBalance) + "</span></p>"
                        + "</div></html>",
                "Balance Inquiry",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Action 2: Cash Deposit (Requirements 3, 6 & 7)
     */
    private void handleDepositDialog() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("Select quick deposit amount or enter a custom amount:");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField customField = new JTextField(10);
        customField.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Quick amount buttons
        JPanel quickPanel = new JPanel(new GridLayout(1, 4, 8, 8));
        double[] quickValues = { 50.0, 100.0, 500.0, 1000.0 };
        for (double val : quickValues) {
            JButton quickBtn = new JButton("₹" + (int) val);
            quickBtn.addActionListener(e -> customField.setText(String.valueOf(val)));
            quickPanel.add(quickBtn);
        }

        dialogPanel.add(infoLabel);
        dialogPanel.add(Box.createVerticalStrut(10));
        dialogPanel.add(quickPanel);
        dialogPanel.add(Box.createVerticalStrut(15));
        dialogPanel.add(new JLabel("Amount (₹):"));
        dialogPanel.add(customField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel,
                "Deposit Funds", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String text = customField.getText().trim();
            try {
                double amount = Double.parseDouble(text);
                // Call core ATM deposit method (Requirement 3, 6, 7)
                boolean success = atm.deposit(amount);
                updateDashboardData();

                if (success) {
                    showToast(atm.getLastMessage(), ACCENT_GREEN);
                    JOptionPane.showMessageDialog(this, atm.getLastMessage(), "Deposit Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showToast(atm.getLastMessage(), ACCENT_RED);
                    JOptionPane.showMessageDialog(this, atm.getLastMessage(), "Deposit Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                showToast("Invalid amount format. Please enter numbers only.", ACCENT_RED);
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric deposit amount.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Action 3: Cash Withdrawal (Requirements 3, 6 & 7)
     */
    private void handleWithdrawDialog() {
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("Select quick cash or enter a custom amount:");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTextField customField = new JTextField(10);
        customField.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Quick withdrawal presets
        JPanel quickPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        double[] quickWithdraw = { 100.0, 200.0, 500.0, 1000.0, 2000.0, 5000.0 };
        for (double val : quickWithdraw) {
            JButton quickBtn = new JButton("₹" + (int) val);
            quickBtn.addActionListener(e -> customField.setText(String.valueOf(val)));
            quickPanel.add(quickBtn);
        }

        dialogPanel.add(infoLabel);
        dialogPanel.add(Box.createVerticalStrut(10));
        dialogPanel.add(quickPanel);
        dialogPanel.add(Box.createVerticalStrut(15));
        dialogPanel.add(new JLabel("Amount (₹):"));
        dialogPanel.add(customField);

        int result = JOptionPane.showConfirmDialog(this, dialogPanel,
                "Cash Withdrawal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String text = customField.getText().trim();
            try {
                double amount = Double.parseDouble(text);
                // Call core ATM withdraw method (Requirement 3, 6, 7)
                boolean success = atm.withdraw(amount);
                updateDashboardData();

                if (success) {
                    showToast(atm.getLastMessage(), ACCENT_GREEN);
                    JOptionPane.showMessageDialog(this,
                            "<html><div style='font-family:sans-serif; width:260px;'>"
                                    + "<h3 style='color:#16A34A;'>✓ Withdrawal Approved</h3>"
                                    + "<p>" + atm.getLastMessage() + "</p>"
                                    + "<p><i>Please collect your cash dispenser output.</i></p>"
                                    + "</div></html>",
                            "Transaction Approved", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showToast(atm.getLastMessage(), ACCENT_RED);
                    JOptionPane.showMessageDialog(this,
                            "<html><div style='font-family:sans-serif; width:260px;'>"
                                    + "<h3 style='color:#DC2626;'>✗ Withdrawal Declined</h3>"
                                    + "<p>" + atm.getLastMessage() + "</p>"
                                    + "</div></html>",
                            "Transaction Declined", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                showToast("Invalid withdrawal amount. Numbers only.", ACCENT_RED);
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric withdrawal amount.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Action 4: Mini Statement Dialog
     */
    private void handleShowStatement() {
        List<Transaction> list = atm.getTransactionHistory();
        String[] columns = { "Txn ID", "Date / Time", "Type", "Amount", "Balance After", "Description" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Transaction t : list) {
            model.addRow(new Object[] {
                    t.getTransactionId(),
                    t.getFormattedTimestamp(),
                    t.getType(),
                    currencyFormat.format(t.getAmount()),
                    currencyFormat.format(t.getBalanceAfter()),
                    t.getDescription()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        // Center renderers
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(680, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Mini Statement - Transaction History",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Action 5: Print Receipt Dialog
     */
    private void handlePrintReceipt() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String receipt = String.format(
                "==========================================\n" +
                        "         AXIS BANK ATM         \n" +
                        "==========================================\n" +
                        " Terminal  : %s\n" +
                        " Location  : %s\n" +
                        " Date/Time : %s\n" +
                        " Account   : %s\n" +
                        " Name      : %s\n" +
                        "------------------------------------------\n" +
                        " Available Balance : %s\n" +
                        " Ledger Balance    : %s\n" +
                        " Status            : SUCCESS / VERIFIED\n" +
                        "==========================================\n" +
                        "   Thank you for banking with Axis Bank!   \n" +
                        "   Customer Care: 1800-0032-8746      \n" +
                        "==========================================\n",
                atm.getAtmId(),
                atm.getLocation(),
                timestamp,
                atm.getAccount().getMaskedAccountNumber(),
                atm.getAccount().getAccountHolderName(),
                currencyFormat.format(atm.getAccount().getBalance()),
                currencyFormat.format(atm.getAccount().getBalance()));

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(new Color(248, 250, 252));
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, new JScrollPane(area), "ATM Printed Receipt", JOptionPane.PLAIN_MESSAGE);
        showToast("Receipt printed successfully.", ACCENT_BLUE);
    }

    /**
     * Action 6: Change PIN Dialog
     */
    private void handleChangePinDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPasswordField currentPin = new JPasswordField();
        JPasswordField newPin = new JPasswordField();
        JPasswordField confirmPin = new JPasswordField();

        panel.add(new JLabel("Current 4-Digit PIN:"));
        panel.add(currentPin);
        panel.add(new JLabel("New 4-Digit PIN:"));
        panel.add(newPin);
        panel.add(new JLabel("Confirm New PIN:"));
        panel.add(confirmPin);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Security PIN", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cur = new String(currentPin.getPassword()).trim();
            String nw = new String(newPin.getPassword()).trim();
            String conf = new String(confirmPin.getPassword()).trim();

            if (!atm.getAccount().validatePin(cur)) {
                JOptionPane.showMessageDialog(this, "Current PIN is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nw.length() < 4 || !nw.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "New PIN must be at least 4 digits.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!nw.equals(conf)) {
                JOptionPane.showMessageDialog(this, "New PIN and Confirmation PIN do not match.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = atm.getAccount().changePin(cur, nw);
            if (ok) {
                JOptionPane.showMessageDialog(this, "PIN successfully updated!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                showToast("Security PIN updated successfully.", ACCENT_GREEN);
            }
        }
    }
}
