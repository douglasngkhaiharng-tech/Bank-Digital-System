import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class ReportService {
    private Bank bank;

    public ReportService() {
        bank = Bank.getInstance();
    }

    public void generateMonthlyStatement(String customerId) {
        Account account = bank.getAccountByCustomerId(customerId);
        List<Transaction> transactions = bank.getTransactionsByCustomer(customerId);

        if (account == null) {
            System.out.println(ColorUtil.RED + "[X] Account not found!" + ColorUtil.RESET);
            return;
        }

        if (transactions == null || transactions.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "[!] No transactions to generate report." + ColorUtil.RESET);
            return;
        }

        // create reports folder if not exists
        new File("reports").mkdirs();

        String fileName = "reports/statement_" + customerId + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {

            writer.println(StringUtil.createLine('=', 50));
            writer.println(StringUtil.center("MONTHLY STATEMENT", 50));
            writer.println(StringUtil.createLine('=', 50));

            writer.println("Customer ID : " + customerId);
            writer.println("Account No  : " + account.getAccountNumber());
            writer.println("Balance     : RM" + String.format("%.2f", account.getBalance()));

            writer.println(StringUtil.createLine('-', 50));
            writer.printf("%-22s %-12s %-12s%n", "DATE", "TYPE", "AMOUNT");
            writer.println(StringUtil.createLine('-', 50));

            for (Transaction t : transactions) {
                String timestamp = t.getTimestamp();
                if (timestamp != null && timestamp.length() > 19) {
                    timestamp = timestamp.substring(0, 19);
                }

                String amountStr = "RM" + String.format("%.2f", t.getAmount());

                writer.printf("%-22s %-12s %-12s%n",
                        timestamp,
                        t.getType(),
                        amountStr);
            }

            writer.println(StringUtil.createLine('-', 50));
            writer.println("END OF REPORT");

            System.out.println(ColorUtil.GREEN + "[OK] Report generated: " + fileName + ColorUtil.RESET);

        } catch (Exception e) {
            System.out.println(ColorUtil.RED + "[X] Error generating report!" + ColorUtil.RESET);
        }
    }
}