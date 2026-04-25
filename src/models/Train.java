package models;

// Train: Extends Transport
//
// Also not active in MVP but class exists for the OOP structure.

public class Train extends Transport {

    private String operator;    // e.g. "Pakistan Railways"
    private String trainNumber; // e.g. "14-UP"

    // Constructor
    public Train(int id, int destinationId, double price, int capacity,
                 String departureTime, String arrivalTime,
                 String operator, String trainNumber) {

        super(id, destinationId, "TRAIN", price, capacity, departureTime, arrivalTime);

        this.operator    = operator;
        this.trainNumber = trainNumber;
    }

    // Polymorphism: Train's version of displaying details
    @Override
    public void displayTransportDetails() {
        System.out.println("  🚂  TRAIN DETAILS");
        System.out.println("------------------------------");
        displayBasicInfo();
        System.out.println("  Operator  : " + operator);
        System.out.println("  Train No  : " + trainNumber);
        System.out.println("------------------------------");
    }

    // Train cost = price x passengers (simple)
    @Override
    public double calculateCost(int passengers) {
        return getPrice() * passengers;
    }

    // Getters
    public String getOperator()    { return operator;    }
    public String getTrainNumber() { return trainNumber; }

    @Override
    public String toFileString() {
        return "TRAIN," + super.toFileString() + "," + operator + "," + trainNumber;
    }
}
