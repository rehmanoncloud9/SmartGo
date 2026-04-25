import data.DataStore;
import exceptions.SmartGoException;
import models.User;
import services.*;

import java.util.Scanner;

// This is the entry point of the SmartGo travel app.
// Everything starts here. The menu lets the user navigate
// through all features of the application.

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Set up the data folder and load all saved data
        DataStore.init();
        AuthService.init();
        FlightService.init();
        TourPlanService.init();
        BookingService.init();
        ReviewService.init();

        System.out.println("=====================================");
        System.out.println("   Welcome to SmartGo Travel App!   ");
        System.out.println("=====================================");

        boolean running = true;

        while (running) {
            if (!AuthService.isLoggedIn()) {
                showGuestMenu();
                int choice = readInt();

                switch (choice) {
                    case 1 -> handleRegister();
                    case 2 -> handleLogin();
                    case 3 -> {
                        System.out.println("Thank you for using SmartGo. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }

            } else {
                showMainMenu();
                int choice = readInt();

                switch (choice) {
                    case 1 -> handleFlightsMenu();
                    case 2 -> handleTourPlansMenu();
                    case 3 -> handleBookingsMenu();
                    case 4 -> handleReviewsMenu();
                    case 5 -> AuthService.logout();
                    default -> System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                }
            }
        }

        scanner.close();
    }

    // The menu shown when no one is logged in
    private static void showGuestMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Create an account");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Your choice: ");
    }

    // The main menu shown after login
    private static void showMainMenu() {
        User user = AuthService.getLoggedInUser();
        System.out.println("\nHello, " + user.getName() + "! What would you like to do?");
        System.out.println("1. Browse Flights");
        System.out.println("2. Browse Tour Plans");
        System.out.println("3. My Bookings");
        System.out.println("4. Reviews");
        System.out.println("5. Logout");
        System.out.print("Your choice: ");
    }

    // Register a new account
    private static void handleRegister() {
        System.out.println("\nCreate Your Account");
        System.out.println("--------------------");

        System.out.print("Full name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Home address: ");
        String address = scanner.nextLine().trim();

        try {
            AuthService.register(name, email, phone, password, address);
        } catch (SmartGoException e) {
            System.out.println("Could not create account: " + e.getMessage());
        }
    }

    // Login to an existing account
    private static void handleLogin() {
        System.out.println("\nLogin to Your Account");
        System.out.println("----------------------");

        System.out.print("Email address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            AuthService.login(email, password);
        } catch (SmartGoException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    // Flights sub-menu
    private static void handleFlightsMenu() {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nFlights");
            System.out.println("--------");
            System.out.println("1. View all flights");
            System.out.println("2. Search flights by destination");
            System.out.println("3. Search flights by airline");
            System.out.println("4. View reviews for a flight");
            System.out.println("5. Book a flight");
            System.out.println("6. Go back");
            System.out.print("Your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> FlightService.showAllFlights();
                case 2 -> {
                    System.out.print("Enter destination city: ");
                    String city = scanner.nextLine().trim();
                    var results = FlightService.searchByDestination(city);
                    if (results.isEmpty()) {
                        System.out.println("No flights found to " + city + ".");
                    } else {
                        results.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    System.out.print("Enter airline name: ");
                    String airline = scanner.nextLine().trim();
                    var results = FlightService.searchByAirline(airline);
                    if (results.isEmpty()) {
                        System.out.println("No flights found for airline: " + airline + ".");
                    } else {
                        results.forEach(System.out::println);
                    }
                }
                case 4 -> {
                    System.out.print("Enter flight ID to see reviews: ");
                    int id = readInt();
                    ReviewService.showReviews("FLIGHT", id);
                }
                case 5 -> {
                    System.out.print("Enter the flight ID you want to book: ");
                    int id = readInt();
                    try {
                        BookingService.bookFlight(AuthService.getLoggedInUser().getId(), id);
                    } catch (SmartGoException e) {
                        System.out.println("Booking failed: " + e.getMessage());
                    }
                }
                case 6 -> inMenu = false;
                default -> System.out.println("Please enter a number from 1 to 6.");
            }
        }
    }

    // Tour Plans sub-menu
    private static void handleTourPlansMenu() {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\nTour Plans");
            System.out.println("-----------");
            System.out.println("1. View all tour plans");
            System.out.println("2. View meal plans for a tour");
            System.out.println("3. View reviews for a tour plan");
            System.out.println("4. Book a tour plan");
            System.out.println("5. Go back");
            System.out.print("Your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> TourPlanService.showAllTourPlans();
                case 2 -> {
                    System.out.print("Enter tour plan ID: ");
                    int id = readInt();
                    TourPlanService.showMealPlansForTour(id);
                }
                case 3 -> {
                    System.out.print("Enter tour plan ID to see reviews: ");
                    int id = readInt();
                    ReviewService.showReviews("TOUR_PLAN", id);
                }
                case 4 -> {
                    System.out.print("Enter the tour plan ID you want to book: ");
                    int id = readInt();
                    try {
                        BookingService.bookTourPlan(AuthService.getLoggedInUser().getId(), id);
                    } catch (SmartGoException e) {
                        System.out.println("Booking failed: " + e.getMessage());
                    }
                }
                case 5 -> inMenu = false;
                default -> System.out.println("Please enter a number from 1 to 5.");
            }
        }
    }

    // Bookings sub-menu
    private static void handleBookingsMenu() {
        boolean inMenu = true;
        int userId = AuthService.getLoggedInUser().getId();

        while (inMenu) {
            System.out.println("\nMy Bookings");
            System.out.println("------------");
            System.out.println("1. View all my bookings");
            System.out.println("2. Cancel a booking");
            System.out.println("3. Pay a bill");
            System.out.println("4. Go back");
            System.out.print("Your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> BookingService.showMyBookings(userId);
                case 2 -> {
                    System.out.print("Enter the booking ID you want to cancel: ");
                    int id = readInt();
                    try {
                        BookingService.cancelBooking(id, userId);
                    } catch (SmartGoException e) {
                        System.out.println("Could not cancel: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter the booking ID you want to pay for: ");
                    int bookingId = readInt();
                    System.out.println("Payment method:");
                    System.out.println("1. Cash");
                    System.out.println("2. Card");
                    System.out.println("3. Bank Transfer");
                    System.out.print("Your choice: ");
                    int methodChoice = readInt();
                    String method = switch (methodChoice) {
                        case 1 -> "CASH";
                        case 2 -> "CARD";
                        case 3 -> "BANK_TRANSFER";
                        default -> "CASH";
                    };
                    try {
                        BookingService.payBill(bookingId, userId, method);
                    } catch (SmartGoException e) {
                        System.out.println("Payment failed: " + e.getMessage());
                    }
                }
                case 4 -> inMenu = false;
                default -> System.out.println("Please enter a number from 1 to 4.");
            }
        }
    }

    // Reviews sub-menu
    private static void handleReviewsMenu() {
        boolean inMenu = true;
        int userId = AuthService.getLoggedInUser().getId();

        while (inMenu) {
            System.out.println("\nReviews");
            System.out.println("--------");
            System.out.println("1. Write a review");
            System.out.println("2. View my reviews");
            System.out.println("3. Go back");
            System.out.print("Your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("What are you reviewing?");
                    System.out.println("1. Flight");
                    System.out.println("2. Hotel");
                    System.out.println("3. Tour Plan");
                    System.out.print("Your choice: ");
                    int typeChoice = readInt();

                    String reviewableType = switch (typeChoice) {
                        case 1 -> "FLIGHT";
                        case 2 -> "HOTEL";
                        case 3 -> "TOUR_PLAN";
                        default -> "FLIGHT";
                    };

                    System.out.print("Enter the ID of the " + reviewableType + " you are reviewing: ");
                    int reviewableId = readInt();

                    System.out.print("Your rating (1 to 5): ");
                    int rating = readInt();

                    System.out.print("Your comment: ");
                    String comment = scanner.nextLine().trim();

                    try {
                        ReviewService.addReview(userId, reviewableType, reviewableId, rating, comment);
                    } catch (SmartGoException e) {
                        System.out.println("Could not save review: " + e.getMessage());
                    }
                }
                case 2 -> ReviewService.showMyReviews(userId);
                case 3 -> inMenu = false;
                default -> System.out.println("Please enter 1, 2, or 3.");
            }
        }
    }

    // A safe way to read an integer from the user
    // If the user types something that is not a number, it returns -1 instead of crashing
    private static int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
