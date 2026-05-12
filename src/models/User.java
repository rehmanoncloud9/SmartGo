package models;

// A regular user of the SmartGo app.
// This is critical logic because it extends Person to inherit all the basic fields like name and email.

public class User extends Person {

    // These extra fields are specific to regular users and are not part of the base
    // Person class
    private String address;
    private String lastLogin;

    // This constructor is critical because it uses super() to send data back to the
    // parent class
    // This allows the base class to handle the core identity while we focus on the
    // user details
    public User(int id, String name, String email, String phone, String passwordHash, String createdAt, String address,
            String lastLogin) {
        super(id, name, email, phone, passwordHash, createdAt);
        this.address = address;
        this.lastLogin = lastLogin;
    }

    public String getAddress() {
        return address;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String getRole() {
        // This is a critical override because it tells the whole system that this
        // person is a regular user
        // Any code calling getRole() on a Person object will see "User" if the object
        // was created as this class
        return "User";
    }

    @Override
    public String toString() {
        // We call super.toString() to get the standard description and then tack on the
        // address
        return super.toString() + " | Address: " + address;
    }
}
