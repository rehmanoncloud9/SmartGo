package models;

// A regular user of the SmartGo app.
// Extends Person and adds address and last login info.

public class User extends Person {

    private String address;
    private String lastLogin;

    public User(int id, String name, String email, String phone, String passwordHash, String createdAt, String address, String lastLogin) {
        super(id, name, email, phone, passwordHash, createdAt);
        this.address = address;
        this.lastLogin = lastLogin;
    }

    public String getAddress() { return address; }
    public String getLastLogin() { return lastLogin; }

    public void setAddress(String address) { this.address = address; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String getRole() {
        return "User";
    }

    @Override
    public String toString() {
        return super.toString() + " | Address: " + address;
    }
}
