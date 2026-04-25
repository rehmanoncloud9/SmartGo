package models;

// Person: The base class for everyone in the system
//
// Think of it like this: every human in SmartGo is a Person.
// But we never say "I am just a Person." We say:
//     "I am a User" or "I am an Admin."
// That is why this class is ABSTRACT: you can't create it directly.
//
// OOP Concepts used here:
//   - ABSTRACTION: hiding details, showing only what matters
//   - INHERITANCE: User and Admin will inherit everything here
//   - ENCAPSULATION: all fields are private, accessed via getters/setters

public abstract class Person {

    // private = only THIS class can touch these directly
    // everyone else must use getters and setters (that's encapsulation)
    private int    id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash; // NEVER store plain passwords
    private String createdAt;    // date this person joined

    // Constructor
    // When User or Admin is created, they call this first
    // using the keyword "super(...)"
    public Person(int id, String name, String email,
                  String phone, String passwordHash, String createdAt) {
        this.id           = id;
        this.name         = name;
        this.email        = email;
        this.phone        = phone;
        this.passwordHash = passwordHash;
        this.createdAt    = createdAt;
    }

    // Abstract Method: every subclass MUST implement this
    // A User will return "Traveller"
    // An Admin will return "Admin"
    // OOP: this is POLYMORPHISM: same method name, different result
    public abstract String getRole();

    // Display this person's info on the console
    // Subclasses can override this to show extra info
    public void displayInfo() {
        System.out.println("==============================");
        System.out.println("  Name  : " + name);
        System.out.println("  Email : " + email);
        System.out.println("  Phone : " + phone);
        System.out.println("  Role  : " + getRole());
        System.out.println("  Since : " + createdAt);
        System.out.println("==============================");
    }

    // Getters: read-only access to private fields
    public int    getId()           { return id;           }
    public String getName()         { return name;         }
    public String getEmail()        { return email;        }
    public String getPhone()        { return phone;        }
    public String getPasswordHash() { return passwordHash; }
    public String getCreatedAt()    { return createdAt;    }

    // Setters: controlled write access
    public void setName(String name)   { this.name  = name;  }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    // Converts this object to a line of text for saving to a file
    // Example output: "1,Ali,ali@gmail.com,03001234567,abc123hash,2024-04-20"
    public String toFileString() {
        return id + "," + name + "," + email + ","
             + phone + "," + passwordHash + "," + createdAt;
    }
}
