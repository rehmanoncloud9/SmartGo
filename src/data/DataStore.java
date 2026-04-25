package data;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Data Store: Handles saving and loading data from files

//  This is the FILE HANDLING part of Week 3.
//  Instead of a database for now, we save everything to .txt files.
//  Each line in the file = one object.
//


public class DataStore {

    // File paths: where we store all data
    // All files go into a "app_data/" folder
    private static final String DATA_FOLDER    = "app_data/";
    private static final String USERS_FILE     = DATA_FOLDER + "users.txt";
    private static final String FLIGHTS_FILE   = DATA_FOLDER + "flights.txt";
    private static final String HOTELS_FILE    = DATA_FOLDER + "hotels.txt";
    private static final String BOOKINGS_FILE  = DATA_FOLDER + "bookings.txt";
    private static final String BILLS_FILE     = DATA_FOLDER + "bills.txt";
    private static final String PAYMENTS_FILE  = DATA_FOLDER + "payments.txt";
    private static final String REVIEWS_FILE   = DATA_FOLDER + "reviews.txt";
    private static final String TOURPLANS_FILE = DATA_FOLDER + "tourplans.txt";
    private static final String MEALPLANS_FILE = DATA_FOLDER + "mealplans.txt";
    private static final String DESTINATIONS_FILE = DATA_FOLDER + "destinations.txt";

