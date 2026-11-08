public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    private static final double OVERDRAFT_LIMIT = 5000.0;
    private double overdraftUsed = 0.0;

    public CurrentAccount(String customerId, double initialDeposit) {
        super(customerId, initialDeposit, "Current");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (!isActive) return false;

        double available = balance + (OVERDRAFT_LIMIT - overdraftUsed);

        if (amount > available) {
            return false;
        }

        if (amount <= balance) {
            balance -= amount;
        } else {
            double remainder = amount - balance;
            balance = 0;
            overdraftUsed += remainder;
        }

        return true;
    }

    @Override
    public double calculateInterest() {
        return 0.0;
    }

    public double getOverdraftUsed() {
        return overdraftUsed;
    }

    public double getAvailableOverdraft() {
        return OVERDRAFT_LIMIT - overdraftUsed;
    }

    @Override
    public String getAccountDetails() {
        String title = StringUtil.center("CURRENT ACCOUNT DETAILS", 66);

        return "\n" +
            "+--------------------------------------------------------------------+\n" +
            "| " + title + " |\n" +
            "+--------------------------------------------------------------------+\n" +
            String.format("| %-20s : %-43s |\n", "Account Number", accountNumber) +
            String.format("| %-20s : %-43s |\n", "Customer ID", customerId) +
            String.format("| %-20s : RM %-40.2f |\n", "Balance", balance) +
            String.format("| %-20s : RM %-40.2f |\n", "Overdraft Limit", OVERDRAFT_LIMIT) +
            String.format("| %-20s : RM %-40.2f |\n", "Overdraft Used", overdraftUsed) +
            String.format("| %-20s : RM %-40.2f |\n", "Available Credit", getAvailableOverdraft()) +
            String.format("| %-20s : %-43s |\n", "Status", isActive ? "Active" : "Frozen") +
            String.format("| %-20s : %-43s |\n", "Created Date",
                createdAt.length() > 43 ? createdAt.substring(0, 40) + "..." : createdAt) +
            "+--------------------------------------------------------------------+";
    }
}