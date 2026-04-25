package models;

// Admin: A staff member who manages Tour Plans on SmartGo
//
// OOP Concepts:
//   - INHERITANCE: Admin extends Person
//   - POLYMORPHISM: getRole() returns "Admin"

public class Admin extends Person {

    // Admins have a specific job title, like "Tour Manager"
    private String adminRole;
    private String lastLogin;

    // Constructor
    public Admin(int id, String name, String email,
                 String phone, String passwordHash,
                 String createdAt, String adminRole, String lastLogin) {

        super(id, name, email, phone, passwordHash, createdAt);

        this.adminRole = adminRole;
        this.lastLogin = lastLogin;
    }

    // Admin answers the abstract method from Person
    @Override
    public String getRole() {
        return "Admin";
    }

    // Show extra admin info on top of Person's info
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("  Admin Role : " + adminRole);
        System.out.println("  Last Login : " + lastLogin);
        System.out.println("==============================");
    }

    // Getters and Setters
    public String getAdminRole() { return adminRole; }
    public String getLastLogin() { return lastLogin; }

    public void setAdminRole(String adminRole) { this.adminRole = adminRole; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    // Save to file
    @Override
    public String toFileString() {
        return "ADMIN," + super.toFileString() + "," + adminRole + "," + lastLogin;
    }
}
