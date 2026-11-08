import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._-]+@example\\.com$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^01[0-9]{8,9}$"
    );
    
    private static final Pattern CUSTOMER_ID_PATTERN = Pattern.compile(
        "^CUST[0-9]+$"
    );
    
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(
        "^ACC[A-Z0-9]+$"
    );
    
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z\\s]+$"
    );
    
    public static boolean isValidName(String name) {
        if (name == null) return false;
        String trimmed = name.trim();
        return trimmed.length() >= 2 && NAME_PATTERN.matcher(trimmed).matches();
    }
    
    public static String getValidNameMessage() {
        return "Name must be at least 2 characters and contain only letters and spaces.";
    }
    
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String trimmed = email.trim().toLowerCase();
        return EMAIL_PATTERN.matcher(trimmed).matches();
    }
    
    public static String getValidEmailMessage() {
        return "Email must end with @example.com (e.g., username@example.com).";
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    public static String getValidPhoneMessage() {
        return "Phone number must start with 01 and have 10-11 digits total. Example: 0123456789";
    }
    
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.trim().length() >= 6;
    }
    
    public static String getValidPasswordMessage() {
        return "Password must be at least 6 characters long.";
    }
    
    public static boolean isValidAmount(double amount, double min, double max) {
        return amount >= min && amount <= max;
    }
    
    public static String getValidAmountMessage(double min, double max) {
        return "Amount must be between " + min + " and " + max + ".";
    }
    
    public static boolean isValidCustomerId(String customerId) {
        if (customerId == null) return false;
        return CUSTOMER_ID_PATTERN.matcher(customerId.trim()).matches();
    }
    
    public static String getValidCustomerIdMessage() {
        return "Customer ID must start with 'CUST' followed by numbers.";
    }
    
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null) return false;
        return ACCOUNT_NUMBER_PATTERN.matcher(accountNumber.trim()).matches();
    }
    
    public static String getValidAccountNumberMessage() {
        return "Account number must start with 'ACC'.";
    }
    
    public static boolean isValidInterestRate(double rate) {
        return rate >= 0 && rate <= 0.1;
    }
    
    public static String getValidInterestRateMessage() {
        return "Interest rate must be between 0 and 0.1 (0% to 10%).";
    }
    
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    public static String getNotEmptyMessage(String fieldName) {
        return fieldName + " cannot be empty or only spaces.";
    }
}