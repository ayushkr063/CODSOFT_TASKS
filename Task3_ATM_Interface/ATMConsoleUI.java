import java.util.List;
import java.util.Scanner;

/**
 * Task 3 Requirement 2:
 * "Design the user interface for the ATM, including options such as
 * withdrawing, depositing, and checking the balance."
 * 
 * Console-based implementation with rich formatting, PIN protection, and full
 * validation.
 */
public class ATMConsoleUI {
    private final ATM atm;
    private final Scanner scanner;

    public ATMConsoleUI(ATM atm) {
        this(atm, new Scanner(System.in));
    }

    public ATMConsoleUI(ATM atm, Scanner scanner) {
        this.atm = atm;
        this.scanner = scanner;
    }

    /**
     * Starts the ATM Console session.
     */
    public void start() {
        printWelcomeBanner();

        if (!authenticateUser()) {
            System.out.println("\n[!] Authentication ended. Returning card.");
            return;
        }

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readIntInput("Select an option (1-7): ");

            switch (choice) {
                case 1:
                    handleCheckBalance();
                    break;
                case 2:
                    handleDeposit();
                    break;
                case 3:
                    handleWithdrawal();
                    break;
                case 4:
                    handleMiniStatement();
                    break;
                case 5:
                    handleChangePin();
                    break;
                case 6:
                    handlePrintReceipt();
                    break;
                case 7:
                    running = false;
                    handleExit();
                    break;
                default:
                    System.out.println("\n[!] Invalid choice. Please select a number between 1 and 7.");
            }

            if (running) {
                pause();
            }
        }
    }

    private void printWelcomeBanner() {
        System.out.println("============================================================");
        System.out.println("               WELCOME TO AXIS BANK            ");
        System.out.println("                 Automated Teller Machine                   ");
        System.out.printf("  Terminal ID: %-15s Location: %s%n", atm.getAtmId(), atm.getLocation());
        System.out.println("============================================================");
        System.out.println("  * Insert Card: [Card Detected: " + atm.getAccount().getMaskedAccountNumber() + "]");
        System.out.println("------------------------------------------------------------");
    }

    private boolean authenticateUser() {
        int attempts = 3;
        while (attempts > 0) {
            System.out.printf("Please enter your 4-digit PIN (%d attempts remaining): ", attempts);
            if (!scanner.hasNextLine()) {
                return false;
            }
            String inputPin = scanner.nextLine().trim();

            if (atm.getAccount().validatePin(inputPin)) {
                System.out.println("\n✓ PIN verified successfully!");
                System.out.println("Welcome, " + atm.getAccount().getAccountHolderName() + "!");
                return true;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("✗ Incorrect PIN. Please try again.");
                }
            }
        }
        System.out.println("\n[!] Too many incorrect PIN attempts. For security reasons, your card is held.");
        System.out.println("[!] Please contact your nearest Axis Bank branch.");
        return false;
    }

    private void displayMenu() {
        System.out.println("\n============================================================");
        System.out.println("                         ATM MAIN MENU                      ");
        System.out.println("============================================================");
        System.out.println("  [1] Check Account Balance");
        System.out.println("  [2] Deposit Funds");
        System.out.println("  [3] Withdraw Cash");
        System.out.println("  [4] Mini Statement (Transaction History)");
        System.out.println("  [5] Change Security PIN");
        System.out.println("  [6] Print ATM Receipt");
        System.out.println("  [7] Eject Card & Exit");
        System.out.println("============================================================");
    }

    private void handleCheckBalance() {
        atm.checkBalance();
    }

    private void handleDeposit() {
        System.out.println("\n----------------------------------------");
        System.out.println("             CASH DEPOSIT               ");
        System.out.println("----------------------------------------");
        double amount = readDoubleInput("Enter amount to deposit (₹): ");
        atm.deposit(amount);
    }

    private void handleWithdrawal() {
        System.out.println("\n----------------------------------------");
        System.out.println("            CASH WITHDRAWAL             ");
        System.out.println("----------------------------------------");
        System.out.println("Quick Cash Options:");
        System.out.println("  [1] ₹100       [2] ₹200");
        System.out.println("  [3] ₹500      [4] ₹1000");
        System.out.println("  [5] ₹2000      [6] Other Custom Amount");
        System.out.println("  [7] Cancel");

        int quickChoice = readIntInput("Select option (1-7): ");
        double amount = 0;

        switch (quickChoice) {
            case 1:
                amount = 100.0;
                break;
            case 2:
                amount = 200.0;
                break;
            case 3:
                amount = 500.0;
                break;
            case 4:
                amount = 1000.0;
                break;
            case 5:
                amount = 2000.0;
                break;
            case 6:
                amount = readDoubleInput("Enter custom withdrawal amount (₹): ");
                break;
            case 7:
                System.out.println("Withdrawal transaction cancelled.");
                return;
            default:
                System.out.println("Invalid selection. Transaction cancelled.");
                return;
        }

        atm.withdraw(amount);
    }

    private void handleMiniStatement() {
        List<Transaction> transactions = atm.getTransactionHistory();
        System.out.println("\n============================================================");
        System.out.println("             MINI STATEMENT - RECENT TRANSACTIONS           ");
        System.out.println("============================================================");
        System.out.println("Account Holder : " + atm.getAccount().getAccountHolderName());
        System.out.println("Account Number : " + atm.getAccount().getMaskedAccountNumber());
        System.out.println("------------------------------------------------------------");

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            int start = Math.max(0, transactions.size() - 10);
            for (int i = start; i < transactions.size(); i++) {
                System.out.println(transactions.get(i).toString());
            }
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("Current Balance: ₹%,.2f%n", atm.getAccount().getBalance());
        System.out.println("============================================================");
    }

    private void handleChangePin() {
        System.out.println("\n----------------------------------------");
        System.out.println("          CHANGE SECURITY PIN           ");
        System.out.println("----------------------------------------");
        System.out.print("Enter current 4-digit PIN: ");
        if (!scanner.hasNextLine())
            return;
        String oldPin = scanner.nextLine().trim();

        if (!atm.getAccount().validatePin(oldPin)) {
            System.out.println("✗ Current PIN is incorrect. Request denied.");
            return;
        }

        System.out.print("Enter new 4-digit PIN: ");
        if (!scanner.hasNextLine())
            return;
        String newPin = scanner.nextLine().trim();

        if (newPin.length() < 4 || !newPin.matches("\\d+")) {
            System.out.println("✗ Invalid PIN format. PIN must be at least 4 digits.");
            return;
        }

        System.out.print("Confirm new 4-digit PIN: ");
        if (!scanner.hasNextLine())
            return;
        String confirmPin = scanner.nextLine().trim();

        if (!newPin.equals(confirmPin)) {
            System.out.println("✗ PIN mismatch! New PIN and Confirmation did not match.");
            return;
        }

        boolean success = atm.getAccount().changePin(oldPin, newPin);
        if (success) {
            System.out.println("✓ Your PIN has been successfully updated!");
        } else {
            System.out.println("✗ Failed to update PIN.");
        }
    }

    private void handlePrintReceipt() {
        System.out.println("\n========================================");
        System.out.println("          AXIS BANK RECEIPT          ");
        System.out.println("========================================");
        System.out.println("Date/Time: " + java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("ATM ID   : " + atm.getAtmId());
        System.out.println("Location : " + atm.getLocation());
        System.out.println("Card No  : " + atm.getAccount().getMaskedAccountNumber());
        System.out.println("Holder   : " + atm.getAccount().getAccountHolderName());
        System.out.printf("Balance  : ₹%,.2f%n", atm.getAccount().getBalance());
        System.out.println("----------------------------------------");
        System.out.println("   Thank you for banking with Axis Bank!   ");
        System.out.println("========================================");
    }

    private void handleExit() {
        System.out.println("\n============================================================");
        System.out.println("Card ejected. Please remove your card.");
        System.out.println("Thank you for choosing Axis Bank. Have a great day!");
        System.out.println("============================================================\n");
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 7; // default exit
            }
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a whole number.");
            }
        }
    }

    private double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0.0;
            }
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric amount. Please enter a valid decimal number (e.g. 50.00).");
            }
        }
    }

    private void pause() {
        System.out.print("\nPress [Enter] key to return to main menu...");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}
