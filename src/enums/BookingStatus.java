package enums;

// The three possible states of any booking.

public enum BookingStatus {
    // This state means the booking is waiting for some action or approval
    PENDING,
    // This state means the reservation is finalized and ready for the user
    CONFIRMED,
    // This state means the user or admin has opted to void the reservation
    CANCELLED
}
