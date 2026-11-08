import java.util.List;
import java.util.Map;

public class AccountService {
    private Bank bank;
    
    public AccountService() {
        bank = Bank.getInstance();
    }
    
    public void viewAccountDetails(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "Invalid Customer ID!" + ColorUtil.RESET);
            return;
        }
        Account account = bank.getAccountByCustomerId(customerId);
        if (account == null) {
            System.out.println(ColorUtil.RED + "No account found!" + ColorUtil.RESET);
            return;
        }
        System.out.println(account.getAccountDetails());
        Customer customer = bank.getCustomer(customerId);
        if (customer != null) {
            System.out.println(customer.getCustomerInfo());
        }
    }
    
    public void viewAllCustomers() {
        Map<String, Customer> customers = bank.getAllCustomers();
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("ALL CUSTOMERS", 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        
        if (customers.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No customers found." + ColorUtil.RESET);
            return;
        }
        
        System.out.printf("%-15s %-25s %-30s %-10s%n", "Customer ID", "Name", "Email", "Status");
        System.out.println(StringUtil.createLine('-', 80));
        
        for (Customer c : customers.values()) {
            String name = c.getName().length() > 25 ? c.getName().substring(0, 22) + "..." : c.getName();
            String email = c.getEmail().length() > 30 ? c.getEmail().substring(0, 27) + "..." : c.getEmail();
            String status = c.isActive() ? ColorUtil.GREEN + "Active" + ColorUtil.RESET : ColorUtil.RED + "Inactive" + ColorUtil.RESET;
            System.out.printf("%-15s %-25s %-30s %-10s%n", c.getCustomerId(), name, email, status);
        }
        System.out.println(StringUtil.createLine('-', 80));
        System.out.println(ColorUtil.CYAN + "Total: " + customers.size() + " customers" + ColorUtil.RESET);
    }
    
    public void viewAllAccounts() {
        Map<String, Account> accounts = bank.getAllAccounts();
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 85) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("ALL ACCOUNTS", 85) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 85) + ColorUtil.RESET);
        
        if (accounts.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No accounts found." + ColorUtil.RESET);
            return;
        }
        
        System.out.printf("%-15s %-15s %-12s %-15s %-10s%n", "Account No", "Customer ID", "Type", "Balance", "Status");
        System.out.println(StringUtil.createLine('-', 85));
        
        for (Account a : accounts.values()) {

            String balanceStr = "RM " + String.format("%.2f", a.getBalance());

            String status = a.isActive()
                ? ColorUtil.GREEN + "Active" + ColorUtil.RESET
                : ColorUtil.RED + "Frozen" + ColorUtil.RESET;

            System.out.printf("%-15s %-15s %-12s %-15s %-10s%n",
                a.getAccountNumber(),
                a.getCustomerId(),
                a.getAccountType(),
                balanceStr,
                status);
        }
        System.out.println(StringUtil.createLine('-', 85));
        System.out.println(ColorUtil.CYAN + "Total: " + accounts.size() + " accounts" + ColorUtil.RESET);
    }
    
    public void approvePendingAccounts() {
        System.out.println(ColorUtil.GREEN + "All accounts are automatically approved." + ColorUtil.RESET);
    }
    
    public void toggleAccountStatus(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "Account number cannot be empty!" + ColorUtil.RESET);
            return;
        }
        Account account = bank.getAccount(accountNumber);
        if (account == null) {
            System.out.println(ColorUtil.RED + "Account not found!" + ColorUtil.RESET);
            return;
        }
        boolean newStatus = !account.isActive();
        account.setActive(newStatus);
        bank.saveAllData();
        System.out.println(ColorUtil.GREEN + "Account " + accountNumber + " has been " + (newStatus ? "ACTIVATED" : "FROZEN") + ColorUtil.RESET);
    }
    
    public void calculateAndDisplayInterest(String customerId) {
        Account account = bank.getAccountByCustomerId(customerId);
        if (account == null) {
            System.out.println(ColorUtil.RED + "No account found!" + ColorUtil.RESET);
            return;
        }
        double interest = account.calculateInterest();
        if (interest > 0) {
            System.out.println(ColorUtil.GREEN + "Interest calculated: RM" + String.format("%.2f", interest) + ColorUtil.RESET);
            System.out.println(ColorUtil.CYAN + "Will be credited at month end." + ColorUtil.RESET);
        } else {
            System.out.println(ColorUtil.YELLOW + "This account type does not earn interest." + ColorUtil.RESET);
        }
    }

    public void updateCustomerProfile(String customerId, String newName, String newEmail, String newPhone, String newAddress) {
        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "Invalid Customer ID!" + ColorUtil.RESET);
            return;
        }

        Customer customer = bank.getCustomer(customerId);
        if (customer == null) {
            System.out.println(ColorUtil.RED + "Customer not found!" + ColorUtil.RESET);
            return;
        }

        try {
            customer.setName(newName);
            customer.setEmail(newEmail);
            customer.setPhone(newPhone);
            customer.setAddress(newAddress);

            bank.saveAllData();

            System.out.println(ColorUtil.GREEN + "Profile updated successfully!" + ColorUtil.RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(ColorUtil.RED + e.getMessage() + ColorUtil.RESET);
        } catch (Exception e) {
            System.out.println(ColorUtil.RED + "Unexpected error while updating profile!" + ColorUtil.RESET);
        }
    }

    public void deleteAccount(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "Account number cannot be empty!" + ColorUtil.RESET);
            return;
        }

        Account account = bank.getAccount(accountNumber);
        if (account == null) {
            System.out.println(ColorUtil.RED + "Account not found!" + ColorUtil.RESET);
            return;
        }

        if (account.isActive()) {
            System.out.println(ColorUtil.RED + "Only frozen accounts can be deleted!" + ColorUtil.RESET);
            return;
        }

        boolean deleted = bank.removeAccount(accountNumber);
        if (deleted) {
            System.out.println(ColorUtil.GREEN + "Account " + accountNumber + " deleted successfully!" + ColorUtil.RESET);
        } else {
            System.out.println(ColorUtil.RED + "Failed to delete account!" + ColorUtil.RESET);
        }
    }

    public void deleteCustomerByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "Customer email cannot be empty!" + ColorUtil.RESET);
            return;
        }

        List<Customer> matchedCustomers = bank.getCustomersByEmail(email);

        if (matchedCustomers.isEmpty()) {
            System.out.println(ColorUtil.RED + "No customer found with this email!" + ColorUtil.RESET);
            return;
        }

        // Checking First, if any account under email that is active, then cancel delete
        for (Customer customer : matchedCustomers) {
            for (String accountNumber : customer.getAccountNumbers()) {
                Account account = bank.getAccount(accountNumber);
                if (account != null && account.isActive()) {
                    System.out.println(ColorUtil.RED +
                        "Cannot delete because account " + accountNumber +
                        " under email " + email + " is still active." +
                        ColorUtil.RESET);
                    return;
                }
            }
        }

        // Check Loan
        for (Customer customer : matchedCustomers) {
            List<Loan> loans = bank.getLoansByCustomer(customer.getCustomerId());
            for (Loan loan : loans) {
                if (!loan.getStatus().equalsIgnoreCase("PAID")
                        && !loan.getStatus().equalsIgnoreCase("REJECTED")) {
                    System.out.println(ColorUtil.RED +
                        "Cannot delete because customer " + customer.getCustomerId() +
                        " still has active or pending loans." +
                        ColorUtil.RESET);
                    return;
                }
            }
        }

        // Check fixed deposit
        for (Customer customer : matchedCustomers) {
            List<FixedDeposit> fixedDeposits = bank.getFixedDepositsByCustomer(customer.getCustomerId());
            for (FixedDeposit fd : fixedDeposits) {
                if (!fd.getStatus().equalsIgnoreCase("WITHDRAWN")) {
                    System.out.println(ColorUtil.RED +
                        "Cannot delete because customer " + customer.getCustomerId() +
                        " still has active fixed deposits." +
                        ColorUtil.RESET);
                    return;
                }
            }
        }

        boolean deleted = bank.removeCustomersByEmail(email);

        if (deleted) {
            System.out.println(ColorUtil.GREEN +
                "All customers and accounts with email " + email + " have been deleted successfully!" +
                ColorUtil.RESET);
        } else {
            System.out.println(ColorUtil.RED + "Failed to delete customers!" + ColorUtil.RESET);
        }
    }
}