package models;

// This is the base class for every person in the system.
// User and Admin both extend this class.
// It is abstract so you can never create a plain Person object directly.

public abstract class Person {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String passwordHash;
    private String createdAt;

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

    // Every subclass must be able to describe itself
    public abstract String getRole();

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Email: " + email + " | Role: " + getRole();
    }
}
