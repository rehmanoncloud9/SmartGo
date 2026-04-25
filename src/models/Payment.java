package models;

// Payment: Tracks a single payment made against a Bill
//
// A Bill can have multiple Payments over time.
// (e.g. advance first, then remaining later)
//
// OOP: Single Responsibility: Payment only tracks money received.

public class Payment {

    private int    id;
    private int    billId;         // which bill this payment is for
    private int    bookingId;      // shortcut reference to booking
    private double amount;         // how much was paid in this transaction
    private String paymentType;    // "ADVANCE" or "REMAINING"
    private String method;         // "CASH", "CARD", "BANK_TRANSFER"
    private String paidAt;         // timestamp of payment
    private String status;         // "COMPLETED", "FAILED", "PENDING"

    public Payment(int id, int billId, int bookingId, double amount,
                   String paymentType, String method,
                   String paidAt, String status) {
        this.id          = id;
        this.billId      = billId;
        this.bookingId   = bookingId;
        this.amount      = amount;
        this.paymentType = paymentType;
        this.method      = method;
        this.paidAt      = paidAt;
        this.status      = status;
    }

    public void displayInfo() {
        System.out.println("  Payment ID   : " + id);
        System.out.println("  Amount       : Rs. " + amount);
        System.out.println("  Type         : " + paymentType);
        System.out.println("  Method       : " + method);
        System.out.println("  Status       : " + status);
        System.out.println("  Paid At      : " + paidAt);
    }

    // Getters
    public int    getId()          { return id;          }
    public int    getBillId()      { return billId;      }
    public int    getBookingId()   { return bookingId;   }
    public double getAmount()      { return amount;      }
    public String getPaymentType() { return paymentType; }
    public String getMethod()      { return method;      }
    public String getPaidAt()      { return paidAt;      }
    public String getStatus()      { return status;      }

    // Setter
    public void setStatus(String status) { this.status = status; }

    public String toFileString() {
        return id + "," + billId + "," + bookingId + "," + amount + ","
             + paymentType + "," + method + "," + paidAt + "," + status;
    }
}
