package models;

// Bus: Extends Transport
//
// Not active in the MVP but the class exists to show the
// inheritance structure (required for OOP marks).
// Think of it as "we designed it, we just haven't turned it on yet."

public class Bus extends Transport {

    private String operator; // e.g. "Daewoo", "Faisal Movers"
    private String busType;  // "AC", "Non-AC", "Sleeper"

    // Constructor
    public Bus(int id, int destinationId, double price, int capacity,
               String departureTime, String arrivalTime,
               String operator, String busType) {

        super(id, destinationId, "BUS", price, capacity, departureTime, arrivalTime);

        this.operator = operator;
        this.busType  = busType;
    }

    // Polymorphism: Bus's version of displaying details
    @Override
    public void displayTransportDetails() {
        System.out.println("  🚌  BUS DETAILS");
        System.out.println("------------------------------");
        displayBasicInfo();
        System.out.println("  Operator  : " + operator);
        System.out.println("  Bus Type  : " + busType);
        System.out.println("------------------------------");
    }

    // Bus cost is simply price x passengers
    @Override
    public double calculateCost(int passengers) {
        return getPrice() * passengers;
    }

    // Getters
    public String getOperator() { return operator; }
    public String getBusType()  { return busType;  }

    @Override
    public String toFileString() {
        return "BUS," + super.toFileString() + "," + operator + "," + busType;
    }
}
