package enums;

// Booking Status: The possible states of a booking
//
// An enum is like a list of allowed values.
// Instead of writing "PENDING" as a random string everywhere
// (which is risky: a typo like "PENDIG" would break things),
// we define them here once and use BookingStatus.PENDING safely.

public enum BookingStatus {
    PENDING,    // just created, not confirmed yet
    CONFIRMED,  // payment received, booking locked in
    CANCELLED   // user or admin cancelled it
}
