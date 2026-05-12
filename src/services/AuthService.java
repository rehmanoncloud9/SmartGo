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

    // This creates a new account for a user after checking if the email is
    // available
    public static User register(String name, String email, String phone, String password, String address)
            throws SmartGoException {
        // Step 1: Load the existing user list from the file to check for duplicates
        List<User> users = DataStore.loadUsers();

        // Step 2: Loop through every user to make sure this email hasn't been used
        // before
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                // If we find a match we stop the registration and show a helpful warning
                throw new SmartGoException("An account with this email already exists. Please use a different email.");
            }
        }

        String createdAt = LocalDateTime.now().format(formatter);

        // Step 3: Create the new user object with a unique ID and the current timestamp
        User newUser = new User(nextUserId++, name, email, phone, password, createdAt, address, createdAt);
        // Step 4: Save the new user record permanently into our text database
        DataStore.saveUser(newUser);

        System.out.println("Account created successfully! Welcome, " + name + ".");
        return newUser;
    }

    // This verifies credentials and sets the session for the user
    public static User login(String email, String password) throws SmartGoException {
        // Step 1: Retrieve all registered users to search for the login credentials
        List<User> users = DataStore.loadUsers();

        // Step 2: Compare the provided email and password against each record in the
        // list
        for (User u : users) {
            // We check for an exact match on both pieces of information
            if (u.getEmail().equalsIgnoreCase(email) && u.getPasswordHash().equals(password)) {
                // Step 3: If matched, we store this user in memory as the "logged in" user for
                // this session
                loggedInUser = u;
                System.out.println("Welcome back, " + u.getName() + "!");
                return u;
            }
        }

        // Step 4: If we finish the loop without finding a match we inform the user they
        // made a mistake
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
