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

    private static User loggedInUser = null;
    private static int nextUserId = 1;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Load users from file when the app starts to get the right next ID
    public static void init() {
        List<User> existing = DataStore.loadUsers();
        if (!existing.isEmpty()) {
            nextUserId = existing.get(existing.size() - 1).getId() + 1;
        }
    }

    // Register a new user account
    // Throws an exception if the email is already taken
    public static User register(String name, String email, String phone, String password, String address) throws SmartGoException {
        List<User> users = DataStore.loadUsers();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new SmartGoException("An account with this email already exists. Please use a different email.");
            }
        }

        String createdAt = LocalDateTime.now().format(formatter);

        // In a real app you would hash the password properly.
        // For this project we store it as-is to keep things simple.
        User newUser = new User(nextUserId++, name, email, phone, password, createdAt, address, createdAt);
        DataStore.saveUser(newUser);

        System.out.println("Account created successfully! Welcome, " + name + ".");
        return newUser;
    }

    // Login with email and password
    // Throws an exception if the credentials are wrong
    public static User login(String email, String password) throws SmartGoException {
        List<User> users = DataStore.loadUsers();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(password)) {
                loggedInUser = u;
                System.out.println("Welcome back, " + u.getName() + "!");
                return u;
            }
        }

        throw new SmartGoException("Incorrect email or password. Please try again.");
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
