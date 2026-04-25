package services;

import models.*;
import exceptions.SmartGoException;
import data.DataStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Booking Service: The heart of SmartGo
//
// This handles:
//   - Making a new booking (standalone flight)
//   - Viewing a user's bookings
//   - Cancelling a booking
//   - Generating a Bill after booking
//   - Processing payment

public class BookingService {

    private List<Booking> bookings;
    private List<Bill>    bills;
    private List<Payment> payments;

    // Constructor: loads everything from files
    public BookingService() {
        this.bookings = DataStore.loadBookings();
        this.bills    = DataStore.loadBills();
        this.payments = new ArrayList<>();
    }

    // Book a Flight: Standalone booking (no tour plan)
    public Booking bookFlight(User user, Flight flight, int passengers) throws SmartGoException {

        // --- Validation ---
        if (passengers <= 0) {
            throw new SmartGoException("Number of passengers must be at least 1.");
        }
        if (passengers > flight.getCapacity()) {
            throw new SmartGoException("Not enough seats. Only "
                    + flight.getCapacity() + " seats available.");
        }

        // Create the booking
        int newBookingId = bookings.size() + 1;
        String now = getCurrentTime();

        Booking booking = new Booking(
            newBookingId,
            user.getId(),
            "STANDALONE",   // not a tour plan
            0,              // no tour plan involved
            flight.getId(), // the flight they're booking
            "CONFIRMED",    // auto-confirm for now
            now
        );

        bookings.add(booking);
        DataStore.saveBookings(bookings);

        System.out.println("\n  ✅ Booking confirmed!");
        booking.displayInfo();

        // --- Auto-generate the Bill ---
        generateBill(booking, flight, passengers);

        return booking;
    }

    // Cancel a Booking
    public void cancelBooking(int bookingId, User user) throws SmartGoException {

        Booking booking = getBookingById(bookingId);

        // Make sure this booking belongs to this user
        if (booking.getUserId() != user.getId()) {
            throw new SmartGoException("You can only cancel your own bookings.");
        }

        // Can't cancel what's already cancelled
        if (booking.getStatus().equals("CANCELLED")) {
            throw new SmartGoException("This booking is already cancelled.");
        }

        booking.setStatus("CANCELLED");
        DataStore.saveBookings(bookings);

        System.out.println("\n  ✅ Booking #" + bookingId + " has been cancelled.");
        System.out.println("  Note: Advance payment is non-refundable as per policy.");
    }

    // View all bookings for a user
    public List<Booking> getUserBookings(User user) throws SmartGoException {
        List<Booking> userBookings = new ArrayList<>();

        for (Booking b : bookings) {
            if (b.getUserId() == user.getId()) {
                userBookings.add(b);
            }
        }

        if (userBookings.isEmpty()) {
            throw new SmartGoException("You have no bookings yet. Start planning your trip!");
        }

        return userBookings;
    }

    // Display all bookings for a user
    public void displayUserBookings(User user) {
        try {
            List<Booking> userBookings = getUserBookings(user);
            System.out.println("\n  ========== YOUR BOOKINGS ==========");
            int i = 1;
            for (Booking b : userBookings) {
                System.out.println("\n  Booking #" + i);
                b.displayInfo();
                i++;
            }
        } catch (SmartGoException e) {
            System.out.println("\n  " + e.getMessage());
        }
    }

    // Process Payment: Pay the advance (40%)
    public void processAdvancePayment(int bookingId, String method, User user)
            throws SmartGoException {

        // Find the bill for this booking
        Bill bill = getBillByBookingId(bookingId);

        if (bill.getStatus().equals("PAID")) {
            throw new SmartGoException("This bill is already fully paid.");
        }

        // Create the payment record
        int newPaymentId = payments.size() + 1;
        Payment payment = new Payment(
            newPaymentId,
            bill.getId(),
            bookingId,
            bill.getAdvanceFee(),   // paying 40% advance
            "ADVANCE",
            method,
            getCurrentTime(),
            "COMPLETED"
        );

        payments.add(payment);
        bill.setStatus("PARTIAL"); // advance paid, remaining pending
        DataStore.saveBills(bills);

        System.out.println("\n  ✅ Advance payment of Rs. "
                + bill.getAdvanceFee() + " received!");
        System.out.println("  Remaining balance : Rs. " + bill.getRemainingAmount());
        payment.displayInfo();
    }

    // View Bill for a booking
    public void displayBill(int bookingId) throws SmartGoException {
        Bill bill = getBillByBookingId(bookingId);
        bill.displayBill();
    }

    // Private Helpers

    // Generate a bill automatically after a booking is made
    private void generateBill(Booking booking, Flight flight, int passengers) {
        double totalCost = flight.calculateCost(passengers); // polymorphism!
        int newBillId = bills.size() + 1;

        Bill bill = new Bill(
            newBillId,
            booking.getId(),
            totalCost,  // base = total here (no extra fees)
            totalCost,  // total
            "UNPAID"
        );

        bills.add(bill);
        DataStore.saveBills(bills);

        System.out.println("\n  📄 Bill Generated:");
        bill.displayBill();
    }

    // Find a booking by its ID
    private Booking getBookingById(int bookingId) throws SmartGoException {
        for (Booking b : bookings) {
            if (b.getId() == bookingId) return b;
        }
        throw new SmartGoException("Booking #" + bookingId + " not found.");
    }

    // Find a bill by booking ID
    private Bill getBillByBookingId(int bookingId) throws SmartGoException {
        for (Bill b : bills) {
            if (b.getBookingId() == bookingId) return b;
        }
        throw new SmartGoException("No bill found for Booking #" + bookingId);
    }

    // Current timestamp
    private String getCurrentTime() {
        return LocalDateTime.now()
               .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
