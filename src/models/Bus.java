package models;

// A bus is one type of transport.
// Extends Transport and adds operator, bus type, and departure and arrival times.

public class Bus extends Transport {

    private String operator;
    private String busType;
    private String departureTime;
    private String arrivalTime;

    public Bus(int id, int destinationId, double price, int capacity,
               String operator, String busType, String departureTime, String arrivalTime) {
        super(id, destinationId, "Bus", price, capacity);
        this.operator = operator;
        this.busType = busType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getOperator() { return operator; }
    public String getBusType() { return busType; }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime() { return arrivalTime; }

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
