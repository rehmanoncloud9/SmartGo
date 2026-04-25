package models;

// A payment is made to settle a bill.
// payment_type tells us if it is a full payment or an advance.
// method tells us how the user paid (cash, card, bank transfer).

public class Payment {

    private int id;
    private int billId;
    private double amount;
    private String paymentType;
    private String method;
    private String paidAt;
    private String status;

    public Payment(int id, int billId, double amount, String paymentType, String method, String paidAt, String status) {
        this.id = id;
        this.billId = billId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.method = method;
        this.paidAt = paidAt;
        this.status = status;
    }

    public int getId() { return id; }
    public int getBillId() { return billId; }
    public double getAmount() { return amount; }
    public String getPaymentType() { return paymentType; }
    public String getMethod() { return method; }
    public String getPaidAt() { return paidAt; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Payment ID: " + id + " | Bill: " + billId + " | Amount: $" + amount
                + " | Type: " + paymentType + " | Method: " + method
                + " | Paid at: " + paidAt + " | Status: " + status;
    }
}
