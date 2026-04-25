package models;

// Booking: The central record when someone books something
//
// Every booking links a User to what they booked.
// It can be a standalone flight, a hotel, or a full tour plan.
//
// OOP: plain model with encapsulation

public class Booking {

    private int    id;
    private int    userId;
    private String bookingType;   // "STANDALONE" or "TOUR_PLAN"
    private int    tourPlanId;    // 0 if standalone
    private int    transportId;   // the flight/bus/train booked
    private String status;        // "PENDING", "CONFIRMED", "CANCELLED"
    private String createdAt;

    public Booking(int id, int userId, String bookingType,
                   int tourPlanId, int transportId,
                   String status, String createdAt) {
        this.id           = id;
        this.userId       = userId;
        this.bookingType  = bookingType;
        this.tourPlanId   = tourPlanId;
        this.transportId  = transportId;
        this.status       = status;
        this.createdAt    = createdAt;
    }

    public void displayInfo() {
        System.out.println("  Booking ID   : " + id);
        System.out.println("  Type         : " + bookingType);
        System.out.println("  Status       : " + status);
        System.out.println("  Booked On    : " + createdAt);
    }

    // Getters
    public int    getId()           { return id;           }
    public int    getUserId()       { return userId;       }
    public String getBookingType()  { return bookingType;  }
    public int    getTourPlanId()   { return tourPlanId;   }
    public int    getTransportId()  { return transportId;  }
    public String getStatus()       { return status;       }
    public String getCreatedAt()    { return createdAt;    }

    // Setters
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return id + "," + userId + "," + bookingType + ","
             + tourPlanId + "," + transportId + "," + status + "," + createdAt;
    }
}
