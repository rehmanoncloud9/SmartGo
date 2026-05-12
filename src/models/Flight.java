package models;

// A flight is one type of transport.
// It extends Transport and adds airline, flight number, class, and times.

public class Flight extends Transport implements Reviewable {

    // We store specific flight details that aren't part of general transport
    private String airline;
    private String flightNumber;
    private String flightClass;
    private String departureTime;
    private String returnTime;

    // We pass general info like price and capacity to the Transport parent class
    public Flight(int id, int destinationId, double price, int capacity,
            String airline, String flightNumber, String flightClass,
            String departureTime, String returnTime) {
        super(id, destinationId, "Flight", price, capacity);
        this.airline = airline;
        this.flightNumber = flightNumber;
        this.flightClass = flightClass;
        this.departureTime = departureTime;
        this.returnTime = returnTime;
    }

    // This is the getter for airline
    public String getAirline() {
        return airline;
    }

    // This is the getter for flight number
    public String getFlightNumber() {
        return flightNumber;
    }

    // This is the getter for flight class
    public String getFlightClass() { return flightClass; }
    // This is the getter for departure time
    public String getDepartureTime() { return departureTime; }
    // This is the getter for return time
    public String getReturnTime() { return returnTime; }

    @Override
    public String getScheduleSummary() {
        // This provides the specific timing details for this flight
        return "Departs: " + departureTime + " | Returns: " + returnTime;
    }

    @Override
    public String getDisplayName() {
        // This is part of the Reviewable interface so we can identify what is being reviewed
        return airline + " " + flightNumber;
    }

    @Override
    public String getReviewType() {
        // This tells the review service that this record is a flight
        return "FLIGHT";
    }

    @Override
    public String toString() {
        // We combine the general transport info with the specific airline details
        return super.toString() + " | Airline: " + airline + " | Flight: " + flightNumber
                + " | Class: " + flightClass + " | " + getScheduleSummary();
    }
}
