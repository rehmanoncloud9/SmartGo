package services;

import data.DataStore;
import enums.BookingStatus;
import exceptions.SmartGoException;
import models.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// BookingService handles everything related to bookings.
// Users can book a flight, book a tour plan, cancel a booking,
// view their bookings, and make payments on their bills.

public class BookingService {

    private static int nextBookingId = 1;
    private static int nextBillId = 1;
    private static int nextPaymentId = 1;

    private static final double PLATFORM_FEE_RATE = 0.05;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Set up IDs based on existing saved data
    public static void init() {
        List<Booking> bookings = DataStore.loadBookings();
        if (!bookings.isEmpty()) {
            nextBookingId = bookings.get(bookings.size() - 1).getId() + 1;
        }

        List<Bill> bills = DataStore.loadBills();
        if (!bills.isEmpty()) {
            nextBillId = bills.get(bills.size() - 1).getId() + 1;
        }

        List<Payment> payments = DataStore.loadPayments();
        if (!payments.isEmpty()) {
            nextPaymentId = payments.get(payments.size() - 1).getId() + 1;
        }
    }

    // Book a flight for the currently logged in user
    public static Booking bookFlight(int userId, int flightId) throws SmartGoException {
        Flight flight = FlightService.getFlightById(flightId);

        String bookedAt = LocalDateTime.now().format(formatter);

        Booking booking = new Booking(
                nextBookingId++, userId, "FLIGHT",
                0, flight.getId(), bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        double platformFee = flight.getPrice() * PLATFORM_FEE_RATE;
        Bill bill = new Bill(nextBillId++, booking.getId(), flight.getPrice(), platformFee);
        DataStore.saveBill(bill);

        System.out.println("Flight booked successfully!");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Flight: " + flight.getAirline() + " " + flight.getFlightNumber());
        System.out.println("Base Price: $" + flight.getPrice());
        System.out.println("Platform Fee (5%): $" + String.format("%.2f", platformFee));
        System.out.println("Total Bill: $" + String.format("%.2f", bill.getTotalAmount()));

        return booking;
    }

    // Book a tour plan for the currently logged in user
    public static Booking bookTourPlan(int userId, int tourPlanId) throws SmartGoException {
        TourPlan plan = TourPlanService.getTourPlanById(tourPlanId);

        String bookedAt = LocalDateTime.now().format(formatter);

        Booking booking = new Booking(
                nextBookingId++, userId, "TOUR_PLAN",
                plan.getId(), 0, bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        double platformFee = plan.getBasePrice() * PLATFORM_FEE_RATE;
        Bill bill = new Bill(nextBillId++, booking.getId(), plan.getBasePrice(), platformFee);
        DataStore.saveBill(bill);

        System.out.println("Tour plan booked successfully!");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Tour: " + plan.getTitle());
        System.out.println("Duration: " + plan.getDurationDays() + " days");
        System.out.println("Base Price: $" + plan.getBasePrice());
        System.out.println("Platform Fee (5%): $" + String.format("%.2f", platformFee));
        System.out.println("Total Bill: $" + String.format("%.2f", bill.getTotalAmount()));

        return booking;
    }

    // Cancel a booking by ID
    public static void cancelBooking(int bookingId, int userId) throws SmartGoException {
        List<Booking> bookings = DataStore.loadBookings();
        boolean found = false;

        for (Booking b : bookings) {
            if (b.getId() == bookingId && b.getUserId() == userId) {
                if (b.getStatus() == BookingStatus.CANCELLED) {
                    throw new SmartGoException("This booking is already cancelled.");
                }
                b.setStatus(BookingStatus.CANCELLED);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new SmartGoException("No booking found with ID " + bookingId + " for your account.");
        }

        DataStore.saveAllBookings(bookings);
        System.out.println("Booking #" + bookingId + " has been cancelled.");
    }

    // Show all bookings for a specific user
    public static void showMyBookings(int userId) {
        List<Booking> bookings = DataStore.loadBookings();
        List<Bill> bills = DataStore.loadBills();
        boolean found = false;

        System.out.println("\nYour Bookings:");
        System.out.println("===============");

        for (Booking b : bookings) {
            if (b.getUserId() == userId) {
                System.out.println(b);

                // Also show the bill for this booking
                for (Bill bill : bills) {
                    if (bill.getBookingId() == b.getId()) {
                        System.out.println("  Bill: Total $" + String.format("%.2f", bill.getTotalAmount())
                                + " | Status: " + bill.getStatus());
                    }
                }
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("You have no bookings yet.");
        }
    }

    // Pay a bill for a booking
    public static void payBill(int bookingId, int userId, String method) throws SmartGoException {
        List<Bill> bills = DataStore.loadBills();
        List<Booking> bookings = DataStore.loadBookings();

        // Make sure the booking belongs to this user
        boolean userOwnsBooking = false;
        for (Booking b : bookings) {
            if (b.getId() == bookingId && b.getUserId() == userId) {
                userOwnsBooking = true;
                break;
            }
        }

        if (!userOwnsBooking) {
            throw new SmartGoException("No booking found with ID " + bookingId + " for your account.");
        }

        // Find the bill and mark it as paid
        boolean billFound = false;
        for (Bill bill : bills) {
            if (bill.getBookingId() == bookingId) {
                if (bill.getStatus().equals("PAID")) {
                    throw new SmartGoException("This bill has already been paid.");
                }

                bill.setStatus("PAID");
                billFound = true;

                String paidAt = LocalDateTime.now().format(formatter);
                Payment payment = new Payment(
                        nextPaymentId++, bill.getId(),
                        bill.getTotalAmount(), "FULL", method, paidAt, "COMPLETED"
                );
                DataStore.savePayment(payment);

                System.out.println("Payment successful!");
                System.out.println("Amount paid: $" + String.format("%.2f", bill.getTotalAmount()));
                System.out.println("Method: " + method);
                System.out.println("Paid at: " + paidAt);
                break;
            }
        }

        if (!billFound) {
            throw new SmartGoException("No bill found for booking ID " + bookingId + ".");
        }

        DataStore.saveAllBills(bills);
    }

    // Get the bill for a specific booking
    public static Bill getBillForBooking(int bookingId) throws SmartGoException {
        for (Bill b : DataStore.loadBills()) {
            if (b.getBookingId() == bookingId) return b;
        }
        throw new SmartGoException("No bill found for booking ID " + bookingId + ".");
    }
}
