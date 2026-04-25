package models;

// Hotel Booking: Stores the hotel-specific part of a booking
//
// The main Booking record knows you booked something.
// HotelBooking knows WHICH hotel, which dates, how many rooms.
//
// OOP: Encapsulation: hotel stay details are owned by this class alone

public class HotelBooking {

    private int    id;
    private int    bookingId; // links to the main Booking
    private int    hotelId;   // which hotel
    private String checkIn;
    private String checkOut;
    private int    guestCount;
    private String roomType;  // "SINGLE", "DOUBLE", "SUITE"

    public HotelBooking(int id, int bookingId, int hotelId,
                        String checkIn, String checkOut,
                        int guestCount, String roomType) {
        this.id         = id;
        this.bookingId  = bookingId;
        this.hotelId    = hotelId;
        this.checkIn    = checkIn;
        this.checkOut   = checkOut;
        this.guestCount = guestCount;
        this.roomType   = roomType;
    }

    public void displayInfo() {
        System.out.println("  Hotel Booking ID : " + id);
        System.out.println("  Check In         : " + checkIn);
        System.out.println("  Check Out        : " + checkOut);
        System.out.println("  Guests           : " + guestCount);
        System.out.println("  Room Type        : " + roomType);
    }

    // Getters
    public int    getId()         { return id;         }
    public int    getBookingId()  { return bookingId;  }
    public int    getHotelId()    { return hotelId;    }
    public String getCheckIn()    { return checkIn;    }
    public String getCheckOut()   { return checkOut;   }
    public int    getGuestCount() { return guestCount; }
    public String getRoomType()   { return roomType;   }

    public String toFileString() {
        return id + "," + bookingId + "," + hotelId + ","
             + checkIn + "," + checkOut + "," + guestCount + "," + roomType;
    }
}
