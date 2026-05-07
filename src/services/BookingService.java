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
        // Step 1: Look up the flight details to make sure the flight exists and get its price
        Flight flight = FlightService.getFlightById(flightId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // Step 2: Create a generic booking record that connects the user to this specific flight
        Booking booking = new Booking(
                nextBookingId++, userId, "FLIGHT",
                0, flight.getId(), bookedAt, BookingStatus.CONFIRMED
        );
        // Step 3: Write this new booking into our text database immediately
        DataStore.saveBooking(booking);

        // Step 4: Calculate the small extra fee our platform charges and generate the invoice
        double platformFee = flight.getPrice() * PLATFORM_FEE_RATE;
        Bill bill = new Bill(nextBillId++, booking.getId(), flight.getPrice(), platformFee);
        // Step 5: Save the financial bill so the user can pay it later
        DataStore.saveBill(bill);

        // Step 6: Print out all the details so the main window can show them to the user
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
        // Step 1: Verify the tour package is still available
        TourPlan plan = TourPlanService.getTourPlanById(tourPlanId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // Step 2: Create the booking link for the tour
        Booking booking = new Booking(
                nextBookingId++, userId, "TOUR_PLAN",
                plan.getId(), 0, bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        // Step 3: Add the platform fee and create the bill for the package
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
        // Step 1: Find the hotel and ensure it exists in our system
        Hotel hotel = HotelService.getHotelById(hotelId);

        String bookedAt = LocalDateTime.now().format(formatter);

        // Step 2: Create the primary booking record for the hotel stay
        Booking booking = new Booking(
                nextBookingId++, userId, "HOTEL",
                0, 0, bookedAt, BookingStatus.CONFIRMED
        );
        DataStore.saveBooking(booking);

        // Step 3: Since hotels need extra details like dates we create a specialized stay record
        HotelBooking hotelBooking = new HotelBooking(
                nextHotelBookingId++, booking.getId(), hotelId,
                checkIn, checkOut, numGuests
        );
        // Step 4: Save these specific stay details separately from the main booking
        DataStore.saveHotelBooking(hotelBooking);

        // Step 5: Calculate the cost based on the hotel rate and add our platform commission
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
        // Step 1: Load all current bookings from the file to start our search
        List<Booking> bookings = DataStore.loadBookings();
        boolean found = false;

        for (Booking b : bookings) {
            // Step 2: This is critical security logic because we must check that the booking ID matches and it belongs to the logged in user
            // This prevents one user from accidentally or maliciously cancelling someone else's trip
            if (b.getId() == bookingId && b.getUserId() == userId) {
                // Step 3: We also check if the booking was already voided to avoid redundant actions
                if (b.getStatus() == BookingStatus.CANCELLED) {
                    throw new SmartGoException("This booking is already cancelled.");
                }
                // Step 4: Mark the status as cancelled in our memory list
                b.setStatus(BookingStatus.CANCELLED);
                found = true;
                break;
            }
        }

        // Step 5: If the loop finishes and we didn't find a matching record we throw an error
        if (!found) {
            throw new SmartGoException("No booking found with ID " + bookingId + " for your account.");
        }

        // Step 6: Finally we overwrite the entire file with our updated list to make the change permanent
        DataStore.saveAllBookings(bookings);
        System.out.println("Booking #" + bookingId + " has been cancelled.");
    }

    // Show all bookings for a specific user
    public static void showMyBookings(int userId) {
        // Step 1: We need to load both bookings and bills to show a complete picture to the user
        List<Booking> bookings = DataStore.loadBookings();
        List<Bill> bills = DataStore.loadBills();
        boolean found = false;

        System.out.println("\nYour Bookings:");
        System.out.println("===============");

        for (Booking b : bookings) {
            // Step 2: Only show the records that belong to the person currently using the app
            if (b.getUserId() == userId) {
                System.out.println(b);

                // Step 3: This nested loop cross references the bills file to find the matching invoice for this trip
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
        // Step 1: Load all financial and reservation records
        List<Bill> bills = DataStore.loadBills();
        List<Booking> bookings = DataStore.loadBookings();

        // Step 2: Critical logic because we verify that the user actually owns the booking they are trying to pay for
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

        // Step 3: Find the specific bill in our list and update its status
        boolean billFound = false;
        for (Bill bill : bills) {
            if (bill.getBookingId() == bookingId) {
                // Step 4: We prevent double payments by checking the current status first
                if (bill.getStatus().equals("PAID")) {
                    throw new SmartGoException("This bill has already been paid.");
                }

                // Step 5: Mark the bill as paid and create a new payment receipt record
                bill.setStatus("PAID");
                billFound = true;

                String paidAt = LocalDateTime.now().format(formatter);
                Payment payment = new Payment(
                        nextPaymentId++, bill.getId(),
                        bill.getTotalAmount(), "FULL", method, paidAt, "COMPLETED"
                );
                // Step 6: Save the receipt to the payments file
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

        // Step 7: Update the master bills file so the user won't be charged again
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
