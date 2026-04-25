package models;

// Bill: The financial record for a booking
//
// Every booking generates exactly one bill.
// The bill knows the total, how much advance was paid (40%),
// and what's still remaining.
//
// OOP: Single Responsibility: Bill only cares about money.
//      Booking handles the trip. Bill handles the finances.

public class Bill {

    private int    id;
    private int    bookingId;      // which booking this bill is for
    private double baseAmount;     // raw cost before anything
    private double advanceFee;     // 40% paid upfront
    private double totalAmount;    // final total
    private String status;         // "UNPAID", "PARTIAL", "PAID"

    // Constructor
    // We auto-calculate the advance fee (40%) here
    public Bill(int id, int bookingId, double baseAmount, double totalAmount, String status) {
        this.id          = id;
        this.bookingId   = bookingId;
        this.baseAmount  = baseAmount;
        this.totalAmount = totalAmount;
        this.advanceFee  = totalAmount * 0.40; // 40% advance as per PRD
        this.status      = status;
    }

    // Show the full bill like a receipt
    public void displayBill() {
        System.out.println("========================================");
        System.out.println("           SmartGo: BILL RECEIPT       ");
        System.out.println("========================================");
        System.out.println("  Bill ID       : " + id);
        System.out.println("  Booking ID    : " + bookingId);
        System.out.println("  Base Amount   : Rs. " + baseAmount);
        System.out.println("  Total Amount  : Rs. " + totalAmount);
        System.out.println("  Advance (40%) : Rs. " + advanceFee);
        System.out.println("  Remaining     : Rs. " + (totalAmount - advanceFee));
        System.out.println("  Status        : " + status);
        System.out.println("========================================");
    }

    // How much is still left to pay
    public double getRemainingAmount() {
        return totalAmount - advanceFee;
    }

    // Getters
    public int    getId()          { return id;          }
    public int    getBookingId()   { return bookingId;   }
    public double getBaseAmount()  { return baseAmount;  }
    public double getAdvanceFee()  { return advanceFee;  }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus()      { return status;      }

    // Setters
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return id + "," + bookingId + "," + baseAmount + ","
             + advanceFee + "," + totalAmount + "," + status;
    }
}
