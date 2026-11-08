public class AuthenticationService {
    private Bank bank;
    
    public AuthenticationService() {
        bank = Bank.getInstance();
    }
    
    public boolean authenticateCustomer(String customerId, String password) {
        if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "[X] Customer ID cannot be empty!" + ColorUtil.RESET);
            return false;
        }
        
        if (!ValidationUtil.isValidCustomerId(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Invalid Customer ID format!" + ColorUtil.RESET);
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            System.out.println(ColorUtil.RED + "[X] Password cannot be empty!" + ColorUtil.RESET);
            return false;
        }
        
        Customer customer = bank.getCustomer(customerId);
        if (customer == null) {
            return false;
        }
        
        return customer.validatePassword(password) && customer.isActive();
    }
    
    public String registerCustomer(String name, String email, String phone, String address,
                                   String password, int accountType, double initialDeposit) {
        
        if (!ValidationUtil.isValidName(name)) {
            throw new IllegalArgumentException(ValidationUtil.getValidNameMessage());
        }
        
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException(ValidationUtil.getValidEmailMessage());
        }
        
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException(ValidationUtil.getValidPhoneMessage());
        }
        
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        if (address.trim().length() < 5) {
            throw new IllegalArgumentException("Address must be at least 5 characters.");
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException(ValidationUtil.getValidPasswordMessage());
        }
        
        if (accountType != 1 && accountType != 2) {
            throw new IllegalArgumentException("Invalid account type. Must be 1 (Savings) or 2 (Current).");
        }
        
        if (!ValidationUtil.isValidAmount(initialDeposit, 100, 1000000)) {
            throw new IllegalArgumentException("Initial deposit must be between RM100 and RM1,000,000.");
        }
        
        Customer customer = new Customer(
            name.trim(),
            email.trim().toLowerCase(),
            phone.trim(),
            address.trim(),
            password
        );
        bank.addCustomer(customer);
        
        Account account;
        if (accountType == 1) {
            account = new SavingsAccount(customer.getCustomerId(), initialDeposit);
        } else {
            account = new CurrentAccount(customer.getCustomerId(), initialDeposit);
        }
        bank.addAccount(account);
        
        Transaction initialTransaction = new Transaction(
            account.getAccountNumber(),
            customer.getCustomerId(),
            "DEPOSIT",
            initialDeposit,
            initialDeposit,
            "Initial deposit for account opening"
        );
        bank.addTransaction(initialTransaction);
        
        return customer.getCustomerId();
    }
}