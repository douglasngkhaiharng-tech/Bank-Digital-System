import java.io.*;
import java.util.*;

public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Bank instance;

    private Map<String, Customer> customers;
    private Map<String, Account> accounts;
    private List<Transaction> transactions;
    private List<Loan> loans;
    private List<FixedDeposit> fixedDeposits;

    private Bank() {
        customers = new HashMap<>();
        accounts = new HashMap<>();
        transactions = new ArrayList<>();
        loans = new ArrayList<>();
        fixedDeposits = new ArrayList<>();
        loadData();
    }

    public static synchronized Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    // ==================== CUSTOMER OPERATIONS ====================

    public void addCustomer(Customer customer) {
        if (customer == null) return;
        customers.put(customer.getCustomerId(), customer);
        saveData();
    }

    public Customer getCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) return null;
        return customers.get(customerId);
    }

    public Map<String, Customer> getAllCustomers() {
        return new HashMap<>(customers);
    }

    public boolean customerExists(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) return false;
        return customers.containsKey(customerId);
    }

    public boolean removeCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) return false;

        Customer customer = customers.get(customerId);
        if (customer == null) return false;

        List<String> customerAccounts = new ArrayList<>(customer.getAccountNumbers());
        for (String accountNumber : customerAccounts) {
            accounts.remove(accountNumber);
        }

        customers.remove(customerId);
        saveData();
        return true;
    }

    // ==================== ACCOUNT OPERATIONS ====================

    public void addAccount(Account account) {
        if (account == null) return;
        accounts.put(account.getAccountNumber(), account);

        Customer customer = customers.get(account.getCustomerId());
        if (customer != null) {
            List<String> accountNumbers = customer.getAccountNumbers();
            if (!accountNumbers.contains(account.getAccountNumber())) {
                customer.addAccountNumber(account.getAccountNumber());
            }
        }
        saveData();
    }

    public Account getAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) return null;
        return accounts.get(accountNumber);
    }

    public Account getAccountByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) return null;
        for (Account account : accounts.values()) {
            if (account.getCustomerId().equals(customerId)) return account;
        }
        return null;
    }

    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }

    public boolean removeAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) return false;

        Account removedAccount = accounts.remove(accountNumber);
        if (removedAccount == null) return false;

        Customer customer = customers.get(removedAccount.getCustomerId());
        if (customer != null) {
            customer.removeAccountNumber(accountNumber);
        }

        saveData();
        return true;
    }

    // ==================== TRANSACTION OPERATIONS ====================

    public void addTransaction(Transaction transaction) {
        if (transaction == null) return;
        transactions.add(transaction);
        saveData();
    }

    public List<Transaction> getTransactionsByCustomer(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) return new ArrayList<>();
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getCustomerId().equals(customerId)) result.add(t);
        }
        return result;
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    // ==================== LOAN OPERATIONS ====================

    public void addLoan(Loan loan) {
        if (loan == null) return;

        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i).getLoanId().equals(loan.getLoanId())) {
                loans.set(i, loan);
                saveData();
                return;
            }
        }

        loans.add(loan);
        saveData();
    }

    public List<Loan> getLoansByCustomer(String customerId) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getCustomerId().equals(customerId)) result.add(loan);
        }
        return result;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public Loan getLoanById(String loanId) {
        for (Loan loan : loans) {
            if (loan.getLoanId().equals(loanId)) return loan;
        }
        return null;
    }

    public List<Loan> getPendingLoans() {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getStatus().equals("PENDING")) result.add(loan);
        }
        return result;
    }

    // ==================== FIXED DEPOSIT OPERATIONS ====================

    public void addFixedDeposit(FixedDeposit fd) {
        if (fd == null) return;

        for (int i = 0; i < fixedDeposits.size(); i++) {
            if (fixedDeposits.get(i).getFdId().equals(fd.getFdId())) {
                fixedDeposits.set(i, fd);
                saveData();
                return;
            }
        }

        fixedDeposits.add(fd);
        saveData();
    }

    public List<FixedDeposit> getFixedDepositsByCustomer(String customerId) {
        List<FixedDeposit> result = new ArrayList<>();
        for (FixedDeposit fd : fixedDeposits) {
            if (fd.getCustomerId().equals(customerId)) result.add(fd);
        }
        return result;
    }

    public List<FixedDeposit> getAllFixedDeposits() {
        return new ArrayList<>(fixedDeposits);
    }

    public FixedDeposit getFixedDepositById(String fdId) {
        for (FixedDeposit fd : fixedDeposits) {
            if (fd.getFdId().equals(fdId)) return fd;
        }
        return null;
    }

    public List<Customer> getCustomersByEmail(String email) {
        List<Customer> result = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            return result;
        }

        for (Customer customer : customers.values()) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())) {
                result.add(customer);
            }
        }

        return result;
    }

    public boolean removeCustomersByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        List<Customer> matchedCustomers = getCustomersByEmail(email);
        if (matchedCustomers.isEmpty()) {
            return false;
        }

        for (Customer customer : matchedCustomers) {
            List<String> customerAccounts = new ArrayList<>(customer.getAccountNumbers());
            for (String accountNumber : customerAccounts) {
                accounts.remove(accountNumber);
            }

            customers.remove(customer.getCustomerId());
        }

        saveData();
        return true;
    }

    public Customer getCustomerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        for (Customer customer : customers.values()) {
            if (customer.getEmail().equalsIgnoreCase(email.trim())) {
                return customer;
            }
        }
        return null;
    }

    public boolean removeCustomerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        Customer customer = getCustomerByEmail(email);
        if (customer == null) {
            return false;
        }

        List<String> customerAccounts = new ArrayList<>(customer.getAccountNumbers());
        for (String accountNumber : customerAccounts) {
            accounts.remove(accountNumber);
        }

        customers.remove(customer.getCustomerId());
        saveData();
        return true;
    }

    // ==================== DATA STORAGE ====================

    public void saveAllData() {
        saveData();
    }

    private void saveData() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();

            saveCustomers();
            saveAccounts();
            saveTransactions();
            saveLoans();
            saveFixedDeposits();
        } catch (Exception e) {
            System.out.println(ColorUtil.YELLOW + "Warning: Could not save data" + ColorUtil.RESET);
        }
    }

    private void saveCustomers() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter("data/customers.txt"))) {
            w.println("CUSTOMER_ID|NAME|EMAIL|PHONE|ADDRESS|PASSWORD|REGISTERED_DATE|IS_ACTIVE|ACCOUNT_NUMBERS");
            for (Customer c : customers.values()) {
                String accNums = String.join(",", c.getAccountNumbers());
                w.printf("%s|%s|%s|%s|%s|%s|%s|%b|%s%n",
                    c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone(),
                    c.getAddress(), c.getPassword(), c.getRegisteredDate(), c.isActive(), accNums);
            }
        }
    }

    private void saveAccounts() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter("data/accounts.txt"))) {
            w.println("ACCOUNT_NUMBER|CUSTOMER_ID|BALANCE|TYPE|IS_ACTIVE|CREATED_AT");
            for (Account a : accounts.values()) {
                w.printf("%s|%s|%.2f|%s|%b|%s%n",
                    a.getAccountNumber(), a.getCustomerId(), a.getBalance(),
                    a.getAccountType(), a.isActive(), a.getCreatedAt());
            }
        }
    }

    private void saveTransactions() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter("data/transactions.txt"))) {
            w.println("TRANSACTION_ID|ACCOUNT_NUMBER|CUSTOMER_ID|TYPE|AMOUNT|BALANCE_AFTER|TIMESTAMP|REFERENCE_ID|DESCRIPTION");
            for (Transaction t : transactions) {
                w.printf("%s|%s|%s|%s|%.2f|%.2f|%s|%s|%s%n",
                    t.getTransactionId(), t.getAccountNumber(), t.getCustomerId(),
                    t.getType(), t.getAmount(), t.getBalanceAfter(),
                    t.getTimestamp(), t.getReferenceId(), t.getDescription());
            }
        }
    }

    private void saveLoans() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter("data/loans.txt"))) {
            w.println("LOAN_ID|CUSTOMER_ID|AMOUNT|INTEREST_RATE|TERM_MONTHS|MONTHLY_PAYMENT|REMAINING|STATUS|APPLICATION_DATE|APPROVAL_DATE|PURPOSE");
            for (Loan l : loans) {
                w.printf("%s|%s|%.2f|%.4f|%d|%.2f|%.2f|%s|%s|%s|%s%n",
                    l.getLoanId(), l.getCustomerId(), l.getLoanAmount(),
                    l.getInterestRate(), l.getTermMonths(), l.getMonthlyPayment(),
                    l.getRemainingBalance(), l.getStatus(), l.getApplicationDate(),
                    l.getApprovalDate(), l.getPurpose());
            }
        }
    }

    private void saveFixedDeposits() throws IOException {
        try (PrintWriter w = new PrintWriter(new FileWriter("data/fixeddeposits.txt"))) {
            w.println("FD_ID|CUSTOMER_ID|PRINCIPAL|INTEREST_RATE|TENURE_MONTHS|MATURITY_AMOUNT|START_DATE|MATURITY_DATE|STATUS");
            for (FixedDeposit fd : fixedDeposits) {
                w.printf("%s|%s|%.2f|%.4f|%d|%.2f|%s|%s|%s%n",
                    fd.getFdId(), fd.getCustomerId(), fd.getPrincipal(),
                    fd.getInterestRate(), fd.getTenureMonths(), fd.getMaturityAmount(),
                    fd.getStartDate(), fd.getMaturityDate(), fd.getStatus());
            }
        }
    }

    private void loadData() {
        loadCustomers();
        loadAccounts();
        loadTransactions();
        loadLoans();
        loadFixedDeposits();
    }

    private void loadCustomers() {
        File f = new File("data/customers.txt");
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;

            while ((line = r.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] p = line.split("\\|", -1);
                if (p.length >= 8) {
                    Customer c = new Customer(p[1], p[2], p[3], p[4], p[5]);

                    java.lang.reflect.Field idField = Customer.class.getDeclaredField("customerId");
                    idField.setAccessible(true);
                    idField.set(c, p[0]);

                    java.lang.reflect.Field dateField = Customer.class.getDeclaredField("registeredDate");
                    dateField.setAccessible(true);
                    dateField.set(c, p[6]);

                    c.setActive(Boolean.parseBoolean(p[7]));

                    if (p.length >= 9 && !p[8].trim().isEmpty()) {
                        String[] accNums = p[8].split(",");
                        for (String acc : accNums) {
                            if (acc != null && !acc.trim().isEmpty()) {
                                c.addAccountNumber(acc.trim());
                            }
                        }
                    }

                    customers.put(p[0], c);
                }
            }
        } catch (Exception e) {
        }
    }

    private void loadAccounts() {
        File f = new File("data/accounts.txt");
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;

            while ((line = r.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] p = line.split("\\|", -1);
                if (p.length >= 6) {
                    Account a = null;

                    if (p[3].equals("Savings")) {
                        a = new SavingsAccount(p[1], Double.parseDouble(p[2]));
                    } else if (p[3].equals("Current")) {
                        a = new CurrentAccount(p[1], Double.parseDouble(p[2]));
                    }

                    if (a != null) {
                        java.lang.reflect.Field numField = Account.class.getDeclaredField("accountNumber");
                        numField.setAccessible(true);
                        numField.set(a, p[0]);

                        java.lang.reflect.Field activeField = Account.class.getDeclaredField("isActive");
                        activeField.setAccessible(true);
                        activeField.set(a, Boolean.parseBoolean(p[4]));

                        java.lang.reflect.Field dateField = Account.class.getDeclaredField("createdAt");
                        dateField.setAccessible(true);
                        dateField.set(a, p[5]);

                        accounts.put(p[0], a);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private void loadTransactions() {
        File f = new File("data/transactions.txt");
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;

            while ((line = r.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] p = line.split("\\|", -1);
                if (p.length >= 9) {
                    Transaction t = new Transaction(
                        p[1],
                        p[2],
                        p[3],
                        Double.parseDouble(p[4]),
                        Double.parseDouble(p[5]),
                        p[8]
                    );

                    java.lang.reflect.Field idField = Transaction.class.getDeclaredField("transactionId");
                    idField.setAccessible(true);
                    idField.set(t, p[0]);

                    java.lang.reflect.Field timeField = Transaction.class.getDeclaredField("timestamp");
                    timeField.setAccessible(true);
                    timeField.set(t, p[6]);

                    java.lang.reflect.Field refField = Transaction.class.getDeclaredField("referenceId");
                    refField.setAccessible(true);
                    refField.set(t, p[7]);

                    transactions.add(t);
                }
            }
        } catch (Exception e) {
        }
    }

    private void loadLoans() {
        File f = new File("data/loans.txt");
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;

            while ((line = r.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] p = line.split("\\|", -1);
                if (p.length >= 11) {
                    Loan l = new Loan(
                        p[1],
                        Double.parseDouble(p[2]),
                        Integer.parseInt(p[4]),
                        p[10]
                    );

                    java.lang.reflect.Field idField = Loan.class.getDeclaredField("loanId");
                    idField.setAccessible(true);
                    idField.set(l, p[0]);

                    java.lang.reflect.Field rateField = Loan.class.getDeclaredField("interestRate");
                    rateField.setAccessible(true);
                    rateField.set(l, Double.parseDouble(p[3]));

                    java.lang.reflect.Field paymentField = Loan.class.getDeclaredField("monthlyPayment");
                    paymentField.setAccessible(true);
                    paymentField.set(l, Double.parseDouble(p[5]));

                    java.lang.reflect.Field remainField = Loan.class.getDeclaredField("remainingBalance");
                    remainField.setAccessible(true);
                    remainField.set(l, Double.parseDouble(p[6]));

                    java.lang.reflect.Field statusField = Loan.class.getDeclaredField("status");
                    statusField.setAccessible(true);
                    statusField.set(l, p[7]);

                    java.lang.reflect.Field dateField = Loan.class.getDeclaredField("applicationDate");
                    dateField.setAccessible(true);
                    dateField.set(l, p[8]);

                    if (!p[9].isEmpty()) {
                        java.lang.reflect.Field approveField = Loan.class.getDeclaredField("approvalDate");
                        approveField.setAccessible(true);
                        approveField.set(l, p[9]);
                    }

                    loans.add(l);
                }
            }
        } catch (Exception e) {
        }
    }

    private void loadFixedDeposits() {
        File f = new File("data/fixeddeposits.txt");
        if (!f.exists()) return;

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            boolean firstLine = true;

            while ((line = r.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] p = line.split("\\|", -1);
                if (p.length >= 9) {
                    FixedDeposit fd = new FixedDeposit(
                        p[1],
                        Double.parseDouble(p[2]),
                        Integer.parseInt(p[4])
                    );

                    java.lang.reflect.Field idField = FixedDeposit.class.getDeclaredField("fdId");
                    idField.setAccessible(true);
                    idField.set(fd, p[0]);

                    java.lang.reflect.Field rateField = FixedDeposit.class.getDeclaredField("interestRate");
                    rateField.setAccessible(true);
                    rateField.set(fd, Double.parseDouble(p[3]));

                    java.lang.reflect.Field maturityField = FixedDeposit.class.getDeclaredField("maturityAmount");
                    maturityField.setAccessible(true);
                    maturityField.set(fd, Double.parseDouble(p[5]));

                    java.lang.reflect.Field startField = FixedDeposit.class.getDeclaredField("startDate");
                    startField.setAccessible(true);
                    startField.set(fd, p[6]);

                    java.lang.reflect.Field matureField = FixedDeposit.class.getDeclaredField("maturityDate");
                    matureField.setAccessible(true);
                    matureField.set(fd, p[7]);

                    java.lang.reflect.Field statusField = FixedDeposit.class.getDeclaredField("status");
                    statusField.setAccessible(true);
                    statusField.set(fd, p[8]);

                    fixedDeposits.add(fd);
                }
            }
        } catch (Exception e) {
        }
    }
}