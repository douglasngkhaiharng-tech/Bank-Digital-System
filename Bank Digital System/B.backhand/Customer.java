import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String registeredDate;
    private boolean isActive;
    private List<String> accountNumbers;

    public Customer(String name, String email, String phone, String address, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        this.customerId = generateCustomerId();
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.phone = phone.trim();
        this.address = address.trim();
        this.password = password;
        this.registeredDate = new java.util.Date().toString();
        this.isActive = true;
        this.accountNumbers = new ArrayList<>();
    }

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis() % 1000000;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<String> getAccountNumbers() {
        return accountNumbers;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setName(String name) {
        if (!ValidationUtil.isValidName(name)) {
            throw new IllegalArgumentException(ValidationUtil.getValidNameMessage());
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException(ValidationUtil.getValidEmailMessage());
        }
        this.email = email.trim().toLowerCase();
    }

    public void setPhone(String phone) {
        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException(ValidationUtil.getValidPhoneMessage());
        }
        this.phone = phone.trim();
    }

    public void setAddress(String address) {
        if (address == null || address.trim().length() < 5) {
            throw new IllegalArgumentException("Address must be at least 5 characters!");
        }
        this.address = address.trim();
    }

    public void addAccountNumber(String accountNumber) {
        if (accountNumber != null && !accountNumber.trim().isEmpty() && !accountNumbers.contains(accountNumber)) {
            accountNumbers.add(accountNumber);
        }
    }

    public void removeAccountNumber(String accountNumber) {
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            accountNumbers.remove(accountNumber);
        }
    }

    public boolean validatePassword(String inputPassword) {
        if (inputPassword == null) return false;
        return this.password.equals(inputPassword);
    }

    public String getCustomerInfo() {
        String title = StringUtil.center("CUSTOMER DETAILS", 66);

        return "\n" +
            "+--------------------------------------------------------------------+\n" +
            "| " + title + " |\n" +
            "+--------------------------------------------------------------------+\n" +
            String.format("| %-20s : %-43s |\n", "Customer ID", customerId) +
            String.format("| %-20s : %-43s |\n", "Name", name.length() > 43 ? name.substring(0, 40) + "..." : name) +
            String.format("| %-20s : %-43s |\n", "Email", email.length() > 43 ? email.substring(0, 40) + "..." : email) +
            String.format("| %-20s : %-43s |\n", "Phone", phone) +
            String.format("| %-20s : %-43s |\n", "Address", address.length() > 43 ? address.substring(0, 40) + "..." : address) +
            String.format("| %-20s : %-43s |\n", "Status", isActive ? "Active" : "Inactive") +
            String.format("| %-20s : %-43s |\n", "Registered Date",
                registeredDate.length() > 43 ? registeredDate.substring(0, 40) + "..." : registeredDate) +
            "+--------------------------------------------------------------------+";
    }
}