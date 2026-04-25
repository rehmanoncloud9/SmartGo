import services.*;
import models.*;
import exceptions.SmartGoException;
import data.DataStore;

import java.util.List;
import java.util.Scanner;

// Main: The entry point of SmartGo
//
// This is the MENU-DRIVEN SYSTEM required for Week 3.
// Everything runs from here:
//   - The app starts
//   - Shows a menu
//   - User picks an option
//   - We call the right service
//   - Loop back to menu
//
// Think of Main as the "receptionist" of the app.
// It doesn't DO the work — it just directs you to the right
// service (AuthService, FlightService, BookingService, etc.)

public class Main {

    // These are our services: the brains of each feature
    static AuthService   authService   = new AuthService();
    static FlightService flightService = new FlightService();
    static BookingService bookingService = new BookingService();
    static ReviewService reviewService  = new ReviewService();

    // Scanner reads what the user types
    static Scanner scanner = new Scanner(System.in);

    // App Entry Point
    public static void main(String[] args) {

        // Initialize data folder for file handling
        DataStore.initialize();

        printBanner();

        // Main Loop: keeps running until user exits
        boolean running = true;
        while (running) {

            // If nobody is logged in, show the welcome menu
            if (!authService.isLoggedIn()) {
                running = showWelcomeMenu();
            } else {
                // Someone is logged in, show the main app menu
                running = showMainMenu();
            }
        }

        System.out.println("\n  Thank you for using SmartGo. Have a great trip! ✈");
        scanner.close();
    }

    // Welcome Menu: shown when nobody is logged in
    private static boolean showWelcomeMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║       WELCOME TO SmartGo     ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Login                    ║");
        System.out.println("║  2. Register                 ║");
        System.out.println("║  3. Browse Flights (Guest)   ║");
        System.out.println("║  0. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("\n  Enter your choice: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": handleLogin();           break;
            case "2": handleRegister();        break;
            case "3": handleBrowseFlights();   break;
            case "0": return false;            // exit app
            default:
                System.out.println("\n  ❌ Invalid choice. Please enter 1, 2, 3, or 0.");
        }

