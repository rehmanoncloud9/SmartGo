package models;

// User: A traveller who uses SmartGo to book trips
//
// OOP Concepts:
//   - INHERITANCE: User extends Person (gets all Person fields/methods)
//   - ENCAPSULATION: has its own private fields too

public class User extends Person {

    // Extra info that only a User has (not Admin)
    private String address;
    private String lastLogin;

    // Constructor
    // "super(...)" calls the Person constructor first,
    // then we set the User-specific stuff
    public User(int id, String name, String email,
                String phone, String passwordHash,
                String createdAt, String address, String lastLogin) {

        // Pass shared fields up to Person
        super(id, name, email, phone, passwordHash, createdAt);

        // Set User-only fields
        this.address   = address;
        this.lastLogin = lastLogin;
    }

    // Polymorphism in action
    // Person said: "every subclass must tell me their role"
    // User answers: "I am a Traveller"
    @Override
    public String getRole() {
        return "Traveller";
    }

    // Override displayInfo() to also show address and last login
    @Override
    public void displayInfo() {
        super.displayInfo(); // first show what Person shows
        System.out.println("  Address    : " + address);
        System.out.println("  Last Login : " + lastLogin);
        System.out.println("==============================");
    }

    // Getters and Setters for User-only fields
    public String getAddress()   { return address;   }
    public String getLastLogin() { return lastLogin; }

    public void setAddress(String address)     { this.address   = address;   }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }

    // Save to file: adds User-specific fields to Person's line
    @Override
    public String toFileString() {
        return "USER," + super.toFileString() + "," + address + "," + lastLogin;
    }
}
