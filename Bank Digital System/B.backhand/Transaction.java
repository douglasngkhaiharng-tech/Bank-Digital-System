import java.io.Serializable;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String accountNumber;
    private String customerId;
    private String type;
    private double amount;
    private double balanceAfter;
    private String timestamp;
    private String referenceId;
    private String description;

    public Transaction(String accountNumber, String customerId, String type,
                       double amount, double balanceAfter, String description) {
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = new java.util.Date().toString();
        this.referenceId = generateReferenceId();
        this.description = description;
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateReferenceId() {
        return "REF" + System.currentTimeMillis();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionDetails() {
        String title = StringUtil.center("TRANSACTION DETAILS", 66);

        return "\n" +
            "+--------------------------------------------------------------------+\n" +
            "| " + title + " |\n" +
            "+--------------------------------------------------------------------+\n" +
            String.format("| %-20s : %-43s |\n", "Transaction ID", transactionId) +
            String.format("| %-20s : %-43s |\n", "Account Number", accountNumber) +
            String.format("| %-20s : %-43s |\n", "Customer ID", customerId) +
            String.format("| %-20s : %-43s |\n", "Type", type) +
            String.format("| %-20s : RM %-40.2f |\n", "Amount", amount) +
            String.format("| %-20s : RM %-40.2f |\n", "Balance After", balanceAfter) +
            String.format("| %-20s : %-43s |\n", "Reference ID", referenceId) +
            String.format("| %-20s : %-43s |\n", "Timestamp",
                timestamp.length() > 43 ? timestamp.substring(0, 40) + "..." : timestamp) +
            String.format("| %-20s : %-43s |\n", "Description",
                description != null && description.length() > 43 ? description.substring(0, 40) + "..." : description) +
            "+--------------------------------------------------------------------+";
    }
}