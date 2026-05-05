package models;

// This is the base class for every person in the system.
// User and Admin both extend this class.
// It is abstract so you can never create a plain Person object directly.

public abstract class Person {

    // We use private fields here to keep the data safe and follow encapsulation
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

    // This is an abstract method that every specific person type must implement
    public abstract String getRole();

    @Override
    public String toString() {
        // We call getRole() here even though it is not defined yet - that is polymorphism
        return "ID: " + id + " | Name: " + name + " | Email: " + email + " | Role: " + getRole();
    }
}
