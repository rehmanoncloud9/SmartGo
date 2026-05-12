package models;

// A train is one type of transport.
// Extends Transport and adds operator, train number, and departure and arrival times.

public class Train extends Transport {

    private String operator;
    private String trainNumber;
    private String departureTime;
    private String arrivalTime;

    public Train(int id, int destinationId, double price, int capacity,
            String operator, String trainNumber, String departureTime, String arrivalTime) {
        super(id, destinationId, "Train", price, capacity);
        this.operator = operator;
        this.trainNumber = trainNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getOperator() {
        return operator;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String getScheduleSummary() {
        return "Departs: " + departureTime + " | Arrives: " + arrivalTime;
    }

    @Override
    public String toString() {
        return super.toString() + " | Operator: " + operator + " | Train: " + trainNumber
                + " | " + getScheduleSummary();
    }
}
