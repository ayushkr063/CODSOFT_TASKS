import java.util.List;

/**
 * Task 3 Requirement 1:
 * "Create a class to represent the ATM machine."
 * 
 * Task 3 Requirement 3:
 * "Implement methods for each option, such as withdraw(amount),
 * deposit(amount), and checkBalance()."
 * 
 * Task 3 Requirement 5:
 * "Connect the ATM class with the user's bank account class to access and
 * modify the account balance."
 * 
 * Task 3 Requirement 6:
 * "Validate user input to ensure it is within acceptable limits (e.g.,
 * sufficient balance for withdrawals)."
 * 
 * Task 3 Requirement 7:
 * "Display appropriate messages to the user based on their chosen options and
 * the success or failure of their transactions."
 */
public class ATM {
    private final BankAccount account;
    private final String atmId;
    private final String location;
    private String lastMessage;

    // Safety limits for ATM operations
    public static final double MAX_SINGLE_WITHDRAWAL = 10000.00;
    public static final double MIN_TRANSACTION_AMOUNT = 100.00;
    public static final double MAX_SINGLE_DEPOSIT = 50000.00;

    /**
     * Connects this ATM machine with a specific user's bank account.
     *
     * @param account the connected bank account
     */
    public ATM(BankAccount account) {
        this(account, "ATM-CS-101", "Axis Bank Financial Hub");
    }

    public ATM(BankAccount account, String atmId, String location) {
        if (account == null) {
            throw new IllegalArgumentException("ATM must be initialized with a valid BankAccount.");
        }
        this.account = account;
        this.atmId = atmId;
        this.location = location;
        this.lastMessage = "ATM initialized successfully.";
    }

    /**
     * Requirement 3: checkBalance()
     * Requirement 7: Display appropriate message to the user
     * 
     * @return current balance of the connected account
     */
    public double checkBalance() {
        double currentBalance = account.getBalance();
        this.lastMessage = String.format("Current Available Balance: ₹%,.2f", currentBalance);
        System.out.println("\n----------------------------------------");
        System.out.println("            BALANCE INQUIRY             ");
        System.out.println("----------------------------------------");
        System.out.println("Account Holder : " + account.getAccountHolderName());
        System.out.println("Account Number : " + account.getMaskedAccountNumber());
        System.out.printf("Current Balance: ₹%,.2f%n", currentBalance);
        System.out.println("----------------------------------------\n");
        return currentBalance;
    }

    /**
     * Requirement 3: deposit(amount)
     * Requirement 6: Validate user input
     * Requirement 7: Display appropriate message for success or failure
     * 
     * @param amount the amount to deposit
     * @return true if deposit was successful, false otherwise
     */
    public boolean deposit(double amount) {
        // Validation 1: Amount must be positive and above minimum
        if (amount < MIN_TRANSACTION_AMOUNT) {
            this.lastMessage = String.format("Deposit Failed: Minimum deposit amount is ₹%,.2f.",
                    MIN_TRANSACTION_AMOUNT);
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }

        // Validation 2: Amount must not exceed max single deposit
        if (amount > MAX_SINGLE_DEPOSIT) {
            this.lastMessage = String.format("Deposit Failed: Exceeds single deposit limit of ₹%,.2f.",
                    MAX_SINGLE_DEPOSIT);
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }

        // Execute deposit on the connected bank account
        boolean success = account.deposit(amount);
        if (success) {
            this.lastMessage = String.format("Successfully deposited ₹%,.2f. New Balance: ₹%,.2f",
                    amount, account.getBalance());
            System.out.println("\n----------------------------------------");
            System.out.println("           DEPOSIT SUCCESSFUL           ");
            System.out.println("----------------------------------------");
            System.out.printf("Deposited Amount : ₹%,.2f%n", amount);
            System.out.printf("Updated Balance  : ₹%,.2f%n", account.getBalance());
            System.out.println("----------------------------------------\n");
            return true;
        } else {
            this.lastMessage = "Deposit Failed: An unexpected error occurred while updating the account.";
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }
    }

    /**
     * Requirement 3: withdraw(amount)
     * Requirement 6: Validate user input (sufficient balance, limits, positive)
     * Requirement 7: Display appropriate message for success or failure
     * 
     * @param amount the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    public boolean withdraw(double amount) {
        // Validation 1: Positive and at least minimum
        if (amount < MIN_TRANSACTION_AMOUNT) {
            this.lastMessage = String.format("Withdrawal Failed: Minimum withdrawal is ₹%,.2f.",
                    MIN_TRANSACTION_AMOUNT);
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }

        // Validation 2: Exceeding ATM single transaction limit
        if (amount > MAX_SINGLE_WITHDRAWAL) {
            this.lastMessage = String.format("Withdrawal Failed: Maximum single withdrawal is ₹%,.2f.",
                    MAX_SINGLE_WITHDRAWAL);
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }

        // Validation 3: Check for sufficient balance (Requirement 6)
        double currentBalance = account.getBalance();
        if (amount > currentBalance) {
            this.lastMessage = String.format(
                    "Withdrawal Failed: Insufficient funds! Requested: ₹%,.2f | Available: ₹%,.2f",
                    amount, currentBalance);
            System.out.println("\n----------------------------------------");
            System.out.println("          WITHDRAWAL DECLINED           ");
            System.out.println("----------------------------------------");
            System.out.println(this.lastMessage);
            System.out.println("----------------------------------------\n");
            return false;
        }

        // Execute withdrawal on the connected bank account
        boolean success = account.withdraw(amount);
        if (success) {
            this.lastMessage = String.format("Successfully withdrew ₹%,.2f. Remaining Balance: ₹%,.2f",
                    amount, account.getBalance());
            System.out.println("\n----------------------------------------");
            System.out.println("         WITHDRAWAL SUCCESSFUL          ");
            System.out.println("----------------------------------------");
            System.out.printf("Withdrawn Amount : ₹%,.2f%n", amount);
            System.out.printf("Remaining Balance: ₹%,.2f%n", account.getBalance());
            System.out.println("Please collect your cash dispenser output.");
            System.out.println("----------------------------------------\n");
            return true;
        } else {
            this.lastMessage = "Withdrawal Failed: Transaction could not be completed.";
            System.out.println("[FAILED] " + this.lastMessage);
            return false;
        }
    }

    /**
     * Helper returning structured response for GUI or external callers.
     */
    public ATMResponse depositWithResponse(double amount) {
        boolean success = deposit(amount);
        return new ATMResponse(success, lastMessage, account.getBalance());
    }

    /**
     * Helper returning structured response for GUI or external callers.
     */
    public ATMResponse withdrawWithResponse(double amount) {
        boolean success = withdraw(amount);
        return new ATMResponse(success, lastMessage, account.getBalance());
    }

    /**
     * Helper returning structured response for GUI or external callers.
     */
    public ATMResponse checkBalanceWithResponse() {
        double bal = checkBalance();
        return new ATMResponse(true, lastMessage, bal);
    }

    public BankAccount getAccount() {
        return account;
    }

    public String getAtmId() {
        return atmId;
    }

    public String getLocation() {
        return location;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public List<Transaction> getTransactionHistory() {
        return account.getTransactionHistory();
    }
}
