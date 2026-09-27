/**
 * Encapsulates the outcome of an ATM operation for UI consumption.
 */
public class ATMResponse {
    private final boolean success;
    private final String message;
    private final double balance;

    public ATMResponse(boolean success, String message, double balance) {
        this.success = success;
        this.message = message;
        this.balance = balance;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return (success ? "✓ SUCCESS: " : "✗ ERROR: ") + message;
    }
}
