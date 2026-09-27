
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single banking transaction performed on an account.
 */
public class Transaction {
    public enum Type {
        DEPOSIT,
        WITHDRAWAL,
        PIN_CHANGE
    }

    private final String transactionId;
    private final Type type;
    private final double amount;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(Type type, double amount, double balanceAfter, String description) {
        this.transactionId = "TXN" + (System.currentTimeMillis() % 10000000);
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Type getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | %-11s | Amount: $%,10.2f | Balance: $%,10.2f | %s",
                getFormattedTimestamp(), transactionId, type, amount, balanceAfter, description);
    }
}
