import java.io.Serializable;
import java.util.UUID;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String accountNumber;
    protected String customerId;
    protected double balance;
    protected boolean isActive;
    protected String createdAt;
    protected String accountType;

    public Account(String customerId, double initialDeposit, String accountType) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty");
        }
        if (initialDeposit < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        if (accountType == null || accountType.trim().isEmpty()) {
            throw new IllegalArgumentException("Account type cannot be empty");
        }

        this.accountNumber = generateAccountNumber();
        this.customerId = customerId.trim();
        this.balance = initialDeposit;
        this.isActive = true;
        this.createdAt = new java.util.Date().toString();
        this.accountType = accountType.trim();
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than 0");
        }
        if (!isActive) {
            throw new IllegalStateException("Account is frozen");
        }
        balance += amount;
    }

    public abstract boolean withdraw(double amount);

    public abstract double calculateInterest();

    public abstract String getAccountDetails();

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }
}