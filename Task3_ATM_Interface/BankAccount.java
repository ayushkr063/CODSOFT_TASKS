import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Task 3 Requirement 4:
 * "Create a class to represent the user's bank account, which stores the account balance."
 */
public class BankAccount {
    private final String accountNumber;
    private final String accountHolderName;
    private double balance;
    private String pin;
    private final List<Transaction> transactionHistory;

    /**
     * Constructs a BankAccount with initial balance and details.
     *
     * @param accountNumber     the unique account number
     * @param accountHolderName the name of the account holder
     * @param initialBalance    the starting balance
     * @param pin               the 4-digit security PIN
     */
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance, String pin) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be empty.");
        }
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        if (pin == null || pin.length() < 4) {
            throw new IllegalArgumentException("PIN must be at least 4 digits.");
        }

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.pin = pin;
        this.transactionHistory = new ArrayList<>();

        // Record initial deposit transaction if positive
        if (initialBalance > 0) {
            this.transactionHistory.add(new Transaction(
                    Transaction.Type.DEPOSIT,
                    initialBalance,
                    initialBalance,
                    "Initial account opening balance"
            ));
        }
    }

    /**
     * Returns the current account balance.
     *
     * @return current balance
     */
    public synchronized double getBalance() {
        return balance;
    }

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to deposit (must be positive)
     * @return true if deposit succeeded, false otherwise
     */
    public synchronized boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        this.transactionHistory.add(new Transaction(
                Transaction.Type.DEPOSIT,
                amount,
                this.balance,
                "Cash deposit via ATM"
        ));
        return true;
    }

    /**
     * Withdraws the specified amount from the account if balance is sufficient.
     *
     * @param amount the amount to withdraw
     * @return true if withdrawal succeeded, false otherwise
     */
    public synchronized boolean withdraw(double amount) {
        if (amount <= 0 || amount > this.balance) {
            return false;
        }
        this.balance -= amount;
        this.transactionHistory.add(new Transaction(
                Transaction.Type.WITHDRAWAL,
                amount,
                this.balance,
                "Cash withdrawal via ATM"
        ));
        return true;
    }

    /**
     * Validates if the supplied PIN matches the account PIN.
     *
     * @param enteredPin input PIN
     * @return true if valid, false otherwise
     */
    public boolean validatePin(String enteredPin) {
        return this.pin != null && this.pin.equals(enteredPin);
    }

    /**
     * Updates the account PIN after verifying old PIN.
     *
     * @param oldPin current PIN
     * @param newPin new PIN (at least 4 digits)
     * @return true if successfully updated, false otherwise
     */
    public synchronized boolean changePin(String oldPin, String newPin) {
        if (!validatePin(oldPin)) {
            return false;
        }
        if (newPin == null || newPin.length() < 4) {
            return false;
        }
        this.pin = newPin;
        this.transactionHistory.add(new Transaction(
                Transaction.Type.PIN_CHANGE,
                0.0,
                this.balance,
                "ATM Security: PIN successfully changed"
        ));
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getMaskedAccountNumber() {
        if (accountNumber.length() <= 4) {
            return accountNumber;
        }
        return "•••• " + accountNumber.substring(accountNumber.length() - 4);
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(new ArrayList<>(transactionHistory));
    }
}
