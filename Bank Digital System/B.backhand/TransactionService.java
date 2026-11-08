import java.util.List;

public class TransactionService {
    private Bank bank;
    
    public TransactionService() {
        bank = Bank.getInstance();
    }
    
    public void deposit(String customerId, double amount) {
        if (!ValidationUtil.isValidCustomerId(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid Customer ID!" + ColorUtil.RESET);
            return;
        }

        if (!ValidationUtil.isValidAmount(amount, 0.01, 1000000)) {
            System.out.println(ColorUtil.RED + "[X] Deposit amount must be between RM0.01 and RM1,000,000!" + ColorUtil.RESET);
            return;
        }
        
        Account account = bank.getAccountByCustomerId(customerId);
        
        if (account == null) {
            System.out.println(ColorUtil.RED + "[X] Account not found!" + ColorUtil.RESET);
            return;
        }
        
        if (!account.isActive()) {
            System.out.println(ColorUtil.RED + "[X] Account is frozen! Cannot deposit." + ColorUtil.RESET);
            return;
        }
        
        double previousBalance = account.getBalance();
        account.deposit(amount);

        bank.addAccount(account);
        
        Transaction transaction = new Transaction(
            account.getAccountNumber(),
            customerId,
            "DEPOSIT",
            amount,
            account.getBalance(),
            "Deposit of RM" + String.format("%.2f", amount)
        );
        bank.addTransaction(transaction);
        
        System.out.println(ColorUtil.GREEN + String.format("\n[OK] Deposit successful! RM%.2f credited.", amount) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + String.format("  Previous Balance: RM%.2f -> New Balance: RM%.2f", previousBalance, account.getBalance()) + ColorUtil.RESET);
    }
    
    public void withdraw(String customerId, double amount) {
        if (!ValidationUtil.isValidCustomerId(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid Customer ID!" + ColorUtil.RESET);
            return;
        }

        if (!ValidationUtil.isValidAmount(amount, 0.01, 50000)) {
            System.out.println(ColorUtil.RED + "[X] Withdrawal amount must be between RM0.01 and RM50,000!" + ColorUtil.RESET);
            return;
        }
        
        Account account = bank.getAccountByCustomerId(customerId);
        
        if (account == null) {
            System.out.println(ColorUtil.RED + "[X] Account not found!" + ColorUtil.RESET);
            return;
        }
        
        if (!account.isActive()) {
            System.out.println(ColorUtil.RED + "[X] Account is frozen! Cannot withdraw." + ColorUtil.RESET);
            return;
        }
        
        double previousBalance = account.getBalance();
        
        try {
            boolean success = account.withdraw(amount);
            
            if (!success) {
                throw new InsufficientBalanceException("Withdrawal failed! Please check your balance and account status.");
            }

            bank.addAccount(account);
            
            Transaction transaction = new Transaction(
                account.getAccountNumber(),
                customerId,
                "WITHDRAWAL",
                amount,
                account.getBalance(),
                "Withdrawal of RM" + String.format("%.2f", amount)
            );
            bank.addTransaction(transaction);
            
            System.out.println(ColorUtil.GREEN + String.format("\n[OK] Withdrawal successful! RM%.2f debited.", amount) + ColorUtil.RESET);
            System.out.println(ColorUtil.CYAN + String.format("  Previous Balance: RM%.2f -> New Balance: RM%.2f", previousBalance, account.getBalance()) + ColorUtil.RESET);

        } catch (InsufficientBalanceException e) {
            System.out.println(ColorUtil.RED + "[X] " + e.getMessage() + ColorUtil.RESET);
        }
    }
    
    public void transfer(String fromCustomerId, String toCustomerId, double amount) {
        if (!ValidationUtil.isValidCustomerId(fromCustomerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid sender Customer ID!" + ColorUtil.RESET);
            return;
        }

        if (!ValidationUtil.isValidCustomerId(toCustomerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid recipient Customer ID!" + ColorUtil.RESET);
            return;
        }

        if (fromCustomerId.equals(toCustomerId)) {
            System.out.println(ColorUtil.RED + "[X] Cannot transfer to the same account!" + ColorUtil.RESET);
            return;
        }

        if (!ValidationUtil.isValidAmount(amount, 0.01, 50000)) {
            System.out.println(ColorUtil.RED + "[X] Transfer amount must be between RM0.01 and RM50,000!" + ColorUtil.RESET);
            return;
        }
        
        Account fromAccount = bank.getAccountByCustomerId(fromCustomerId);
        Account toAccount = bank.getAccountByCustomerId(toCustomerId);
        
        if (fromAccount == null) {
            System.out.println(ColorUtil.RED + "[X] Sender account not found!" + ColorUtil.RESET);
            return;
        }
        
        if (toAccount == null) {
            System.out.println(ColorUtil.RED + "[X] Recipient account not found!" + ColorUtil.RESET);
            return;
        }
        
        if (!fromAccount.isActive()) {
            System.out.println(ColorUtil.RED + "[X] Your account is frozen! Cannot transfer." + ColorUtil.RESET);
            return;
        }
        
        if (!toAccount.isActive()) {
            System.out.println(ColorUtil.RED + "[X] Recipient account is frozen! Cannot transfer." + ColorUtil.RESET);
            return;
        }
        
        double previousFromBalance = fromAccount.getBalance();
        
        try {
            boolean withdrawSuccess = fromAccount.withdraw(amount);
            
            if (!withdrawSuccess) {
                throw new InsufficientBalanceException("Transfer failed! Insufficient funds or overdraft limit exceeded.");
            }

            toAccount.deposit(amount);

            bank.addAccount(fromAccount);
            bank.addAccount(toAccount);
            
            Transaction outTransaction = new Transaction(
                fromAccount.getAccountNumber(),
                fromCustomerId,
                "TRANSFER_OUT",
                amount,
                fromAccount.getBalance(),
                "Transfer to " + toCustomerId + " of RM" + String.format("%.2f", amount)
            );
            bank.addTransaction(outTransaction);
            
            Transaction inTransaction = new Transaction(
                toAccount.getAccountNumber(),
                toCustomerId,
                "TRANSFER_IN",
                amount,
                toAccount.getBalance(),
                "Transfer from " + fromCustomerId + " of RM" + String.format("%.2f", amount)
            );
            bank.addTransaction(inTransaction);
            
            System.out.println(ColorUtil.GREEN + String.format("\n[OK] Transfer successful! RM%.2f sent to %s", amount, toCustomerId) + ColorUtil.RESET);
            System.out.println(ColorUtil.CYAN + String.format("  Your Balance: RM%.2f -> RM%.2f", previousFromBalance, fromAccount.getBalance()) + ColorUtil.RESET);

        } catch (InsufficientBalanceException e) {
            System.out.println(ColorUtil.RED + "[X] " + e.getMessage() + ColorUtil.RESET);
        }
    }
    
    public void viewTransactionHistory(String customerId) {
        if (!ValidationUtil.isValidCustomerId(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid Customer ID!" + ColorUtil.RESET);
            return;
        }

        List<Transaction> transactions = bank.getTransactionsByCustomer(customerId);
        Account account = bank.getAccountByCustomerId(customerId);

        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 100) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("TRANSACTION HISTORY", 100) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 100) + ColorUtil.RESET);

        if (transactions == null || transactions.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No transactions found for this account." + ColorUtil.RESET);
            return;
        }

        System.out.printf("%-22s %-15s %-16s %-18s %-20s%n",
            "Date & Time", "Type", "Amount", "Balance After", "Reference");
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 100) + ColorUtil.RESET);

        for (Transaction t : transactions) {
            String amountStr;
            if (t.getType().equals("DEPOSIT") || t.getType().equals("TRANSFER_IN")) {
                amountStr = "+RM" + String.format("%.2f", t.getAmount());
            } else {
                amountStr = "-RM" + String.format("%.2f", t.getAmount());
            }

            String balanceStr = "RM" + String.format("%.2f", t.getBalanceAfter());

            String timestamp = t.getTimestamp();
            if (timestamp != null && timestamp.length() > 19) {
                timestamp = timestamp.substring(0, 19);
            }

            String reference = t.getReferenceId();
            if (reference == null) {
                reference = "";
            }

            System.out.printf("%-22s %-15s %-16s %-18s %-20s%n",
                timestamp,
                t.getType(),
                amountStr,
                balanceStr,
                reference);
        }

        if (account != null) {
            System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 100) + ColorUtil.RESET);
            System.out.println(ColorUtil.CYAN_BOLD + String.format("Current Balance: RM%.2f", account.getBalance()) + ColorUtil.RESET);
        }
    }
    
    public void viewSuspiciousTransactions() {
        List<Transaction> allTransactions = bank.getAllTransactions();
        
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("SUSPICIOUS TRANSACTIONS (> RM10,000)", 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        
        if (allTransactions == null || allTransactions.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No transactions found in the system." + ColorUtil.RESET);
            return;
        }
        
        boolean found = false;
        System.out.printf("%-20s %-15s %-15s %-15s%n", "Date & Time", "Customer ID", "Type", "Amount");
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 80) + ColorUtil.RESET);

        for (Transaction t : allTransactions) {
            if (t.getAmount() > 10000) {
                found = true;
                String timestamp = t.getTimestamp();
                if (timestamp != null && timestamp.length() > 19) {
                    timestamp = timestamp.substring(0, 19);
                }
                System.out.printf("%-20s %-15s %-15s %-15s%n",
                    timestamp,
                    t.getCustomerId(),
                    t.getType(),
                    "RM" + String.format("%.2f", t.getAmount()));
            }
        }
        
        if (!found) {
            System.out.println(ColorUtil.GREEN + "No suspicious transactions found." + ColorUtil.RESET);
        }
    }
}