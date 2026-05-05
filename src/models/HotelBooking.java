package models;

// A hotel booking stores the extra details when a user picks a hotel.
// It is always linked to a main Booking record.

public class HotelBooking {

    // These fields store the specific details for a guest's stay at a hotel
    private int id;
    private int bookingId;
    private int hotelId;
    private String checkIn;
    private String checkOut;
    private int numGuests;

    // This constructor links the stay details to a general booking record in our system
    public HotelBooking(int id, int bookingId, int hotelId, String checkIn, String checkOut, int numGuests) {
        this.id = id;
        this.bookingId = bookingId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numGuests = numGuests;
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getHotelId() { return hotelId; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public int getNumGuests() { return numGuests; }

    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }

    @Override
    public String toString() {
        // This provides a helpful textual summary of the hotel reservation details
        return "Hotel Booking ID: " + id + " | Booking: " + bookingId + " | Hotel: " + hotelId
                + " | Check-in: " + checkIn + " | Check-out: " + checkOut + " | Guests: " + numGuests;
    }
}
