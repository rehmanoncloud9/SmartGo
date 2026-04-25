package models;

// A flight is one type of transport.
// It extends Transport and adds airline, flight number, class, and times.

public class Flight extends Transport {

    private String airline;
    private String flightNumber;
    private String flightClass;
    private String departureTime;
    private String returnTime;

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

    public String getAirline() { return airline; }
    public String getFlightNumber() { return flightNumber; }
    public String getFlightClass() { return flightClass; }
    public String getDepartureTime() { return departureTime; }
    public String getReturnTime() { return returnTime; }

    @Override
    public String getScheduleSummary() {
        return "Departs: " + departureTime + " | Returns: " + returnTime;
    }

    @Override
    public String toString() {
        return super.toString() + " | Airline: " + airline + " | Flight: " + flightNumber
                + " | Class: " + flightClass + " | " + getScheduleSummary();
    }
}
