package models;

// A bill is automatically created when a booking is made.
// It holds the base amount, a platform fee, and the total.
// The status tells us if it has been paid or not.

public class Bill {

    // These fields hold all the financial details for an invoice
    private int id;
    private int bookingId;
    private double baseAmount;
    private double platformFee;
    private double totalAmount;
    private String status;

    // This constructor calculates the final total by adding the platform fee to the base price
    public Bill(int id, int bookingId, double baseAmount, double platformFee) {
        this.id = id;
        this.bookingId = bookingId;
        this.baseAmount = baseAmount;
        this.platformFee = platformFee;
        this.totalAmount = baseAmount + platformFee;
        // Every new bill starts as unpaid by default until the user makes a payment
        this.status = "UNPAID";
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public double getBaseAmount() { return baseAmount; }
    public double getPlatformFee() { return platformFee; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        // This provides a clear summary of the charges for the user to review
        return "Bill ID: " + id + " | Booking: " + bookingId
                + " | Base: $" + baseAmount + " | Platform Fee: $" + platformFee
                + " | Total: $" + totalAmount + " | Status: " + status;
    }
}
