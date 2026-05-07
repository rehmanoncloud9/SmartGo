import data.DataStore;
import exceptions.SmartGoException;
import models.User;
import services.*;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Step 1: Start by waking up all our different services and loading their data
        DataStore.init();
        AuthService.init();
        FlightService.init();
        TourPlanService.init();
        HotelService.init();
        BookingService.init();
        ReviewService.init();

        // Step 2: Set the visual theme so the buttons and windows look modern on any computer
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Step 3: Greet the user with a friendly popup message before the main app starts
        JOptionPane.showMessageDialog(null, "Welcome to SmartGo Travel App!", "SmartGo", JOptionPane.INFORMATION_MESSAGE);

        boolean running = true;

        // Step 4: This loop is the heart of the app and keeps it open until the user chooses to exit
        while (running) {
            // We check if a user is logged in to decide which menu to show them
            if (!AuthService.isLoggedIn()) {
                // If nobody is logged in we show the guest menu for joining or signing in
                String[] options = {"Create an account", "Login", "Exit"};
                int choice = JOptionPane.showOptionDialog(null, "What would you like to do?", "Guest Menu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                // Based on the button clicked we trigger the right helper method
                switch (choice) {
                    case 0 -> handleRegister();
                    case 1 -> handleLogin();
                    case 2, JOptionPane.CLOSED_OPTION -> {
                        // If they pick exit or close the window we stop the loop and say goodbye
                        JOptionPane.showMessageDialog(null, "Thank you for using SmartGo. Goodbye!");
                        running = false;
                    }
                }
            } else {
                // Once a user is logged in we show them their personal travel dashboard
                User user = AuthService.getLoggedInUser();
                String[] options = {"Browse Flights", "Browse Tour Plans", "Browse Hotels", "My Bookings", "Reviews", "Logout"};
                int choice = JOptionPane.showOptionDialog(null, "Hello, " + user.getName() + "! What would you like to do?", "Main Menu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                // Each option here leads to a specialized submenu for that travel category
                switch (choice) {
                    case 0 -> handleFlightsMenu();
                    case 1 -> handleTourPlansMenu();
                    case 2 -> handleHotelsMenu();
                    case 3 -> handleBookingsMenu();
                    case 4 -> handleReviewsMenu();
                    case 5 -> AuthService.logout();
                    case JOptionPane.CLOSED_OPTION -> {}
                }
            }
        }
    }

    // This method sets up a custom form with multiple text boxes for new users
    private static void handleRegister() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField addressField = new JTextField();

        // We bundle all the input boxes together into one object to show in the popup
        Object[] message = {
                "Full Name:", nameField,
                "Email:", emailField,
                "Phone:", phoneField,
                "Password:", passwordField,
                "Address:", addressField
        };

        // If the user clicks OK we grab all their text and try to create the account
        int option = JOptionPane.showConfirmDialog(null, message, "Register Account", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                AuthService.register(nameField.getText(), emailField.getText(), phoneField.getText(), 
                        new String(passwordField.getPassword()), addressField.getText());
                JOptionPane.showMessageDialog(null, "Account created successfully!");
            } catch (SmartGoException e) {
                // If something goes wrong like a duplicate email we show the specific error
                JOptionPane.showMessageDialog(null, "Could not create account: " + e.getMessage());
            }
        }
    }

    // Similar to register but we only need two fields for signing in
    private static void handleLogin() {
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "Email:", emailField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                AuthService.login(emailField.getText(), new String(passwordField.getPassword()));
            } catch (SmartGoException e) {
                JOptionPane.showMessageDialog(null, "Login failed: " + e.getMessage());
            }
        }
    }

    // This submenu handles everything related to flight searching and booking
    private static void handleFlightsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            String[] options = {"View all", "Search by destination", "Search by airline", "View reviews", "Book a flight", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "Flights Menu", "Flights",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                // We use our special captureOutput helper to show all flights in a scrollable window
                case 0 -> captureOutput(() -> FlightService.showAllFlights(), "All Flights");
                case 1 -> {
                    // We ask the user for a city name to filter the flights
                    String city = JOptionPane.showInputDialog("Enter destination city:");
                    if (city != null) {
                        var results = FlightService.searchByDestination(city);
                        showResults(results, "Flights to " + city);
                    }
                }
                case 2 -> {
                    // We let the user filter flights by their favorite airline
                    String airline = JOptionPane.showInputDialog("Enter airline name:");
                    if (airline != null) {
                        var results = FlightService.searchByAirline(airline);
                        showResults(results, "Flights by " + airline);
                    }
                }
                case 3 -> {
                    // Users can read feedback from other travelers by entering a flight ID
                    String idStr = JOptionPane.showInputDialog("Enter flight ID:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            captureOutput(() -> ReviewService.showReviews("FLIGHT", id), "Flight Reviews");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Invalid ID"); }
                    }
                }
                case 4 -> {
                    // This triggers the booking process and saves the reservation to the file
                    String idStr = JOptionPane.showInputDialog("Enter flight ID to book:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            BookingService.bookFlight(AuthService.getLoggedInUser().getId(), id);
                            JOptionPane.showMessageDialog(null, "Flight booked!");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Booking failed: " + e.getMessage()); }
                    }
                }
                case 5, JOptionPane.CLOSED_OPTION -> inMenu = false;
            }
        }
    }

    // This submenu deals with organized tour packages and vacation plans
    private static void handleTourPlansMenu() {
        boolean inMenu = true;
        while (inMenu) {
            String[] options = {"View all", "View meal plans", "View reviews", "Book a tour", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "Tour Plans Menu", "Tour Plans",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> captureOutput(() -> TourPlanService.showAllTourPlans(), "All Tour Plans");
                case 1 -> {
                    // Each tour can have different food options which we show here
                    String idStr = JOptionPane.showInputDialog("Enter tour ID:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            captureOutput(() -> TourPlanService.showMealPlansForTour(id), "Meal Plans");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Invalid ID"); }
                    }
                }
                case 2 -> {
                    String idStr = JOptionPane.showInputDialog("Enter tour ID:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            captureOutput(() -> ReviewService.showReviews("TOUR_PLAN", id), "Tour Reviews");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Invalid ID"); }
                    }
                }
                case 3 -> {
                    String idStr = JOptionPane.showInputDialog("Enter tour ID to book:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            BookingService.bookTourPlan(AuthService.getLoggedInUser().getId(), id);
                            JOptionPane.showMessageDialog(null, "Tour booked!");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Booking failed: " + e.getMessage()); }
                    }
                }
                case 4, JOptionPane.CLOSED_OPTION -> inMenu = false;
            }
        }
    }

    // This submenu is where users can find and reserve hotel rooms
    private static void handleHotelsMenu() {
        boolean inMenu = true;
        while (inMenu) {
            String[] options = {"View all", "Search by name", "View reviews", "Book a hotel", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "Hotels Menu", "Hotels",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> captureOutput(() -> HotelService.showAllHotels(), "All Hotels");
                case 1 -> {
                    String name = JOptionPane.showInputDialog("Enter hotel name:");
                    if (name != null) {
                        var results = HotelService.searchByName(name);
                        showResults(results, "Hotels matching " + name);
                    }
                }
                case 2 -> {
                    String idStr = JOptionPane.showInputDialog("Enter hotel ID:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            captureOutput(() -> ReviewService.showReviews("HOTEL", id), "Hotel Reviews");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Invalid ID"); }
                    }
                }
                case 3 -> {
                    // For hotels we need extra details like dates and the number of people staying
                    String idStr = JOptionPane.showInputDialog("Enter hotel ID to book:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            String checkIn = JOptionPane.showInputDialog("Enter Check-in Date (YYYY-MM-DD):");
                            String checkOut = JOptionPane.showInputDialog("Enter Check-out Date (YYYY-MM-DD):");
                            String guestsStr = JOptionPane.showInputDialog("Number of guests:");
                            
                            if (checkIn != null && checkOut != null && guestsStr != null) {
                                int guests = Integer.parseInt(guestsStr);
                                BookingService.bookHotel(AuthService.getLoggedInUser().getId(), id, checkIn, checkOut, guests);
                                JOptionPane.showMessageDialog(null, "Hotel booked!");
                            }
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Booking failed: " + e.getMessage()); }
                    }
                }
                case 4, JOptionPane.CLOSED_OPTION -> inMenu = false;
            }
        }
    }

    // This submenu lets users manage their existing trips and settle their bills
    private static void handleBookingsMenu() {
        boolean inMenu = true;
        int userId = AuthService.getLoggedInUser().getId();
        while (inMenu) {
            String[] options = {"View my bookings", "Cancel a booking", "Pay a bill", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "My Bookings Menu", "Bookings",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> captureOutput(() -> BookingService.showMyBookings(userId), "My Bookings");
                case 1 -> {
                    // Users can cancel any reservation they no longer need
                    String idStr = JOptionPane.showInputDialog("Enter booking ID to cancel:");
                    if (idStr != null) {
                        try {
                            int id = Integer.parseInt(idStr);
                            BookingService.cancelBooking(id, userId);
                            JOptionPane.showMessageDialog(null, "Booking canceled.");
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
                    }
                }
                case 2 -> {
                    // This initiates the payment flow where users can pick a method like Card or Cash
                    String idStr = JOptionPane.showInputDialog("Enter booking ID to pay:");
                    if (idStr != null) {
                        try {
                            int bId = Integer.parseInt(idStr);
                            String[] methods = {"CASH", "CARD", "BANK_TRANSFER"};
                            int mChoice = JOptionPane.showOptionDialog(null, "Select payment method:", "Payment",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, methods, methods[0]);
                            if (mChoice != JOptionPane.CLOSED_OPTION) {
                                BookingService.payBill(bId, userId, methods[mChoice]);
                                JOptionPane.showMessageDialog(null, "Payment processed!");
                            }
                        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Payment failed: " + e.getMessage()); }
                    }
                }
                case 3, JOptionPane.CLOSED_OPTION -> inMenu = false;
            }
        }
    }

    // This submenu is where users can share their experiences about their travels
    private static void handleReviewsMenu() {
        boolean inMenu = true;
        int userId = AuthService.getLoggedInUser().getId();
        while (inMenu) {
            String[] options = {"Write a review", "View my reviews", "Back"};
            int choice = JOptionPane.showOptionDialog(null, "Reviews Menu", "Reviews",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> {
                    // We ask what type of service is being reviewed before taking the rating
                    String[] types = {"FLIGHT", "HOTEL", "TOUR_PLAN"};
                    int typeChoice = JOptionPane.showOptionDialog(null, "What are you reviewing?", "Review Type",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
                    if (typeChoice != JOptionPane.CLOSED_OPTION) {
                        String idStr = JOptionPane.showInputDialog("Enter ID:");
                        String ratingStr = JOptionPane.showInputDialog("Rating (1-5):");
                        String comment = JOptionPane.showInputDialog("Comment:");
                        if (idStr != null && ratingStr != null && comment != null) {
                            try {
                                ReviewService.addReview(userId, types[typeChoice], Integer.parseInt(idStr), Integer.parseInt(ratingStr), comment);
                                JOptionPane.showMessageDialog(null, "Review added!");
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()); }
                        }
                    }
                }
                case 1 -> captureOutput(() -> ReviewService.showMyReviews(userId), "My Reviews");
                case 2, JOptionPane.CLOSED_OPTION -> inMenu = false;
            }
        }
    }

    // This is a specialized tool that grabs anything the app prints to the console and shows it in a GUI window
    private static void captureOutput(Runnable action, String title) {
        // Step 1: Create a place in the computer memory to hold the text temporarily
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        
        // Step 2: Reroute the standard output stream so it goes into our memory buffer instead of the terminal
        System.setOut(ps);
        action.run(); // We run the actual logic (like searching for flights) while the reroute is active
        System.out.flush();
        System.setOut(old); // Step 3: Put the output stream back to normal immediately after finishing
        
        // Step 4: Take all the captured text and put it inside a scrollable box for the user to read
        JTextArea textArea = new JTextArea(baos.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        // Step 5: Display the window with all the information we gathered
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }

    // This helper quickly builds a window to show a list of objects like flights or hotels
    private static void showResults(List<?> list, String title) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No results found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        // We loop through each item and add its textual description to one big string
        list.forEach(item -> sb.append(item.toString()).append("\n\n"));
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.PLAIN_MESSAGE);
    }
}
