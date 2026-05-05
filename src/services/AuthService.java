package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// AuthService handles everything related to user accounts.
// It lets users register, login, and logout.
// It also keeps track of who is currently logged in.

public class AuthService {

    // This variable keeps track of who is currently using the app
    private static User loggedInUser = null;
    private static int nextUserId = 1;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // We check the last registered user in the file to decide the next unique ID
    public static void init() {
        List<User> existing = DataStore.loadUsers();
        if (!existing.isEmpty()) {
            nextUserId = existing.get(existing.size() - 1).getId() + 1;
        }
    }

    // This creates a new account for a user after checking if the email is available
    public static User register(String name, String email, String phone, String password, String address) throws SmartGoException {
        List<User> users = DataStore.loadUsers();

        // We loop through all users to make sure the email is not already taken
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new SmartGoException("An account with this email already exists. Please use a different email.");
            }
        }

        String createdAt = LocalDateTime.now().format(formatter);

        // We store the password as-is for now, which is common for lab projects
        User newUser = new User(nextUserId++, name, email, phone, password, createdAt, address, createdAt);
        DataStore.saveUser(newUser);

        System.out.println("Account created successfully! Welcome, " + name + ".");
        return newUser;
    }

    // This verifies credentials and sets the session for the user
    public static User login(String email, String password) throws SmartGoException {
        List<User> users = DataStore.loadUsers();

        for (User u : users) {
            // Both the email and password must match exactly
            if (u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(password)) {
                loggedInUser = u;
                System.out.println("Welcome back, " + u.getName() + "!");
                return u;
            }
        }

        // If we reach this point, it means we didn't find a matching account
        throw new SmartGoException("Incorrect email or password. Please try again.");
    }

    // Overloaded login method - Polymorphism (Compile-time)
    public static User login(int userId, String password) throws SmartGoException {
        List<User> users = DataStore.loadUsers();

        for (User u : users) {
            if (u.getId() == userId && u.getPasswordHash().equals(password)) {
                loggedInUser = u;
                System.out.println("Welcome back, " + u.getName() + "!");
                return u;
            }
        }

        throw new SmartGoException("Incorrect ID or password. Please try again.");
    }

    // Logout the current user
    public static void logout() {
        if (loggedInUser != null) {
            System.out.println("You have been logged out. Goodbye, " + loggedInUser.getName() + "!");
            loggedInUser = null;
        } else {
            System.out.println("You are not currently logged in.");
        }
    }

    // Get the user who is currently logged in
    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // Check if anyone is logged in
    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}


