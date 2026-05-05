package data;

import models.*;
import enums.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// DataStore handles saving and loading all app data to and from text files.
// Each type of object gets its own file in the data folder.
// This is how the app remembers things between runs.

public class DataStore {

    // This is the main folder where we keep all our text database files
    private static final String DATA_FOLDER = "data/";

    // We define each file name here so we can change them easily if needed
    private static final String USERS_FILE        = DATA_FOLDER + "users.txt";
    private static final String FLIGHTS_FILE      = DATA_FOLDER + "flights.txt";
    private static final String HOTELS_FILE       = DATA_FOLDER + "hotels.txt";
    private static final String DESTINATIONS_FILE = DATA_FOLDER + "destinations.txt";
    private static final String TOURPLANS_FILE    = DATA_FOLDER + "tourplans.txt";
    private static final String MEALPLANS_FILE    = DATA_FOLDER + "mealplans.txt";
    private static final String BOOKINGS_FILE     = DATA_FOLDER + "bookings.txt";
    private static final String BILLS_FILE        = DATA_FOLDER + "bills.txt";
    private static final String PAYMENTS_FILE     = DATA_FOLDER + "payments.txt";
    private static final String REVIEWS_FILE      = DATA_FOLDER + "reviews.txt";
    private static final String HOTEL_BOOKINGS_FILE = DATA_FOLDER + "hotel_bookings.txt";

