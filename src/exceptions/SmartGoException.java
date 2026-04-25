package exceptions;

// SmartGo Exception: Our own custom error class
//
// In Java, when something goes wrong we "throw" an Exception.
// Instead of using generic Java exceptions like Exception or
// RuntimeException, we make our OWN so the error messages
// are specific and human-friendly.
//
// OOP: INHERITANCE: SmartGoException extends Exception
// This means it IS an Exception, but with our own touch.
//
// Example usage:
//     throw new SmartGoException("No flights found for this destination.");

public class SmartGoException extends Exception {

    // Constructor: just pass in a human-readable message
    public SmartGoException(String message) {
        super(message); // pass the message to Java's Exception class
    }

    // Constructor with a cause: useful when wrapping another error
    // Example: a database error caused a SmartGo error
    public SmartGoException(String message, Throwable cause) {
        super(message, cause);
    }
}
