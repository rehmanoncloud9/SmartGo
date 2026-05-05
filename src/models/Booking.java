package models;

import enums.BookingStatus;

// A booking is created when a user books a flight or a tour plan.
// It stores who booked it, what they booked, and when.

public class Booking {

    // Every booking record keeps track of the user and exactly what service they reserved
    private int id;
    private int userId;
    private String bookingType;
    private int tourPlanId;
    private int transportId;
    private String bookedAt;
    private BookingStatus status;

    // This constructor initializes the reservation with all the necessary tracking details
    public Booking(int id, int userId, String bookingType, int tourPlanId,
                   int transportId, String bookedAt, BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.bookingType = bookingType;
        this.tourPlanId = tourPlanId;
        this.transportId = transportId;
        this.bookedAt = bookedAt;
        this.status = status;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getBookingType() { return bookingType; }
    public int getTourPlanId() { return tourPlanId; }
    public int getTransportId() { return transportId; }
    public String getBookedAt() { return bookedAt; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    @Override
    public String toString() {
        // This provides a quick summary of the reservation for the user or administrator
        return "Booking ID: " + id + " | User: " + userId + " | Type: " + bookingType
                + " | Status: " + status + " | Booked at: " + bookedAt;
    }
}