        return true; // keep running
    }

    // Main Menu: shown after login
    private static boolean showMainMenu() {
        User user = authService.getLoggedInUser();

        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║  Hello, " + padRight(user.getName(), 28) + "║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  1. Browse Flights                   ║");
        System.out.println("║  2. Book a Flight                    ║");
        System.out.println("║  3. My Bookings                      ║");
        System.out.println("║  4. Cancel a Booking                 ║");
        System.out.println("║  5. View / Pay Bill                  ║");
        System.out.println("║  6. Leave a Review                   ║");
        System.out.println("║  7. My Profile                       ║");
        System.out.println("║  8. Logout                           ║");
        System.out.println("║  0. Exit                             ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("\n  Enter your choice: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1": handleBrowseFlights();               break;
            case "2": handleBookFlight();                  break;
            case "3": handleMyBookings();                  break;
            case "4": handleCancelBooking();               break;
            case "5": handleBillAndPayment();              break;
            case "6": handleLeaveReview();                 break;
            case "7": user.displayInfo();                  break;
            case "8": authService.logout();                break;
            case "0": return false;
            default:
                System.out.println("\n  ❌ Invalid choice. Please try again.");
        }

        return true;
    }

    // Handler: Login
    private static void handleLogin() {
        System.out.println("\n  ──── LOGIN ────");

        System.out.print("  Email    : ");
        String email = scanner.nextLine().trim();

        System.out.print("  Password : ");
        String password = scanner.nextLine().trim();

        try {
            authService.login(email, password);
        } catch (SmartGoException e) {
            // SmartGoException gives us a human-friendly message
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Handler: Register
    private static void handleRegister() {
        System.out.println("\n  ──── CREATE AN ACCOUNT ────");

        System.out.print("  Full Name : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Email     : ");
        String email = scanner.nextLine().trim();

        System.out.print("  Phone     : ");
        String phone = scanner.nextLine().trim();

        System.out.print("  Address   : ");
        String address = scanner.nextLine().trim();

        System.out.print("  Password  : ");
        String password = scanner.nextLine().trim();

        try {
            authService.register(name, email, phone, password, address);
        } catch (SmartGoException e) {
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Handler: Browse Flights
    private static void handleBrowseFlights() {
        flightService.displayAllFlights();
    }

    // Handler: Book a Flight
    private static void handleBookFlight() {
        User user = authService.getLoggedInUser();

        // First show available flights
        flightService.displayAllFlights();

        System.out.print("\n  Enter Flight ID to book: ");
        try {
            int flightId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Number of passengers  : ");
            int passengers = Integer.parseInt(scanner.nextLine().trim());

            Flight flight = flightService.getFlightById(flightId);
            bookingService.bookFlight(user, flight, passengers);

        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Please enter a valid number.");
        } catch (SmartGoException e) {
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Handler: My Bookings
    private static void handleMyBookings() {
        User user = authService.getLoggedInUser();
        bookingService.displayUserBookings(user);
    }

    // Handler: Cancel a Booking
    private static void handleCancelBooking() {
        User user = authService.getLoggedInUser();

        // Show their bookings first
        bookingService.displayUserBookings(user);

        System.out.print("\n  Enter Booking ID to cancel: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());
            bookingService.cancelBooking(bookingId, user);
        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Please enter a valid number.");
        } catch (SmartGoException e) {
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Handler: View Bill and Pay
    private static void handleBillAndPayment() {
        User user = authService.getLoggedInUser();

        System.out.print("\n  Enter Booking ID to view bill: ");
        try {
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            // Show the bill
            bookingService.displayBill(bookingId);

            // Ask if they want to pay advance
            System.out.print("\n  Pay advance now? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("yes") || answer.equals("y")) {
                System.out.println("  Payment methods: CASH / CARD / BANK_TRANSFER");
                System.out.print("  Choose method: ");
                String method = scanner.nextLine().trim().toUpperCase();

                bookingService.processAdvancePayment(bookingId, method, user);
            }

        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Please enter a valid number.");
        } catch (SmartGoException e) {
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Handler: Leave a Review
    private static void handleLeaveReview() {
        User user = authService.getLoggedInUser();

        System.out.println("\n  ──── LEAVE A REVIEW ────");
        System.out.println("  What would you like to review?");
        System.out.println("  1. Flight");
        System.out.println("  2. Hotel");
        System.out.print("\n  Choice: ");

        String typeChoice = scanner.nextLine().trim();
        String reviewType;

        switch (typeChoice) {
            case "1": reviewType = "FLIGHT";    break;
            case "2": reviewType = "HOTEL";     break;
            default:
                System.out.println("\n  ❌ Invalid choice.");
                return;
        }

        try {
            System.out.print("  Enter the ID of the " + reviewType.toLowerCase() + ": ");
            int refId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Your comment: ");
            String comment = scanner.nextLine().trim();

            reviewService.addReview(user, refId, reviewType, rating, comment);

        } catch (NumberFormatException e) {
            System.out.println("\n  ❌ Please enter a valid number.");
        } catch (SmartGoException e) {
            System.out.println("\n  ❌ " + e.getMessage());
        }
    }

    // Helpers

    private static void printBanner() {
        System.out.println("\n");
        System.out.println("  ███████╗███╗   ███╗ █████╗ ██████╗ ████████╗ ██████╗  ██████╗ ");
        System.out.println("  ██╔════╝████╗ ████║██╔══██╗██╔══██╗╚══██╔══╝██╔════╝ ██╔═══██╗");
        System.out.println("  ███████╗██╔████╔██║███████║██████╔╝   ██║   ██║  ███╗██║   ██║");
        System.out.println("  ╚════██║██║╚██╔╝██║██╔══██║██╔══██╗   ██║   ██║   ██║██║   ██║");
        System.out.println("  ███████║██║ ╚═╝ ██║██║  ██║██║  ██║   ██║   ╚██████╔╝╚██████╔╝");
        System.out.println("  ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ");
        System.out.println("\n            Smart Travel Planner: Your journey starts here.\n");
    }

    // Pad a string to a fixed width (for neat menu formatting)
    private static String padRight(String text, int width) {
        if (text.length() >= width) return text.substring(0, width);
        return text + " ".repeat(width - text.length());
    }
}
