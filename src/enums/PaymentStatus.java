package enums;

// Payment Status: Has the payment gone through?

public enum PaymentStatus {
    PENDING,    // payment initiated but not done yet
    COMPLETED,  // money received successfully
    FAILED      // something went wrong
}
