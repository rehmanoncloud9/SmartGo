package models;

// A bus is one type of transport.
// Extends Transport and adds operator, bus type, and departure and arrival times.

public class Bus extends Transport {

    private String operator;
    private String busType;
    private String departureTime;
    private String arrivalTime;

    // This is the main constructor for Bus
    public Bus(int id, int destinationId, double price, int capacity,
            String operator, String busType, String departureTime, String arrivalTime) {
        super(id, destinationId, "Bus", price, capacity);
        this.operator = operator;
        this.busType = busType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    // This is the getter for operator
    public String getOperator() {
        return operator;
    }

    // This is the getter for bus type
    public String getBusType() {
        return busType;
    }

    // This is the getter for departure time
    public String getDepartureTime() {
        return departureTime;
    }

    // This is the getter for arrival time
    public String getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String getScheduleSummary() {
        return "Departs: " + departureTime + " | Arrives: " + arrivalTime;
    }

    @Override
    public String toString() {
        return super.toString() + " | Operator: " + operator + " | Bus Type: " + busType
                + " | " + getScheduleSummary();
    }
}
