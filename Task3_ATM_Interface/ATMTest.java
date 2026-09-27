/**
 * Comprehensive verification test for Task 3 ATM Interface requirements.
 */
public class ATMTest {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   RUNNING AUTOMATED CHECKS FOR TASK 3 ATM        ");
        System.out.println("==================================================");

        int passed = 0;
        int total = 0;

        // Test 1: BankAccount creation & initial balance (Requirement 4)
        total++;
        BankAccount account = new BankAccount("1234567890", "Jane Doe", 1000.0, "4321");
        if (account.getBalance() == 1000.0 && account.getAccountHolderName().equals("Jane Doe")) {
            System.out.println("✔ Test 1 Passed: BankAccount initialized correctly.");
            passed++;
        } else {
            System.err.println("✘ Test 1 Failed: BankAccount initialization failed.");
        }

        // Test 2: ATM connected with BankAccount (Requirements 1 & 5)
        total++;
        ATM atm = new ATM(account);
        if (atm.getAccount() == account) {
            System.out.println("✔ Test 2 Passed: ATM connected with BankAccount.");
            passed++;
        } else {
            System.err.println("✘ Test 2 Failed: ATM connection failed.");
        }

        // Test 3: checkBalance() (Requirement 3 & 7)
        total++;
        double bal = atm.checkBalance();
        if (bal == 1000.0) {
            System.out.println("✔ Test 3 Passed: checkBalance() returned correct balance.");
            passed++;
        } else {
            System.err.println("✘ Test 3 Failed: checkBalance() returned " + bal);
        }

        // Test 4: deposit(amount) valid input (Requirements 3, 5, 6, 7)
        total++;
        boolean depSuccess = atm.deposit(500.0);
        if (depSuccess && account.getBalance() == 1500.0) {
            System.out.println("✔ Test 4 Passed: deposit(500.0) updated balance to $1,500.00.");
            passed++;
        } else {
            System.err.println("✘ Test 4 Failed: deposit() didn't update balance.");
        }

        // Test 5: deposit(amount) invalid/negative input (Requirement 6 & 7)
        total++;
        boolean depInvalid = atm.deposit(-50.0);
        if (!depInvalid && account.getBalance() == 1500.0) {
            System.out.println("✔ Test 5 Passed: deposit(-50.0) rejected with validation message.");
            passed++;
        } else {
            System.err.println("✘ Test 5 Failed: negative deposit allowed!");
        }

        // Test 6: withdraw(amount) valid input (Requirements 3, 5, 6, 7)
        total++;
        boolean withSuccess = atm.withdraw(300.0);
        if (withSuccess && account.getBalance() == 1200.0) {
            System.out.println("✔ Test 6 Passed: withdraw(300.0) updated balance to $1,200.00.");
            passed++;
        } else {
            System.err.println("✘ Test 6 Failed: withdraw() failed.");
        }

        // Test 7: withdraw(amount) insufficient balance validation (Requirement 6 & 7)
        total++;
        boolean withOverdraw = atm.withdraw(5000.0);
        if (!withOverdraw && account.getBalance() == 1200.0) {
            System.out.println("✔ Test 7 Passed: withdraw(5000.0) declined due to insufficient balance.");
            passed++;
        } else {
            System.err.println("✘ Test 7 Failed: Overdraw allowed!");
        }

        // Test 8: withdraw(amount) negative input validation (Requirement 6)
        total++;
        boolean withNegative = atm.withdraw(-100.0);
        if (!withNegative && account.getBalance() == 1200.0) {
            System.out.println("✔ Test 8 Passed: withdraw(-100.0) rejected.");
            passed++;
        } else {
            System.err.println("✘ Test 8 Failed: Negative withdrawal allowed!");
        }

        // Test 9: Transaction history tracking
        total++;
        if (account.getTransactionHistory().size() >= 3) {
            System.out.println("✔ Test 9 Passed: Transaction history recorded " + account.getTransactionHistory().size() + " events.");
            passed++;
        } else {
            System.err.println("✘ Test 9 Failed: Transaction history not recorded.");
        }

        System.out.println("==================================================");
        System.out.printf("   ALL CHECKS COMPLETE: %d / %d PASSED%n", passed, total);
        System.out.println("==================================================");

        if (passed != total) {
            System.exit(1);
        }
    }
}
