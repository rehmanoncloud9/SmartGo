package models;

// This is the base class for every person in the system.
// User and Admin both extend this class.
// This is critical logic because it is abstract so you can never create a plain Person object directly.
// This ensures that everyone in the system must have a specific role (like User).

public abstract class Person {

    // We use private fields here to keep the data safe and follow encapsulation
    // These fields are protected by getter and setter methods to control how they are changed
    private int id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String createdAt;

    // This constructor sets up the common information shared by all people in the app
    public Person(int id, String name, String email, String phone, String passwordHash, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    // This is an abstract method that every specific person type must implement in their own way
    // This is critical for polymorphism because it allows the system to treat everyone differently based on their role
    public abstract String getRole();

    @Override
    public String toString() {
        // We call getRole() here even though it is not defined yet in this file
        // Java will automatically find the correct implementation from the child class which is User or Admin at runtime
        return "ID: " + id + " | Name: " + name + " | Email: " + email + " | Role: " + getRole();
    }
}
