import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class FixedDeposit implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fdId;
    private String customerId;
    private double principal;
    private double interestRate;
    private int tenureMonths;
    private double maturityAmount;
    private String startDate;
    private String maturityDate;
    private String status;

    private static final double RATE_1_MONTH = 0.025;
    private static final double RATE_3_MONTH = 0.028;
    private static final double RATE_6_MONTH = 0.030;
    private static final double RATE_12_MONTH = 0.035;

    public FixedDeposit(String customerId, double principal, int tenureMonths) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be empty.");
        }
        if (principal < 1000) {
            throw new IllegalArgumentException("Minimum fixed deposit amount is RM1000.");
        }

        this.fdId = generateFdId();
        this.customerId = customerId;
        this.principal = principal;
        this.tenureMonths = tenureMonths;
        this.interestRate = getRateForTenure(tenureMonths);
        this.maturityAmount = calculateMaturityAmount();
        this.startDate = new Date().toString();
        this.maturityDate = calculateMaturityDate();
        this.status = "ACTIVE";
    }

    private String generateFdId() {
        return "FD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private double getRateForTenure(int months) {
        switch (months) {
            case 1:
                return RATE_1_MONTH;
            case 3:
                return RATE_3_MONTH;
            case 6:
                return RATE_6_MONTH;
            case 12:
                return RATE_12_MONTH;
            default:
                return RATE_3_MONTH;
        }
    }

    private double calculateMaturityAmount() {
        return principal * (1 + interestRate * tenureMonths / 12.0);
    }

    private String calculateMaturityDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, tenureMonths);
        return cal.getTime().toString();
    }

    public String getFdId() {
        return fdId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getMaturityAmount() {
        return maturityAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double withdraw() {
        if ("WITHDRAWN".equals(status)) {
            return 0;
        }

        if ("ACTIVE".equals(status)) {
            double earlyWithdrawPenalty = principal * 0.01;
            double amount = principal - earlyWithdrawPenalty;
            status = "WITHDRAWN";
            return amount;
        } else if ("MATURED".equals(status)) {
            status = "WITHDRAWN";
            return maturityAmount;
        }

        return 0;
    }

    public String getFdDetails() {
        String title = StringUtil.center("FIXED DEPOSIT DETAILS", 66);

        return "\n" +
            "+--------------------------------------------------------------------+\n" +
            "| " + title + " |\n" +
            "+--------------------------------------------------------------------+\n" +
            String.format("| %-20s : %-43s |\n", "FD ID", fdId) +
            String.format("| %-20s : %-43s |\n", "Customer ID", customerId) +
            String.format("| %-20s : RM %-40.2f |\n", "Principal", principal) +
            String.format("| %-20s : %-43.2f%% |\n", "Interest Rate", interestRate * 100) +
            String.format("| %-20s : %-43d |\n", "Tenure Months", tenureMonths) +
            String.format("| %-20s : RM %-40.2f |\n", "Maturity Amount", maturityAmount) +
            String.format("| %-20s : %-43s |\n", "Start Date",
                startDate.length() > 43 ? startDate.substring(0, 40) + "..." : startDate) +
            String.format("| %-20s : %-43s |\n", "Maturity Date",
                maturityDate.length() > 43 ? maturityDate.substring(0, 40) + "..." : maturityDate) +
            String.format("| %-20s : %-43s |\n", "Status", status) +
            "+--------------------------------------------------------------------+";
    }
}