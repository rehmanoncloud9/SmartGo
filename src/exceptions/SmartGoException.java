package exceptions;

// This is our own custom exception class for the SmartGo app.
// Instead of showing Java's default confusing error messages,
// we throw this with a simple human-friendly message.
// Example: throw new SmartGoException("No flight found with that ID.");

public class SmartGoException extends Exception {

    public SmartGoException(String message) {
        super(message);
    }
}