    // Make sure the data folder exists when app starts
    public static void initialize() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir(); // create the folder if it doesn't exist
            System.out.println("  [DataStore] Data folder created.");
        }
    }

    // Generic Helpers: used by all save/load methods below

    // Write a list of lines to a file
    // Each item in the list becomes one line
    private static void writeToFile(String filePath, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // go to next line
            }
        } catch (IOException e) {
            System.out.println("  [DataStore] ERROR saving to " + filePath + ": " + e.getMessage());
        }
    }

    // Read all lines from a file into a list
    private static List<String> readFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        // If the file doesn't exist yet, return an empty list (not an error)
        if (!file.exists()) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // skip blank lines
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("  [DataStore] ERROR reading " + filePath + ": " + e.getMessage());
        }

        return lines;
    }

    // User: Save and Load

    public static void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(u.toFileString());
        }
        writeToFile(USERS_FILE, lines);
        System.out.println("  [DataStore] " + users.size() + " user(s) saved.");
    }

    public static List<User> loadUsers() {
        List<String> lines = readFromFile(USERS_FILE);
        List<User> users = new ArrayList<>();

        for (String line : lines) {
            try {
                // Format: USER,id,name,email,phone,passwordHash,createdAt,address,lastLogin
                String[] parts = line.split(",");
                if (parts[0].equals("USER") && parts.length >= 9) {
                    users.add(new User(
                        Integer.parseInt(parts[1]), // id
                        parts[2],                   // name
                        parts[3],                   // email
                        parts[4],                   // phone
                        parts[5],                   // passwordHash
                        parts[6],                   // createdAt
                        parts[7],                   // address
                        parts[8]                    // lastLogin
                    ));
                }
            } catch (Exception e) {
                System.out.println("  [DataStore] Skipping bad user line: " + line);
            }
        }

        System.out.println("  [DataStore] " + users.size() + " user(s) loaded.");
        return users;
    }

    // Flight: Save and Load

    public static void saveFlights(List<Flight> flights) {
        List<String> lines = new ArrayList<>();
        for (Flight f : flights) {
            lines.add(f.toFileString());
        }
        writeToFile(FLIGHTS_FILE, lines);
        System.out.println("  [DataStore] " + flights.size() + " flight(s) saved.");
    }

    public static List<Flight> loadFlights() {
        List<String> lines = readFromFile(FLIGHTS_FILE);
        List<Flight> flights = new ArrayList<>();

        for (String line : lines) {
            try {
                // Format: FLIGHT,id,destId,type,price,capacity,depTime,arrTime,airline,flightNo,class,origin
                String[] p = line.split(",");
                if (p[0].equals("FLIGHT") && p.length >= 12) {
                    flights.add(new Flight(
                        Integer.parseInt(p[1]),  // id
                        Integer.parseInt(p[2]),  // destinationId
                        Double.parseDouble(p[4]),// price
                        Integer.parseInt(p[5]),  // capacity
                        p[6],                    // departureTime
                        p[7],                    // arrivalTime
                        p[8],                    // airline
                        p[9],                    // flightNumber
                        p[10],                   // flightClass
                        p[11]                    // origin
                    ));
                }
            } catch (Exception e) {
                System.out.println("  [DataStore] Skipping bad flight line: " + line);
            }
        }

        System.out.println("  [DataStore] " + flights.size() + " flight(s) loaded.");
        return flights;
    }

    // Booking: Save and Load

    public static void saveBookings(List<Booking> bookings) {
        List<String> lines = new ArrayList<>();
        for (Booking b : bookings) {
            lines.add(b.toFileString());
        }
        writeToFile(BOOKINGS_FILE, lines);
        System.out.println("  [DataStore] " + bookings.size() + " booking(s) saved.");
    }

    public static List<Booking> loadBookings() {
        List<String> lines = readFromFile(BOOKINGS_FILE);
        List<Booking> bookings = new ArrayList<>();

        for (String line : lines) {
            try {
                // Format: id,userId,bookingType,tourPlanId,transportId,status,createdAt
                String[] p = line.split(",");
                if (p.length >= 7) {
                    bookings.add(new Booking(
                        Integer.parseInt(p[0]), // id
                        Integer.parseInt(p[1]), // userId
                        p[2],                   // bookingType
                        Integer.parseInt(p[3]), // tourPlanId
                        Integer.parseInt(p[4]), // transportId
                        p[5],                   // status
                        p[6]                    // createdAt
                    ));
                }
            } catch (Exception e) {
                System.out.println("  [DataStore] Skipping bad booking line: " + line);
            }
        }

        System.out.println("  [DataStore] " + bookings.size() + " booking(s) loaded.");
        return bookings;
    }

    // Bill: Save and Load

    public static void saveBills(List<Bill> bills) {
        List<String> lines = new ArrayList<>();
        for (Bill b : bills) lines.add(b.toFileString());
        writeToFile(BILLS_FILE, lines);
        System.out.println("  [DataStore] " + bills.size() + " bill(s) saved.");
    }

    public static List<Bill> loadBills() {
        List<String> lines = readFromFile(BILLS_FILE);
        List<Bill> bills = new ArrayList<>();

        for (String line : lines) {
            try {
                // Format: id,bookingId,baseAmount,advanceFee,totalAmount,status
                String[] p = line.split(",");
                if (p.length >= 6) {
                    bills.add(new Bill(
                        Integer.parseInt(p[0]),    // id
                        Integer.parseInt(p[1]),    // bookingId
                        Double.parseDouble(p[2]),  // baseAmount
                        Double.parseDouble(p[4]),  // totalAmount
                        p[5]                       // status
                    ));
                }
            } catch (Exception e) {
                System.out.println("  [DataStore] Skipping bad bill line: " + line);
            }
        }

        System.out.println("  [DataStore] " + bills.size() + " bill(s) loaded.");
        return bills;
    }

    // Review: Save and Load

    public static void saveReviews(List<Review> reviews) {
        List<String> lines = new ArrayList<>();
        for (Review r : reviews) lines.add(r.toFileString());
        writeToFile(REVIEWS_FILE, lines);
        System.out.println("  [DataStore] " + reviews.size() + " review(s) saved.");
    }

    public static List<Review> loadReviews() {
        List<String> lines = readFromFile(REVIEWS_FILE);
        List<Review> reviews = new ArrayList<>();

        for (String line : lines) {
            try {
                // Format: id,userId,referenceId,reviewType,rating,comment,createdAt
                String[] p = line.split(",");
                if (p.length >= 7) {
                    reviews.add(new Review(
                        Integer.parseInt(p[0]), // id
                        Integer.parseInt(p[1]), // userId
                        Integer.parseInt(p[2]), // referenceId
                        p[3],                   // reviewType
                        Integer.parseInt(p[4]), // rating
                        p[5],                   // comment
                        p[6]                    // createdAt
                    ));
                }
            } catch (Exception e) {
                System.out.println("  [DataStore] Skipping bad review line: " + line);
            }
        }

        System.out.println("  [DataStore] " + reviews.size() + " review(s) loaded.");
        return reviews;
    }
}
