import java.io.Serializable;
import java.util.UUID;

public class Loan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String loanId;
    private String customerId;
    private double loanAmount;
    private double interestRate;
    private int termMonths;
    private double monthlyPayment;
    private double remainingBalance;
    private String status;
    private String applicationDate;
    private String approvalDate;
    private String purpose;

    public Loan(String customerId, double loanAmount, int termMonths, String purpose) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty.");
        }
        if (loanAmount <= 0) {
            throw new IllegalArgumentException("Loan amount must be greater than 0.");
        }
        if (termMonths <= 0) {
            throw new IllegalArgumentException("Loan term must be greater than 0.");
        }

        this.loanId = generateLoanId();
        this.customerId = customerId;
        this.loanAmount = loanAmount;
        this.interestRate = 0.045; // 4.5% annual
        this.termMonths = termMonths;
        this.monthlyPayment = calculateMonthlyPayment();
        this.remainingBalance = loanAmount;
        this.status = "PENDING";
        this.applicationDate = new java.util.Date().toString();
        this.approvalDate = "";
        this.purpose = purpose == null ? "" : purpose;
    }

    private String generateLoanId() {
        return "LOAN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private double calculateMonthlyPayment() {
        double monthlyRate = interestRate / 12.0;
        double numerator = loanAmount * monthlyRate * Math.pow(1 + monthlyRate, termMonths);
        double denominator = Math.pow(1 + monthlyRate, termMonths) - 1;

        if (denominator == 0) {
            return loanAmount / termMonths;
        }

        return numerator / denominator;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public String getStatus() {
        return status;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApprovalDate(String date) {
        this.approvalDate = date == null ? "" : date;
    }

    public String getLoanDetails() {
        String title = StringUtil.center("LOAN DETAILS", 66);

        return "\n" +
            "+--------------------------------------------------------------------+\n" +
            "| " + title + " |\n" +
            "+--------------------------------------------------------------------+\n" +
            String.format("| %-20s : %-43s |\n", "Loan ID", loanId) +
            String.format("| %-20s : %-43s |\n", "Customer ID", customerId) +
            String.format("| %-20s : RM %-40.2f |\n", "Loan Amount", loanAmount) +
            String.format("| %-20s : %-43.2f%% |\n", "Interest Rate", interestRate * 100) +
            String.format("| %-20s : %-43d |\n", "Term Months", termMonths) +
            String.format("| %-20s : RM %-40.2f |\n", "Monthly Payment", monthlyPayment) +
            String.format("| %-20s : RM %-40.2f |\n", "Remaining Balance", remainingBalance) +
            String.format("| %-20s : %-43s |\n", "Status", status) +
            String.format("| %-20s : %-43s |\n", "Purpose",
                purpose.length() > 43 ? purpose.substring(0, 40) + "..." : purpose) +
            String.format("| %-20s : %-43s |\n", "Application Date",
                applicationDate.length() > 43 ? applicationDate.substring(0, 40) + "..." : applicationDate) +
            String.format("| %-20s : %-43s |\n", "Approval Date",
                approvalDate.length() > 43 ? approvalDate.substring(0, 40) + "..." : approvalDate) +
            "+--------------------------------------------------------------------+";
    }
}