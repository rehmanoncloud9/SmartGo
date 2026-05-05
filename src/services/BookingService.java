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

    // We keep track of the next available IDs to ensure every record is unique
    private static int nextBookingId = 1;
    private static int nextBillId = 1;
    private static int nextPaymentId = 1;
    private static int nextHotelBookingId = 1;

    // Every booking gets a 5 percent platform fee added to the base price
    private static final double PLATFORM_FEE_RATE = 0.05;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // We look at the last saved records to figure out where to start our ID counters
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

        List<HotelBooking> hotelBookings = DataStore.loadHotelBookings();
        if (!hotelBookings.isEmpty()) {
            nextHotelBookingId = hotelBookings.get(hotelBookings.size() - 1).getId() + 1;
        }
    }

    // This handles the entire process of reserving a flight and generating the bill
    public static Booking bookFlight(int userId, int flightId) throws SmartGoException {
        // Find the flight details first
        Flight flight = FlightService.getFlightById(flightId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // Create the actual booking record and save it to the text file
        Booking booking = new Booking(
                nextBookingId++, userId, "FLIGHT",
                0, flight.getId(), bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        // Calculate the platform fee and create the invoice for this flight
        double platformFee = flight.getPrice() * PLATFORM_FEE_RATE;
        Bill bill = new Bill(nextBillId++, booking.getId(), flight.getPrice(), platformFee);
        DataStore.saveBill(bill);

        // Print the confirmation details so the UI can capture and show them
        System.out.println("Flight booked successfully!");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Flight: " + flight.getAirline() + " " + flight.getFlightNumber());
        System.out.println("Base Price: $" + flight.getPrice());
        System.out.println("Platform Fee (5%): $" + String.format("%.2f", platformFee));
        System.out.println("Total Bill: $" + String.format("%.2f", bill.getTotalAmount()));

        return booking;
    }

    // Similar to flights but for tour packages
    public static Booking bookTourPlan(int userId, int tourPlanId) throws SmartGoException {
        TourPlan plan = TourPlanService.getTourPlanById(tourPlanId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // Record the tour booking in the database
        Booking booking = new Booking(
                nextBookingId++, userId, "TOUR_PLAN",
                plan.getId(), 0, bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        // Generate the bill with the standard 5 percent commission
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

    // Book a hotel for the currently logged in user
    public static Booking bookHotel(int userId, int hotelId, String checkIn, String checkOut, int numGuests) throws SmartGoException {
        Hotel hotel = HotelService.getHotelById(hotelId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // 1. Create main booking record
        Booking booking = new Booking(
                nextBookingId++, userId, "HOTEL",
                0, 0, bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        // 2. Create specific hotel booking details
        HotelBooking hotelBooking = new HotelBooking(
                nextHotelBookingId++, booking.getId(), hotelId,
                checkIn, checkOut, numGuests
        );
        DataStore.saveHotelBooking(hotelBooking);

        // 3. Create bill (assuming 1 night for simplicity, or we could parse dates)
        // For now let's just use the hotel price per night
        double platformFee = hotel.getPricePerNight() * PLATFORM_FEE_RATE;
        Bill bill = new Bill(nextBillId++, booking.getId(), hotel.getPricePerNight(), platformFee);
        DataStore.saveBill(bill);

        System.out.println("Hotel booked successfully!");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Hotel: " + hotel.getName());
        System.out.println("Dates: " + checkIn + " to " + checkOut);
        System.out.println("Price: $" + hotel.getPricePerNight());
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
