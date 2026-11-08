public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    private static double baseInterestRate = 0.025; //2.5%
    private static final double MIN_BALANCE = 100.0;

    public SavingsAccount(String customerId, double initialDeposit) {
        super(customerId, initialDeposit, "Savings");

        if (initialDeposit < MIN_BALANCE) {
            throw new IllegalArgumentException("Minimum balance for Savings Account is RM100.");
        }
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (!isActive) return false;

        if ((balance - amount) < MIN_BALANCE) {
            return false;
        }

        balance -= amount;
        return true;
    }

    @Override
    public double calculateInterest() {
        return balance * baseInterestRate;
    }

    @Override
    public String getAccountDetails() {
        return "Account Type : Savings\n" +
               "Account No   : " + accountNumber + "\n" +
               "Balance      : RM" + String.format("%.2f", balance) + "\n" +
               "Interest Rate: " + (baseInterestRate * 100) + "%\n" +
               "Status       : " + (isActive ? "ACTIVE" : "FROZEN");
    }

    public static void setBaseInterestRate(double rate) {
        if (!ValidationUtil.isValidInterestRate(rate)) {
            System.out.println(ColorUtil.RED + "[X] Invalid interest rate!" + ColorUtil.RESET);
            return;
        }
        baseInterestRate = rate;
        System.out.println(ColorUtil.GREEN + "[OK] Interest rate updated to " + (rate * 100) + "%" + ColorUtil.RESET);
    }
}
