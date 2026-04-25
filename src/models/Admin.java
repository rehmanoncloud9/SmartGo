package models;

// An admin who can add flights, hotels, and tour plans.
// Extends Person and adds an admin role label.

public class Admin extends Person {

    private String role;
    private String lastLogin;

    public Admin(int id, String name, String email, String phone, String passwordHash, String createdAt, String role, String lastLogin) {
        super(id, name, email, phone, passwordHash, createdAt);
        this.role = role;
        this.lastLogin = lastLogin;
    }

    public String getAdminRole() { return role; }
    public String getLastLogin() { return lastLogin; }

    public void setAdminRole(String role) { this.role = role; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String toString() {
        return super.toString() + " | Admin Role: " + role;
    }
}
