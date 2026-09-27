import java.awt.GraphicsEnvironment;
import java.util.*;
import javax.swing.SwingUtilities;

/**
 * Entry point for CodSoft Task 3: ATM Interface. Allows running either the
 * Graphical User Interface (Swing GUI) or the interactive Terminal/Console
 * Interface (CLI).
 */
public class Main {

    public static void main(String[] args) {
        // Step 1: Create the User's Bank Account (Requirement 4)
        BankAccount userAccount = new BankAccount(
                "462783638478936",
                "Ayush Kumar Seth",
                2500.00,
                "9865");

        // Step 2: Create the ATM machine and connect it with the Bank Account
        // (Requirements 1 & 5)
        ATM atm = new ATM(userAccount, "ATM-CS-101", "CodSoft Downtown Financial Hub");

        // Step 3: Handle CLI flag overrides
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--cli") || args[0].equalsIgnoreCase("-c")) {
                launchConsole(atm);
                return;
            } else if (args[0].equalsIgnoreCase("--gui") || args[0].equalsIgnoreCase("-g")) {
                launchGui(atm);
                return;
            }
        }

        // Check if graphical desktop environment is available
        boolean canLaunchGui = !GraphicsEnvironment.isHeadless();

        if (!canLaunchGui) {
            System.out.println("[*] Headless environment detected. Launching Console interface...");
            launchConsole(atm);
            return;
        }

        // Present interactive selector or launch GUI
        System.out.println("============================================================");
        System.out.println("               CODSOFT TASK 3: ATM INTERFACE                ");
        System.out.println("============================================================");
        System.out.println("  Initial Demo Account Details:");
        System.out.println("  • Account Holder : " + userAccount.getAccountHolderName());
        System.out.println("  • Account Number : " + userAccount.getMaskedAccountNumber());
        System.out.printf("  • Starting Balance: $%,.2f%n", userAccount.getBalance());
        System.out.println("  • Default PIN    : 1234");
        System.out.println("============================================================");
        System.out.println("  Choose your preferred interface:");
        System.out.println("  [1] Modern Graphical User Interface (Swing GUI) [Recommended]");
        System.out.println("  [2] Interactive Command-Line Interface (Terminal CLI)");
        System.out.print("\nEnter choice (1 or 2, default is 1): ");

        Scanner scanner = new Scanner(System.in);
        String input = "";
        try {
            if (scanner.hasNextLine()) {
                input = scanner.nextLine().trim();
            }
        } catch (Exception ignored) {
        }

        if (input.equals("2")) {
            launchConsole(atm, scanner);
        } else {
            System.out.println("\nLaunching Modern ATM GUI... (You can also pass --cli to run in terminal)");
            launchGui(atm);
        }
    }

    private static void launchGui(ATM atm) {
        SwingUtilities.invokeLater(() -> {
            ATMGui gui = new ATMGui(atm);
            gui.setVisible(true);
        });
    }

    private static void launchConsole(ATM atm) {
        launchConsole(atm, new Scanner(System.in));
    }

    private static void launchConsole(ATM atm, Scanner scanner) {
        ATMConsoleUI consoleUI = new ATMConsoleUI(atm, scanner);
        consoleUI.start();
    }
}
