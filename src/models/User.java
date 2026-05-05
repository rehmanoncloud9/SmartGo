package models;

// A regular user of the SmartGo app.
// Extends Person and adds address and last login info.

public class User extends Person {

    // These extra fields are specific to regular users and not shared with everyone else
    private String address;
    private String lastLogin;

    // We pass most of the data up to the Person class using super() to avoid code duplication
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
        // This is where we define the specific role for this class
        return "User";
    }

    @Override
    public String toString() {
        // We reuse the parent description and add the address at the end
        return super.toString() + " | Address: " + address;
    }
}
