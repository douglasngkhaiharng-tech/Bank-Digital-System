import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
        private static void clearScreen() {
            try {
                final String os = System.getProperty("os.name", "").toLowerCase();

                if (!os.contains("win")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                } else {

                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
            } catch (Exception e) {

            }

            for (int i = 0; i < 80; i++) {
                System.out.println();
            }
        }

        private static void pauseScreen() {
            /*int seconds = 3;
            try {
                for (int i = seconds; i > 0; i--) {
                    System.out.print("\r" + ColorUtil.CYAN + "Returning to menu in " + i + " seconds..." + ColorUtil.RESET);
                    Thread.sleep(1000);
                }
                System.out.println();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }*/
        }
    
    public static void main(String[] args) {
        Bank bank = Bank.getInstance();
        AuthenticationService authService = new AuthenticationService();
        
        System.out.println(ColorUtil.CYAN_BOLD + "\n" + StringUtil.createLine('=', 60) + ColorUtil.RESET);
        System.out.println(StringUtil.center("WELCOME TO DIGITAL BANKING SYSTEM", 60) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN_BOLD + StringUtil.createLine('=', 60) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "\nTIP: Enter '0' or 'Q' at any time to cancel/go back" + ColorUtil.RESET);
        
        while (true) {
            System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 50) + ColorUtil.RESET);
            System.out.println(ColorUtil.GREEN_BOLD + "1." + ColorUtil.RESET + " Customer Login");
            System.out.println(ColorUtil.GREEN_BOLD + "2." + ColorUtil.RESET + " Admin Login");
            System.out.println(ColorUtil.GREEN_BOLD + "3." + ColorUtil.RESET + " Register New Account");
            System.out.println(ColorUtil.GREEN_BOLD + "0." + ColorUtil.RESET + " Exit System");
            System.out.print(ColorUtil.CYAN + "\nEnter your choice: " + ColorUtil.RESET);
            
            String input = scanner.nextLine().trim();
            
            if (isExitCommand(input)) {
                System.out.println(ColorUtil.GREEN + "\n[OK] Thank you for using Digital Banking System!" + ColorUtil.RESET);
                scanner.close();
                System.exit(0);
                return;
            }
            
            int choice = parseChoice(input, 0, 3);
            if (choice == -1) {
                System.out.println(ColorUtil.RED + "[X] Invalid choice! Please enter 0, 1, 2, or 3." + ColorUtil.RESET);
                continue;
            }
            
            switch (choice) {
                case 1:
                    pauseScreen();
                    clearScreen();
                    handleCustomerLogin();
                    break;
                case 2:
                    pauseScreen();
                    clearScreen();
                    handleAdminLogin();
                    break;
                case 3:
                    pauseScreen();
                    clearScreen();
                    handleRegistration();
                    break;
                case 0:
                    System.out.println(ColorUtil.GREEN + "\n[OK] Thank you for using Digital Banking System!" + ColorUtil.RESET);
                    scanner.close();
                    System.exit(0);
                    return;
            }
        }
    }
    
    private static boolean isExitCommand(String input) {
        if (input == null) return false;
        String trimmed = input.trim().toLowerCase();
        return trimmed.equals("0") || trimmed.equals("q") || trimmed.equals("quit") ||
               trimmed.equals("exit") || trimmed.equals("cancel") || trimmed.equals("back");
    }
    
    private static int parseChoice(String input, int min, int max) {
        if (input == null) return -1;
        String trimmed = input.trim();
        
        if (isExitCommand(trimmed)) {
            return 0;
        }
        
        try {
            int value = Integer.parseInt(trimmed);
            if (value >= min && value <= max) {
                return value;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }
    
    private static String getInputWithExit(String prompt) {
        System.out.print(ColorUtil.CYAN + prompt + " (Enter 0 or Q to cancel): " + ColorUtil.RESET);
        String input = scanner.nextLine().trim();
        
        if (isExitCommand(input)) {
            System.out.println(ColorUtil.YELLOW + "Operation cancelled. Returning..." + ColorUtil.RESET);
            return null;
        }
        return input;
    }
    
    private static int getValidIntInputWithExit(int min, int max, String fieldName) {
        while (true) {
            String input = getInputWithExit(fieldName);
            if (input == null) return -1;
            
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println(ColorUtil.RED + "[X] Please enter a number between " + min + " and " + max + "!" + ColorUtil.RESET);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(ColorUtil.RED + "[X] Invalid input! Please enter a valid number." + ColorUtil.RESET);
            }
        }
    }
    
    private static double getValidDoubleInputWithExit(double min, double max, String fieldName) {
        while (true) {
            String input = getInputWithExit(fieldName);
            if (input == null) return -1;
            
            try {
                double value = Double.parseDouble(input);
                if (value < min) {
                    System.out.println(ColorUtil.RED + "[X] " + fieldName + " cannot be less than " + min + "!" + ColorUtil.RESET);
                    continue;
                }
                if (value > max) {
                    System.out.println(ColorUtil.RED + "[X] " + fieldName + " cannot exceed " + max + "!" + ColorUtil.RESET);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println(ColorUtil.RED + "[X] Invalid amount! Please enter a valid number." + ColorUtil.RESET);
            }
        }
    }
    
    private static String getValidNameWithExit() {
        while (true) {
            String name = getInputWithExit("Full Name");
            if (name == null) return null;
            
            if (!ValidationUtil.isValidName(name)) {
                System.out.println(ColorUtil.RED + "[X] " + ValidationUtil.getValidNameMessage() + ColorUtil.RESET);
                continue;
            }
            return name;
        }
    }
    
    private static String getValidEmailWithExit() {
        while (true) {
            String email = getInputWithExit("Email(username@example.com)");
            if (email == null) return null;
            
            if (!ValidationUtil.isValidEmail(email)) {
                System.out.println(ColorUtil.RED + "[X] " + ValidationUtil.getValidEmailMessage() + ColorUtil.RESET);
                continue;
            }
            return email;
        }
    }
    
    private static String getValidPhoneWithExit() {
        while (true) {
            String phone = getInputWithExit("Phone Number");
            if (phone == null) return null;
            
            if (!ValidationUtil.isValidPhone(phone)) {
                System.out.println(ColorUtil.RED + "[X] " + ValidationUtil.getValidPhoneMessage() + ColorUtil.RESET);
                continue;
            }
            return phone;
        }
    }
    
    private static String getValidAddressWithExit() {
        while (true) {
            String address = getInputWithExit("Address (min 5 characters)");
            if (address == null) return null;
            
            if (address.length() < 5) {
                System.out.println(ColorUtil.RED + "[X] Address must be at least 5 characters!" + ColorUtil.RESET);
                continue;
            }
            return address;
        }
    }
    
    private static String getValidPasswordWithExit(String purpose) {
        while (true) {
            String password = getInputWithExit(purpose);
            if (password == null) return null;
            
            if (!ValidationUtil.isValidPassword(password)) {
                System.out.println(ColorUtil.RED + "[X] " + ValidationUtil.getValidPasswordMessage() + ColorUtil.RESET);
                continue;
            }
            return password;
        }
    }
    
    private static String getValidCustomerIdWithExit() {
        while (true) {
            String customerId = getInputWithExit("Customer ID");
            if (customerId == null) return null;
            
            if (!ValidationUtil.isValidCustomerId(customerId)) {
                System.out.println(ColorUtil.RED + "[X] Invalid Customer ID format! Must start with 'CUST' followed by numbers." + ColorUtil.RESET);
                continue;
            }
            return customerId;
        }
    }
    
    private static String getValidAccountNumberWithExit() {
        while (true) {
            String accNum = getInputWithExit("Account Number");
            if (accNum == null) return null;
            
            if (!ValidationUtil.isValidAccountNumber(accNum)) {
                System.out.println(ColorUtil.RED + "[X] Invalid account number format! Account numbers start with 'ACC'." + ColorUtil.RESET);
                continue;
            }
            return accNum;
        }
    }
    
    private static void handleCustomerLogin() {
        Bank bank = Bank.getInstance();
        AuthenticationService authService = new AuthenticationService();
        
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("CUSTOMER LOGIN", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        String customerId = getValidCustomerIdWithExit();
        if (customerId == null) return;
        
        if (!bank.customerExists(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Customer ID not found! Please register first." + ColorUtil.RESET);
            return;
        }
        
        String password = getValidPasswordWithExit("Enter Password");
        if (password == null) return;
        
        if (authService.authenticateCustomer(customerId, password)) {
            System.out.println(ColorUtil.GREEN + "\n[OK] Login successful! Welcome " + customerId + ColorUtil.RESET);
            showCustomerMenu(customerId);
        } else {
            System.out.println(ColorUtil.RED + "[X] Invalid password! Please try again." + ColorUtil.RESET);
        }
    }
    
    private static void handleAdminLogin() {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("ADMIN LOGIN", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        String password = getInputWithExit("Enter Admin Password");
        if (password == null) return;
        
        if (password.equals("admin123")) {
            System.out.println(ColorUtil.GREEN + "\n[OK] Admin login successful!" + ColorUtil.RESET);
            showAdminMenu();
        } else {
            System.out.println(ColorUtil.RED + "[X] Invalid admin password!" + ColorUtil.RESET);
        }
    }
    
    private static void handleRegistration() {
        AuthenticationService authService = new AuthenticationService();
        
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("NEW ACCOUNT REGISTRATION", 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Enter 0 or Q at any time to cancel registration" + ColorUtil.RESET);
        
        String name = getValidNameWithExit();
        if (name == null) return;
        
        String email = getValidEmailWithExit();
        if (email == null) return;
        
        String phone = getValidPhoneWithExit();
        if (phone == null) return;
        
        String address = getValidAddressWithExit();
        if (address == null) return;
        
        String password = getValidPasswordWithExit("Set Password");
        if (password == null) return;
        
        int accType;
        double initialDeposit;

        while (true) {
            System.out.println(ColorUtil.YELLOW + "\nSelect Account Type:" + ColorUtil.RESET);
            System.out.println("1. Savings Account (2.5% interest rate, min RM100 balance)");
            System.out.println("2. Current Account (Overdraft up to RM5000)");
            System.out.println(ColorUtil.CYAN + "Enter 0 or Q to cancel" + ColorUtil.RESET);

            accType = getValidIntInputWithExit(1, 2, "Account type (1 or 2)");
            if (accType == -1) return;

            initialDeposit = getValidDoubleInputWithExit(100, 1000000, "Initial deposit (min RM100)");
            
            if (initialDeposit == -1) {
                System.out.println(ColorUtil.YELLOW + "Returning to account type selection..." + ColorUtil.RESET);
                continue;
            }

            break;
        }
        
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('-', 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("PLEASE CONFIRM YOUR DETAILS", 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 50) + ColorUtil.RESET);
        System.out.println("  Name:     " + name);
        System.out.println("  Email:    " + email);
        System.out.println("  Phone:    " + phone);
        System.out.println("  Address:  " + address);
        System.out.println("  Account:  " + (accType == 1 ? "Savings Account" : "Current Account"));
        System.out.println("  Deposit:  RM" + String.format("%.2f", initialDeposit));
        
        while (true) {
            System.out.print(ColorUtil.CYAN + "\nConfirm registration? (Y/N) or 0/Q to cancel: " + ColorUtil.RESET);
            String confirm = scanner.nextLine().trim().toUpperCase();
            
            if (isExitCommand(confirm)) {
                System.out.println(ColorUtil.YELLOW + "Registration cancelled. Returning..." + ColorUtil.RESET);
                return;
            }
            
            if (confirm.equals("Y")) {
                try {
                    String customerId = authService.registerCustomer(name, email, phone, address, password, accType, initialDeposit);
                    System.out.println(ColorUtil.GREEN + "\n" + StringUtil.createLine('=', 50) + ColorUtil.RESET);
                    System.out.println(StringUtil.center("REGISTRATION SUCCESSFUL!", 50) + ColorUtil.RESET);
                    System.out.println(ColorUtil.GREEN + StringUtil.createLine('=', 50) + ColorUtil.RESET);
                    System.out.println(ColorUtil.YELLOW + "\nYour Customer ID: " + customerId + ColorUtil.RESET);
                    System.out.println(ColorUtil.CYAN + "Please save this ID for future login!" + ColorUtil.RESET);
                    break;
                } catch (Exception e) {
                    System.out.println(ColorUtil.RED + "[X] Registration failed: " + e.getMessage() + ColorUtil.RESET);
                }
                break;
            } else if (confirm.equals("N")) {
                System.out.println(ColorUtil.YELLOW + "Registration cancelled. Returning to main menu..." + ColorUtil.RESET);
                break;
            } else {
                System.out.println(ColorUtil.RED + "[X] Please enter Y (Yes) or N (No)." + ColorUtil.RESET);
            }
        }
    }
    
    private static void showCustomerMenu(String customerId) {
        while (true) {
            System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 50) + ColorUtil.RESET);
            System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("CUSTOMER MENU", 50) + ColorUtil.RESET);
            System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 50) + ColorUtil.RESET);
            System.out.println(ColorUtil.GREEN_BOLD + "1." + ColorUtil.RESET + " View Account Details");
            System.out.println(ColorUtil.GREEN_BOLD + "2." + ColorUtil.RESET + " Deposit Money");
            System.out.println(ColorUtil.GREEN_BOLD + "3." + ColorUtil.RESET + " Withdraw Money");
            System.out.println(ColorUtil.GREEN_BOLD + "4." + ColorUtil.RESET + " Transfer Funds");
            System.out.println(ColorUtil.GREEN_BOLD + "5." + ColorUtil.RESET + " View Transaction History");
            System.out.println(ColorUtil.GREEN_BOLD + "6." + ColorUtil.RESET + " Generate Monthly Statement");
            System.out.println(ColorUtil.GREEN_BOLD + "7." + ColorUtil.RESET + " Calculate Interest");
            System.out.println(ColorUtil.GREEN_BOLD + "8." + ColorUtil.RESET + " Loan Application");
            System.out.println(ColorUtil.GREEN_BOLD + "9." + ColorUtil.RESET + " Fixed Deposit Management");
            System.out.println(ColorUtil.GREEN_BOLD + "10." + ColorUtil.RESET + " Currency Exchange Calculator");
            System.out.println(ColorUtil.GREEN_BOLD + "11." + ColorUtil.RESET + " Update Profile");
            System.out.println(ColorUtil.GREEN_BOLD + "0." + ColorUtil.RESET + " Logout");
            System.out.print(ColorUtil.CYAN + "\nEnter your choice: " + ColorUtil.RESET);
            
            String input = scanner.nextLine().trim();
            
            if (isExitCommand(input)) {
                System.out.println(ColorUtil.GREEN + "[OK] Logged out successfully!" + ColorUtil.RESET);
                return;
            }
            
            int choice = parseChoice(input, 1, 11);
            if (choice == -1) {
                System.out.println(ColorUtil.RED + "[X] Invalid choice! Please enter 0-11." + ColorUtil.RESET);
                continue;
            }
            
            AccountService accService = new AccountService();
            TransactionService transService = new TransactionService();
            ReportService reportService = new ReportService();
            
            switch (choice) {
                case 1:
                    pauseScreen();
                    clearScreen();
                    accService.viewAccountDetails(customerId);
                    break;
                case 2:
                    pauseScreen();
                    clearScreen();
                    handleDeposit(transService, customerId);
                    break;
                case 3:
                    pauseScreen();
                    clearScreen();
                    handleWithdraw(transService, customerId);
                case 4:
                    pauseScreen();
                    clearScreen();
                    handleTransfer(transService, customerId);
                    break;
                case 5:
                    pauseScreen();
                    clearScreen();
                    transService.viewTransactionHistory(customerId);
                    break;
                case 6:
                    pauseScreen();
                    clearScreen();
                    reportService.generateMonthlyStatement(customerId);
                    break;
                case 7:
                    pauseScreen();
                    clearScreen();
                    accService.calculateAndDisplayInterest(customerId);
                case 8:
                    pauseScreen();
                    clearScreen();
                    handleLoanApplication(customerId);
                    break;
                case 9:
                    pauseScreen();
                    clearScreen();
                    handleFixedDeposit(customerId);
                    break;
                case 10:
                    pauseScreen();
                    clearScreen();
                    handleCurrencyExchange();
                    break;
                case 11:
                    pauseScreen();
                    clearScreen();
                    handleUpdateProfile(customerId);
                    break;
            }
        }
    }
    
    private static void handleDeposit(TransactionService transService, String customerId) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("DEPOSIT MONEY", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        double amount = getValidDoubleInputWithExit(0.01, 1000000, "Deposit amount");
        if (amount == -1) return;
        
        transService.deposit(customerId, amount);
    }
    
    private static void handleWithdraw(TransactionService transService, String customerId) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("WITHDRAW MONEY", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        double amount = getValidDoubleInputWithExit(0.01, 50000, "Withdrawal amount");
        if (amount == -1) return;
        
        transService.withdraw(customerId, amount);
    }
    
    private static void handleTransfer(TransactionService transService, String customerId) {
        Bank bank = Bank.getInstance();
        
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("TRANSFER FUNDS", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        String recipientId = getValidCustomerIdWithExit();
        if (recipientId == null) return;
        
        if (recipientId.equals(customerId)) {
            System.out.println(ColorUtil.RED + "[X] Cannot transfer to your own account!" + ColorUtil.RESET);
            return;
        }
        
        if (!bank.customerExists(recipientId)) {
            System.out.println(ColorUtil.RED + "[X] Recipient Customer ID not found!" + ColorUtil.RESET);
            return;
        }
        
        double amount = getValidDoubleInputWithExit(0.01, 50000, "Transfer amount");
        if (amount == -1) return;
        
        transService.transfer(customerId, recipientId, amount);
    }
    
    private static void handleLoanApplication(String customerId) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("LOAN APPLICATION", 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 50) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Loan Terms: Up to RM100,000 | 4.5% annual interest | 12-60 months" + ColorUtil.RESET);
        
        double loanAmount = getValidDoubleInputWithExit(1000, 100000, "Loan amount (RM1000 - RM100000)");
        if (loanAmount == -1) return;
        
        int termMonths = getValidIntInputWithExit(12, 60, "Loan term in months (12-60 months)");
        if (termMonths == -1) return;
        
        String purpose = getInputWithExit("Loan purpose");
        if (purpose == null) return;
        
        Loan loan = new Loan(customerId, loanAmount, termMonths, purpose);
        Bank.getInstance().addLoan(loan);
        
        System.out.println(ColorUtil.GREEN + "\n[OK] Loan application submitted successfully!" + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "  Loan ID: " + loan.getLoanId() + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "  Monthly Payment: RM" + String.format("%.2f", loan.getMonthlyPayment()) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + "  Status: PENDING (Admin approval required)" + ColorUtil.RESET);
    }
    
    private static void handleFixedDeposit(String customerId) {
        while (true) {
            System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("FIXED DEPOSIT MANAGEMENT", 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.GREEN_BOLD + "1." + ColorUtil.RESET + " Open Fixed Deposit");
            System.out.println(ColorUtil.GREEN_BOLD + "2." + ColorUtil.RESET + " View My Fixed Deposits");
            System.out.println(ColorUtil.GREEN_BOLD + "3." + ColorUtil.RESET + " Withdraw Fixed Deposit");
            System.out.println(ColorUtil.GREEN_BOLD + "0." + ColorUtil.RESET + " Back");
            System.out.print(ColorUtil.CYAN + "\nEnter your choice: " + ColorUtil.RESET);
            
            String input = scanner.nextLine().trim();
            if (isExitCommand(input)) return;
            
            int choice = parseChoice(input, 1, 3);
            if (choice == -1) {
                System.out.println(ColorUtil.RED + "[X] Invalid choice!" + ColorUtil.RESET);
                continue;
            }
            
            switch (choice) {
                case 1:
                    openFixedDeposit(customerId);
                    break;
                case 2:
                    viewFixedDeposits(customerId);
                    break;
                case 3:
                    withdrawFixedDeposit(customerId);
                    break;
                default:
                    return;
            }
        }
    }
    
    private static void openFixedDeposit(String customerId) {
        Bank bank = Bank.getInstance();
        Account account = bank.getAccountByCustomerId(customerId);
        
        if (account == null) {
            System.out.println(ColorUtil.RED + "[X] No account found!" + ColorUtil.RESET);
            return;
        }
        
        System.out.println(ColorUtil.YELLOW + "\nFixed Deposit Interest Rates:" + ColorUtil.RESET);
        System.out.println("  1 month: 2.5% p.a.");
        System.out.println("  3 months: 2.8% p.a.");
        System.out.println("  6 months: 3.0% p.a.");
        System.out.println("  12 months: 3.5% p.a.");
        
        int tenure = getValidIntInputWithExit(1, 12, "Tenure in months (1, 3, 6, 12)");
        if (tenure == -1) return;
        
        if (tenure != 1 && tenure != 3 && tenure != 6 && tenure != 12) {
            System.out.println(ColorUtil.RED + "[X] Invalid tenure! Please choose 1, 3, 6, or 12 months." + ColorUtil.RESET);
            return;
        }
        
        double amount = getValidDoubleInputWithExit(1000, 500000, "Deposit amount (RM1000 - RM500000)");
        if (amount == -1) return;
        
        if (amount > account.getBalance()) {
            System.out.println(ColorUtil.RED + "[X] Insufficient balance! Your balance: RM" + String.format("%.2f", account.getBalance()) + ColorUtil.RESET);
            return;
        }
        
        boolean success = account.withdraw(amount);
        if (!success) {
            System.out.println(ColorUtil.RED + "[X] Failed to deduct balance for Fixed Deposit!" + ColorUtil.RESET);
            return;
        }
        
        FixedDeposit fd = new FixedDeposit(customerId, amount, tenure);
        bank.addFixedDeposit(fd);
        
        System.out.println(ColorUtil.GREEN + "\n[OK] Fixed Deposit opened successfully!" + ColorUtil.RESET);
        System.out.println(fd.getFdDetails());
    }
    
    private static void viewFixedDeposits(String customerId) {
        Bank bank = Bank.getInstance();
        List<FixedDeposit> fds = bank.getFixedDepositsByCustomer(customerId);
        
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 70) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("MY FIXED DEPOSITS", 70) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 70) + ColorUtil.RESET);
        
        if (fds.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No fixed deposits found." + ColorUtil.RESET);
            return;
        }
        
        for (FixedDeposit fd : fds) {
            System.out.println(fd.getFdDetails());
        }
    }
    
    private static void withdrawFixedDeposit(String customerId) {
        Bank bank = Bank.getInstance();
        List<FixedDeposit> fds = bank.getFixedDepositsByCustomer(customerId);
        
        if (fds.isEmpty()) {
            System.out.println(ColorUtil.RED + "[X] No fixed deposits found!" + ColorUtil.RESET);
            return;
        }
        
        System.out.println(ColorUtil.YELLOW + "\nYour Fixed Deposits:" + ColorUtil.RESET);
        for (int i = 0; i < fds.size(); i++) {
            FixedDeposit fd = fds.get(i);
            System.out.println("  " + (i + 1) + ". " + fd.getFdId() + " - RM" + String.format("%.2f", fd.getPrincipal()) +
                " (" + fd.getTenureMonths() + " months) - " + fd.getStatus());
        }
        
        System.out.print(ColorUtil.CYAN + "\nSelect FD number (or 0 to cancel): " + ColorUtil.RESET);
        String input = scanner.nextLine().trim();
        if (isExitCommand(input)) return;
        
        int selection;
        try {
            selection = Integer.parseInt(input);
            if (selection < 1 || selection > fds.size()) {
                System.out.println(ColorUtil.RED + "[X] Invalid selection!" + ColorUtil.RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.RED + "[X] Invalid input!" + ColorUtil.RESET);
            return;
        }
        
        FixedDeposit selectedFd = fds.get(selection - 1);
        
        if (selectedFd.getStatus().equals("WITHDRAWN")) {
            System.out.println(ColorUtil.RED + "[X] This Fixed Deposit has already been withdrawn!" + ColorUtil.RESET);
            return;
        }
        
        if (selectedFd.getStatus().equals("MATURED")) {
            System.out.println(ColorUtil.GREEN + "[OK] Fixed Deposit has matured!" + ColorUtil.RESET);
        }
        
        double amount = selectedFd.withdraw();
        Account account = bank.getAccountByCustomerId(customerId);
        if (account != null) {
            account.deposit(amount);
        }
        bank.addFixedDeposit(selectedFd);
        
        System.out.println(ColorUtil.GREEN + "\n[OK] Fixed Deposit withdrawn successfully!" + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "  Amount credited: RM" + String.format("%.2f", amount) + ColorUtil.RESET);
    }
    
    private static void handleCurrencyExchange() {
        while (true) {
            System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("CURRENCY EXCHANGE CALCULATOR", 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.GREEN_BOLD + "1." + ColorUtil.RESET + " Convert Currency");
            System.out.println(ColorUtil.GREEN_BOLD + "2." + ColorUtil.RESET + " View Exchange Rates");
            System.out.println(ColorUtil.GREEN_BOLD + "0." + ColorUtil.RESET + " Back");
            System.out.print(ColorUtil.CYAN + "\nEnter your choice: " + ColorUtil.RESET);
            
            String input = scanner.nextLine().trim();
            if (isExitCommand(input)) return;
            
            int choice = parseChoice(input, 1, 2);
            if (choice == -1) {
                System.out.println(ColorUtil.RED + "[X] Invalid choice!" + ColorUtil.RESET);
                continue;
            }
            
            switch (choice) {
                case 1:
                    performCurrencyConversion();
                    break;
                case 2:
                    System.out.println(CurrencyConverter.getAvailableCurrencies());
                    break;
                default:
                    return;
            }
        }
    }
    
    private static void performCurrencyConversion() {
        System.out.println(CurrencyConverter.getAvailableCurrencies());
        
        String fromCurrency = getInputWithExit("From Currency (e.g., MYR, USD, SGD)");
        if (fromCurrency == null) return;
        
        String toCurrency = getInputWithExit("To Currency (e.g., MYR, USD, SGD)");
        if (toCurrency == null) return;
        
        double amount = getValidDoubleInputWithExit(0.01, 1000000, "Amount to convert");
        if (amount == -1) return;
        
        double result = CurrencyConverter.convert(amount, fromCurrency, toCurrency);
        
        if (result > 0) {
            System.out.println(ColorUtil.GREEN + "\n[OK] Conversion Result:" + ColorUtil.RESET);
            System.out.println(ColorUtil.CYAN_BOLD + "  " + CurrencyConverter.formatConversion(amount, fromCurrency, result, toCurrency) + ColorUtil.RESET);
        }
    }

    private static void handleUpdateProfile(String customerId) {
        AccountService accService = new AccountService();
        Bank bank = Bank.getInstance();
        Customer customer = bank.getCustomer(customerId);

        if (customer == null) {
            System.out.println(ColorUtil.RED + "[X] Customer not found!" + ColorUtil.RESET);
            return;
        }

        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("UPDATE PROFILE", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Please enter your updated details below." + ColorUtil.RESET);

        System.out.println("Current Name    : " + customer.getName());
        System.out.println("Current Email   : " + customer.getEmail());
        System.out.println("Current Phone   : " + customer.getPhone());
        System.out.println("Current Address : " + customer.getAddress());

        String name = getValidNameWithExit();
        if (name == null) return;

        String email = getValidEmailWithExit();
        if (email == null) return;

        String phone = getValidPhoneWithExit();
        if (phone == null) return;

        String address = getValidAddressWithExit();
        if (address == null) return;

        accService.updateCustomerProfile(customerId, name, email, phone, address);
    }
    
    private static void showAdminMenu() {
        while (true) {
            System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.RED_BOLD + StringUtil.center("ADMIN MENU", 45) + ColorUtil.RESET);
            System.out.println(ColorUtil.GREEN_BOLD + "1." + ColorUtil.RESET + " View All Customers");
            System.out.println(ColorUtil.GREEN_BOLD + "2." + ColorUtil.RESET + " View All Accounts");
            System.out.println(ColorUtil.GREEN_BOLD + "3." + ColorUtil.RESET + " Approve Pending Accounts");
            System.out.println(ColorUtil.GREEN_BOLD + "4." + ColorUtil.RESET + " Freeze/Unfreeze Account");
            System.out.println(ColorUtil.GREEN_BOLD + "5." + ColorUtil.RESET + " Set Interest Rate");
            System.out.println(ColorUtil.GREEN_BOLD + "6." + ColorUtil.RESET + " View Suspicious Transactions");
            System.out.println(ColorUtil.GREEN_BOLD + "7." + ColorUtil.RESET + " Approve/Reject Loans");
            System.out.println(ColorUtil.GREEN_BOLD + "8." + ColorUtil.RESET + " View All Loans");
            System.out.println(ColorUtil.GREEN_BOLD + "9." + ColorUtil.RESET + " View All Fixed Deposits");
            System.out.println(ColorUtil.GREEN_BOLD + "10." + ColorUtil.RESET + " Delete Account");
            System.out.println(ColorUtil.GREEN_BOLD + "11." + ColorUtil.RESET + " Delete Customer");
            System.out.println(ColorUtil.GREEN_BOLD + "0." + ColorUtil.RESET + " Logout");
            System.out.print(ColorUtil.CYAN + "\nEnter your choice: " + ColorUtil.RESET);
            
            String input = scanner.nextLine().trim();
            
            if (isExitCommand(input)) {
                System.out.println(ColorUtil.GREEN + "[OK] Logged out from admin panel!" + ColorUtil.RESET);
                return;
            }
            
            int choice = parseChoice(input, 1, 11);
            if (choice == -1) {
                System.out.println(ColorUtil.RED + "[X] Invalid choice! Please enter 0-11." + ColorUtil.RESET);
                continue;
            }
            
            AccountService accService = new AccountService();
            TransactionService transService = new TransactionService();
            
            switch (choice) {
                case 1:
                    pauseScreen();
                    clearScreen();
                    accService.viewAllCustomers();
                    break;
                case 2:
                    pauseScreen();
                    clearScreen();
                    accService.viewAllAccounts();
                    break;
                case 3:
                    pauseScreen();
                    clearScreen();
                    accService.approvePendingAccounts();
                    break;
                case 4:
                    pauseScreen();
                    clearScreen();
                    handleFreezeAccount(accService);
                    break;
                case 5:
                    pauseScreen();
                    clearScreen();
                    handleSetInterestRate();
                    break;
                case 6:
                    pauseScreen();
                    clearScreen();
                    transService.viewSuspiciousTransactions();
                    break;
                case 7:
                    pauseScreen();
                    clearScreen();
                    handleLoanApproval();
                    break;
                case 8:
                    pauseScreen();
                    clearScreen();
                    viewAllLoans();
                    break;
                case 9:
                    pauseScreen();
                    clearScreen();
                    viewAllFixedDeposits();
                    break;
                case 10:
                    pauseScreen();
                    clearScreen();
                    handleDeleteAccount(accService);
                    break;
                case 11:
                    pauseScreen();
                    clearScreen();
                    handleDeleteCustomer(accService);
                    break;
            }
        }
    }
    
    private static void handleFreezeAccount(AccountService accService) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("FREEZE/UNFREEZE ACCOUNT", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        String accountNumber = getValidAccountNumberWithExit();
        if (accountNumber == null) return;
        
        accService.toggleAccountStatus(accountNumber);
    }
    
    private static void handleSetInterestRate() {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("SET INTEREST RATE", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        
        double newRate = getValidDoubleInputWithExit(0, 0.1, "Interest rate (0 to 0.1)");
        if (newRate == -1) return;
        
        SavingsAccount.setBaseInterestRate(newRate);
    }
    
    private static void handleLoanApproval() {
        Bank bank = Bank.getInstance();
        List<Loan> pendingLoans = bank.getPendingLoans();
        
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 60) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("PENDING LOAN APPLICATIONS", 60) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 60) + ColorUtil.RESET);
        
        if (pendingLoans.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No pending loan applications." + ColorUtil.RESET);
            return;
        }
        
        for (int i = 0; i < pendingLoans.size(); i++) {
            Loan loan = pendingLoans.get(i);
            System.out.println("\n  " + (i + 1) + ". Loan ID: " + loan.getLoanId());
            System.out.println("     Customer ID: " + loan.getCustomerId());
            System.out.println("     Amount: RM" + String.format("%.2f", loan.getLoanAmount()));
            System.out.println("     Term: " + loan.getTermMonths() + " months");
            System.out.println("     Monthly Payment: RM" + String.format("%.2f", loan.getMonthlyPayment()));
            System.out.println("     Purpose: " + loan.getPurpose());
        }
        
        System.out.print(ColorUtil.CYAN + "\nEnter Loan number to process (or 0 to cancel): " + ColorUtil.RESET);
        String input = scanner.nextLine().trim();
        if (isExitCommand(input)) return;
        
        int selection;
        try {
            selection = Integer.parseInt(input);
            if (selection < 1 || selection > pendingLoans.size()) {
                System.out.println(ColorUtil.RED + "[X] Invalid selection!" + ColorUtil.RESET);
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.RED + "[X] Invalid input!" + ColorUtil.RESET);
            return;
        }
        
        Loan selectedLoan = pendingLoans.get(selection - 1);
        
        System.out.print(ColorUtil.CYAN + "Approve (A) or Reject (R): " + ColorUtil.RESET);
        String decision = scanner.nextLine().trim().toUpperCase();
        
        if (decision.equals("A")) {
            selectedLoan.setStatus("APPROVED");
            selectedLoan.setApprovalDate(new java.util.Date().toString());
            bank.addLoan(selectedLoan);
            System.out.println(ColorUtil.GREEN + "[OK] Loan " + selectedLoan.getLoanId() + " has been APPROVED!" + ColorUtil.RESET);
            
            Account account = bank.getAccountByCustomerId(selectedLoan.getCustomerId());
            if (account != null) {
                account.deposit(selectedLoan.getLoanAmount());
                Transaction loanTransaction = new Transaction(
                    account.getAccountNumber(),
                    selectedLoan.getCustomerId(),
                    "DEPOSIT",
                    selectedLoan.getLoanAmount(),
                    account.getBalance(),
                    "Loan disbursement - " + selectedLoan.getLoanId()
                );
                bank.addTransaction(loanTransaction);
                System.out.println(ColorUtil.CYAN + "  RM" + String.format("%.2f", selectedLoan.getLoanAmount()) + " credited to customer's account." + ColorUtil.RESET);
            }
        } else if (decision.equals("R")) {
            selectedLoan.setStatus("REJECTED");
            selectedLoan.setApprovalDate(new java.util.Date().toString());
            bank.addLoan(selectedLoan);
            System.out.println(ColorUtil.RED + "[X] Loan " + selectedLoan.getLoanId() + " has been REJECTED." + ColorUtil.RESET);
        } else {
            System.out.println(ColorUtil.RED + "[X] Invalid decision! Please enter A or R." + ColorUtil.RESET);
        }
    }

    private static void handleDeleteAccount(AccountService accService) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("DELETE ACCOUNT", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Note: Only frozen accounts can be deleted." + ColorUtil.RESET);

        String accountNumber = getValidAccountNumberWithExit();
        if (accountNumber == null) return;

        System.out.print(ColorUtil.CYAN + "Are you sure you want to delete this account? (Y/N): " + ColorUtil.RESET);
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (!confirm.equals("Y")) {
            System.out.println(ColorUtil.YELLOW + "Delete account cancelled." + ColorUtil.RESET);
            return;
        }

        accService.deleteAccount(accountNumber);
    }

    private static void handleDeleteCustomer(AccountService accService) {
        System.out.println(ColorUtil.YELLOW + "\n" + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.RED_BOLD + StringUtil.center("DELETE CUSTOMER", 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('=', 40) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Note: All customer accounts must be frozen before deletion." + ColorUtil.RESET);

        String email = getValidEmailWithExit();
        if (email == null) return;

        System.out.print(ColorUtil.CYAN + "Are you sure you want to delete this customer? (Y/N): " + ColorUtil.RESET);
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (!confirm.equals("Y")) {
            System.out.println(ColorUtil.YELLOW + "Delete customer cancelled." + ColorUtil.RESET);
            return;
        }

        accService.deleteCustomerByEmail(email);
    }
    
    private static void viewAllLoans() {
        Bank bank = Bank.getInstance();
        List<Loan> allLoans = bank.getAllLoans();
        
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("ALL LOANS", 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        
        if (allLoans.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No loans found in the system." + ColorUtil.RESET);
            return;
        }
        
        System.out.printf("%-15s %-15s %-15s %-10s %-10s%n", 
    "Loan ID", "Customer ID", "Amount", "Term", "Status");

    System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 80) + ColorUtil.RESET);

    for (Loan loan : allLoans) {
        String statusColor;
        switch (loan.getStatus()) {
            case "APPROVED": statusColor = ColorUtil.GREEN; break;
            case "REJECTED": statusColor = ColorUtil.RED; break;
            case "PENDING": statusColor = ColorUtil.YELLOW; break;
            default: statusColor = ColorUtil.CYAN; break;
        }

        String amountStr = "RM " + String.format("%.2f", loan.getLoanAmount());

        System.out.printf("%-15s %-15s %-15s %-10d %s%s%n",
            loan.getLoanId(),
            loan.getCustomerId(),
            amountStr,
            loan.getTermMonths(),
            statusColor,
            loan.getStatus() + ColorUtil.RESET);
    }

    System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 80) + ColorUtil.RESET);
    System.out.println(ColorUtil.CYAN + "Total Loans: " + allLoans.size() + ColorUtil.RESET);
    }
    
    private static void viewAllFixedDeposits() {
        Bank bank = Bank.getInstance();
        List<FixedDeposit> allFDs = bank.getAllFixedDeposits();
        
        System.out.println(ColorUtil.YELLOW_BOLD + "\n" + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.PURPLE_BOLD + StringUtil.center("ALL FIXED DEPOSITS", 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.YELLOW_BOLD + StringUtil.createLine('=', 80) + ColorUtil.RESET);
        
        if (allFDs.isEmpty()) {
            System.out.println(ColorUtil.YELLOW + "No fixed deposits found in the system." + ColorUtil.RESET);
            return;
        }
        
        System.out.printf("%-12s %-15s %-15s %-10s %-10s%n",
            "FD ID", "Customer ID", "Principal", "Tenure", "Status");
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 80) + ColorUtil.RESET);

        for (FixedDeposit fd : allFDs) {
            String statusColor;
            switch (fd.getStatus()) {
                case "ACTIVE":
                    statusColor = ColorUtil.GREEN;
                    break;
                case "MATURED":
                    statusColor = ColorUtil.CYAN;
                    break;
                default:
                    statusColor = ColorUtil.RED;
                    break;
            }

            String principalStr = "RM " + String.format("%.2f", fd.getPrincipal());

            System.out.printf("%-12s %-15s %-15s %-10d %s%s%n",
                fd.getFdId(),
                fd.getCustomerId(),
                principalStr,
                fd.getTenureMonths(),
                statusColor,
                fd.getStatus() + ColorUtil.RESET);
        }
        System.out.println(ColorUtil.YELLOW + StringUtil.createLine('-', 80) + ColorUtil.RESET);
        System.out.println(ColorUtil.CYAN + "Total Fixed Deposits: " + allFDs.size() + ColorUtil.RESET);
    }
}