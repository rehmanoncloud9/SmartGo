package models;

// A payment is made to settle a bill.
// payment_type tells us if it is a full payment or an advance.
// method tells us how the user paid (cash, card, bank transfer).

public class Payment {

    // These fields record every detail of a successful financial transaction
    private int id;
    private int billId;
    private double amount;
    private String paymentType;
    private String method;
    private String paidAt;
    private String status;

    // This constructor initializes the payment record with the settled amount and
    // method used
    public Payment(int id, int billId, double amount, String paymentType, String method, String paidAt, String status) {
        this.id = id;
        this.billId = billId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.method = method;
        this.paidAt = paidAt;
        this.status = status;
    }

    // This is the getter for ID
    public int getId() {
        return id;
    }

    // This is the getter for bill ID
    public int getBillId() {
        return billId;
    }

    // This is the getter for amount
    public double getAmount() {
        return amount;
    }

    // This is the getter for payment type
    public String getPaymentType() {
        return paymentType;
    }

    // This is the getter for method
    public String getMethod() {
        return method;
    }

    // This is the getter for date and time
    public String getPaidAt() {
        return paidAt;
    }

    // This is the getter for status
    public String getStatus() {
        return status;
    }

    // This is the setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // This provides a complete transaction receipt for the system to display
        return "Payment ID: " + id + " | Bill: " + billId + " | Amount: $" + amount
                + " | Type: " + paymentType + " | Method: " + method
                + " | Paid at: " + paidAt + " | Status: " + status;
    }
}
