package services;

import models.User;
import exceptions.SmartGoException;
import data.DataStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Auth Service: Handles login and registration
//
// This is the "brain" behind who can enter the app.
// It checks if a user exists, validates passwords,
// and creates new accounts.
//
// OOP: SERVICE LAYER: keeps logic out of models and out of Main

public class AuthService {

    // The list of all users in memory (loaded from file at start)
    private List<User> users;

    // The user who is currently logged in (null if nobody)
    private User loggedInUser;

    // Constructor: loads users from file when service starts
    public AuthService() {
        this.users = DataStore.loadUsers();
        this.loggedInUser = null;
    }

    // Register: Create a new user account
    public User register(String name, String email, String phone,
                         String password, String address) throws SmartGoException {

        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new SmartGoException("Name cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new SmartGoException("Please enter a valid email address.");
        }
        if (password == null || password.length() < 4) {
            throw new SmartGoException("Password must be at least 4 characters.");
        }

        // Check if email already exists
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new SmartGoException("An account with this email already exists.");
            }
        }

        // Create new user
        int newId = users.size() + 1;
        String now = getCurrentTime();
        String hashedPassword = simpleHash(password); // simple hash for demo

        User newUser = new User(newId, name, email, phone,
                                hashedPassword, now, address, now);

        users.add(newUser);
        DataStore.saveUsers(users); // save to file immediately

        System.out.println("\n  ✅ Account created successfully! Welcome, " + name + "!");
        return newUser;
    }

    // Login: Check credentials and log the user in
    public User login(String email, String password) throws SmartGoException {

        if (email == null || email.trim().isEmpty()) {
            throw new SmartGoException("Please enter your email.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new SmartGoException("Please enter your password.");
        }

        String hashedInput = simpleHash(password);

        // Search for a matching user
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)
                    && u.getPasswordHash().equals(hashedInput)) {

                // Found! Log them in
                loggedInUser = u;
                u.setLastLogin(getCurrentTime());
                DataStore.saveUsers(users); // update last login in file

                System.out.println("\n  ✅ Welcome back, " + u.getName() + "!");
                return u;
            }
        }

        // If we get here, no match was found
        throw new SmartGoException("Incorrect email or password. Please try again.");
    }

    // Logout
    public void logout() {
        if (loggedInUser != null) {
            System.out.println("\n  👋 Goodbye, " + loggedInUser.getName() + "! See you soon.");
            loggedInUser = null;
        }
    }

    // Getters
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    // Private Helpers

    // A very simple "hash": not for real production use.
    // For a real app use BCrypt. For this OOP demo, this is fine.
    private String simpleHash(String input) {
        int hash = 0;
        for (char c : input.toCharArray()) {
            hash = hash * 31 + c;
        }
        return String.valueOf(Math.abs(hash));
    }

    // Get current date and time as a readable string
    private String getCurrentTime() {
        return LocalDateTime.now()
               .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