    // This method runs when the app starts to make sure the data folder actually exists
    public static void init() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    // This is a helper that adds one new line to the end of any file
    private static void appendLine(String filename, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Could not save to file: " + filename);
        }
    }

    // This reads everything from a file and gives us a list of every line it found
    private static List<String> readLines(String filename) {
        List<String> lines = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return lines;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // We ignore empty lines to prevent crashes during parsing
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read file: " + filename);
        }
        return lines;
    }

    // This wipes a file clean and writes a whole new set of data to it
    private static void writeAllLines(String filename, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not write file: " + filename);
        }
    }

    // We convert a User object into a single line of text with pipes as dividers
    public static void saveUser(User user) {
        String line = user.getId() + "|" + user.getName() + "|" + user.getEmail()
                + "|" + user.getPhone() + "|" + user.getPasswordHash()
                + "|" + user.getCreatedAt() + "|" + user.getAddress() + "|" + user.getLastLogin();
        appendLine(USERS_FILE, line);
    }

    // We read the users file and turn each line of text back into a real Java object
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        for (String line : readLines(USERS_FILE)) {
            // Split the line by the pipe symbol to get the individual fields
            String[] parts = line.split("\\|");
            if (parts.length >= 8) {
                users.add(new User(
                        Integer.parseInt(parts[0]), parts[1], parts[2],
                        parts[3], parts[4], parts[5], parts[6], parts[7]
                ));
            }
        }
        return users;
    }

    // Save a flight to the flights file
    // Format: id|destinationId|price|capacity|airline|flightNumber|class|departureTime|returnTime
    public static void saveFlight(Flight flight) {
        String line = flight.getId() + "|" + flight.getDestinationId() + "|" + flight.getPrice()
                + "|" + flight.getCapacity() + "|" + flight.getAirline()
                + "|" + flight.getFlightNumber() + "|" + flight.getFlightClass()
                + "|" + flight.getDepartureTime() + "|" + flight.getReturnTime();
        appendLine(FLIGHTS_FILE, line);
    }

    // Load all flights from the file
    public static List<Flight> loadFlights() {
        List<Flight> flights = new ArrayList<>();
        for (String line : readLines(FLIGHTS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 9) {
                flights.add(new Flight(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Double.parseDouble(parts[2]), Integer.parseInt(parts[3]),
                        parts[4], parts[5], parts[6], parts[7], parts[8]
                ));
            }
        }
        return flights;
    }

    // Save all flights back to the file (used after cancel or update)
    public static void saveAllFlights(List<Flight> flights) {
        List<String> lines = new ArrayList<>();
        for (Flight f : flights) {
            lines.add(f.getId() + "|" + f.getDestinationId() + "|" + f.getPrice()
                    + "|" + f.getCapacity() + "|" + f.getAirline()
                    + "|" + f.getFlightNumber() + "|" + f.getFlightClass()
                    + "|" + f.getDepartureTime() + "|" + f.getReturnTime());
        }
        writeAllLines(FLIGHTS_FILE, lines);
    }

    // Save a hotel to the hotels file
    // Format: id|destinationId|name|rating|managerContact|address|pricePerNight
    public static void saveHotel(Hotel hotel) {
        String line = hotel.getId() + "|" + hotel.getDestinationId() + "|" + hotel.getName()
                + "|" + hotel.getRating() + "|" + hotel.getManagerContact()
                + "|" + hotel.getAddress() + "|" + hotel.getPricePerNight();
        appendLine(HOTELS_FILE, line);
    }

    // Load all hotels from the file
    public static List<Hotel> loadHotels() {
        List<Hotel> hotels = new ArrayList<>();
        for (String line : readLines(HOTELS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                hotels.add(new Hotel(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        parts[2], Integer.parseInt(parts[3]),
                        parts[4], parts[5], Double.parseDouble(parts[6])
                ));
            }
        }
        return hotels;
    }

    // Save a destination to the destinations file
    // Format: id|city|region|country|attractions|imageUrl
    public static void saveDestination(Destination destination) {
        String line = destination.getId() + "|" + destination.getCity() + "|" + destination.getRegion()
                + "|" + destination.getCountry() + "|" + destination.getAttractions()
                + "|" + destination.getImageUrl();
        appendLine(DESTINATIONS_FILE, line);
    }

    // Load all destinations from the file
    public static List<Destination> loadDestinations() {
        List<Destination> destinations = new ArrayList<>();
        for (String line : readLines(DESTINATIONS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                destinations.add(new Destination(
                        Integer.parseInt(parts[0]), parts[1], parts[2],
                        parts[3], parts[4], parts[5]
                ));
            }
        }
        return destinations;
    }

    // Save a tour plan to the tour plans file
    // Format: id|destinationId|adminId|title|durationDays|basePrice|status
    public static void saveTourPlan(TourPlan plan) {
        String line = plan.getId() + "|" + plan.getDestinationId() + "|" + plan.getAdminId()
                + "|" + plan.getTitle() + "|" + plan.getDurationDays()
                + "|" + plan.getBasePrice() + "|" + plan.getStatus();
        appendLine(TOURPLANS_FILE, line);
    }

    // Load all tour plans from the file
    public static List<TourPlan> loadTourPlans() {
        List<TourPlan> plans = new ArrayList<>();
        for (String line : readLines(TOURPLANS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                plans.add(new TourPlan(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), parts[3],
                        Integer.parseInt(parts[4]), Double.parseDouble(parts[5]), parts[6]
                ));
            }
        }
        return plans;
    }

    // Save a meal plan to the meal plans file
    // Format: id|tourPlanId|name|description|price
    public static void saveMealPlan(MealPlan meal) {
        String line = meal.getId() + "|" + meal.getTourPlanId() + "|" + meal.getName()
                + "|" + meal.getDescription() + "|" + meal.getPrice();
        appendLine(MEALPLANS_FILE, line);
    }

    // Load all meal plans from the file
    public static List<MealPlan> loadMealPlans() {
        List<MealPlan> meals = new ArrayList<>();
        for (String line : readLines(MEALPLANS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                meals.add(new MealPlan(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        parts[2], parts[3], Double.parseDouble(parts[4])
                ));
            }
        }
        return meals;
    }

    // Save a booking to the bookings file
    // Format: id|userId|bookingType|tourPlanId|transportId|bookedAt|status
    public static void saveBooking(Booking booking) {
        String line = booking.getId() + "|" + booking.getUserId() + "|" + booking.getBookingType()
                + "|" + booking.getTourPlanId() + "|" + booking.getTransportId()
                + "|" + booking.getBookedAt() + "|" + booking.getStatus().name();
        appendLine(BOOKINGS_FILE, line);
    }

    // Load all bookings from the file
    public static List<Booking> loadBookings() {
        List<Booking> bookings = new ArrayList<>();
        for (String line : readLines(BOOKINGS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                bookings.add(new Booking(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        parts[2], Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]), parts[5],
                        BookingStatus.valueOf(parts[6])
                ));
            }
        }
        return bookings;
    }

    // Save all bookings back to file (used after cancellation)
    public static void saveAllBookings(List<Booking> bookings) {
        List<String> lines = new ArrayList<>();
        for (Booking b : bookings) {
            lines.add(b.getId() + "|" + b.getUserId() + "|" + b.getBookingType()
                    + "|" + b.getTourPlanId() + "|" + b.getTransportId()
                    + "|" + b.getBookedAt() + "|" + b.getStatus().name());
        }
        writeAllLines(BOOKINGS_FILE, lines);
    }

    // Save a bill to the bills file
    // Format: id|bookingId|baseAmount|platformFee|totalAmount|status
    public static void saveBill(Bill bill) {
        String line = bill.getId() + "|" + bill.getBookingId() + "|" + bill.getBaseAmount()
                + "|" + bill.getPlatformFee() + "|" + bill.getTotalAmount() + "|" + bill.getStatus();
        appendLine(BILLS_FILE, line);
    }

    // Load all bills from the file
    public static List<Bill> loadBills() {
        List<Bill> bills = new ArrayList<>();
        for (String line : readLines(BILLS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                Bill bill = new Bill(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Double.parseDouble(parts[2]), Double.parseDouble(parts[3])
                );
                bill.setStatus(parts[5]);
                bills.add(bill);
            }
        }
        return bills;
    }

    // Save all bills back to file (used after payment)
    public static void saveAllBills(List<Bill> bills) {
        List<String> lines = new ArrayList<>();
        for (Bill b : bills) {
            lines.add(b.getId() + "|" + b.getBookingId() + "|" + b.getBaseAmount()
                    + "|" + b.getPlatformFee() + "|" + b.getTotalAmount() + "|" + b.getStatus());
        }
        writeAllLines(BILLS_FILE, lines);
    }

    // Save a payment to the payments file
    // Format: id|billId|amount|paymentType|method|paidAt|status
    public static void savePayment(Payment payment) {
        String line = payment.getId() + "|" + payment.getBillId() + "|" + payment.getAmount()
                + "|" + payment.getPaymentType() + "|" + payment.getMethod()
                + "|" + payment.getPaidAt() + "|" + payment.getStatus();
        appendLine(PAYMENTS_FILE, line);
    }

    // Load all payments from the file
    public static List<Payment> loadPayments() {
        List<Payment> payments = new ArrayList<>();
        for (String line : readLines(PAYMENTS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                payments.add(new Payment(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Double.parseDouble(parts[2]), parts[3], parts[4], parts[5], parts[6]
                ));
            }
        }
        return payments;
    }

    // Save a review to the reviews file
    // Format: id|userId|reviewableType|reviewableId|rating|comment|createdAt
    public static void saveReview(Review review) {
        String line = review.getId() + "|" + review.getUserId() + "|" + review.getReviewableType()
                + "|" + review.getReviewableId() + "|" + review.getRating()
                + "|" + review.getComment() + "|" + review.getCreatedAt();
        appendLine(REVIEWS_FILE, line);
    }

    // Load all reviews from the file
    public static List<Review> loadReviews() {
        List<Review> reviews = new ArrayList<>();
        for (String line : readLines(REVIEWS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                reviews.add(new Review(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        parts[2], Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]), parts[5], parts[6]
                ));
            }
        }
        return reviews;
    }

    // Save a hotel booking to the hotel bookings file
    // Format: id|bookingId|hotelId|checkIn|checkOut|numGuests
    public static void saveHotelBooking(HotelBooking hb) {
        String line = hb.getId() + "|" + hb.getBookingId() + "|" + hb.getHotelId()
                + "|" + hb.getCheckIn() + "|" + hb.getCheckOut() + "|" + hb.getNumGuests();
        appendLine(HOTEL_BOOKINGS_FILE, line);
    }

    // Load all hotel bookings from the file
    public static List<HotelBooking> loadHotelBookings() {
        List<HotelBooking> bookings = new ArrayList<>();
        for (String line : readLines(HOTEL_BOOKINGS_FILE)) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6) {
                bookings.add(new HotelBooking(
                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), parts[3], parts[4],
                        Integer.parseInt(parts[5])
                ));
            }
        }
        return bookings;
    }

    // Save all hotel bookings back to file
    public static void saveAllHotelBookings(List<HotelBooking> bookings) {
        List<String> lines = new ArrayList<>();
        for (HotelBooking hb : bookings) {
            lines.add(hb.getId() + "|" + hb.getBookingId() + "|" + hb.getHotelId()
                    + "|" + hb.getCheckIn() + "|" + hb.getCheckOut() + "|" + hb.getNumGuests());
        }
        writeAllLines(HOTEL_BOOKINGS_FILE, lines);
    }
}
