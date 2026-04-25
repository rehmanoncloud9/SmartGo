package models;

// Flight: A specific type of Transport (the only active one in MVP)
//
// OOP Concepts:
//   - INHERITANCE: Flight extends Transport
//   - POLYMORPHISM: overrides displayTransportDetails() and calculateCost()

public class Flight extends Transport {

    // Flight-specific fields
    private String airline;      // e.g. "PIA", "AirBlue"
    private String flightNumber; // e.g. "PK-302"
    private String flightClass;  // "ECONOMY", "BUSINESS", "FIRST"
    private String origin;       // where it takes off from

    // Constructor
    public Flight(int id, int destinationId, double price, int capacity,
                  String departureTime, String arrivalTime,
                  String airline, String flightNumber,
                  String flightClass, String origin) {

        // Pass shared stuff to Transport
        super(id, destinationId, "FLIGHT", price, capacity, departureTime, arrivalTime);

        // Flight-only stuff
        this.airline      = airline;
        this.flightNumber = flightNumber;
        this.flightClass  = flightClass;
        this.origin       = origin;
    }

    // Polymorphism: Flight's version of this method
    @Override
    public void displayTransportDetails() {
        System.out.println("  ✈  FLIGHT DETAILS");
        System.out.println("------------------------------");
        displayBasicInfo(); // show shared info from Transport
        System.out.println("  Airline   : " + airline);
        System.out.println("  Flight No : " + flightNumber);
        System.out.println("  Class     : " + flightClass);
        System.out.println("  From      : " + origin);
        System.out.println("------------------------------");
    }

    // Cost = ticket price x number of passengers
    // Business class gets a 1.5x multiplier
    @Override
    public double calculateCost(int passengers) {
        double base = getPrice() * passengers;
        if (flightClass.equalsIgnoreCase("BUSINESS")) {
            base = base * 1.5;
        } else if (flightClass.equalsIgnoreCase("FIRST")) {
            base = base * 2.0;
        }
        return base;
    }

    // Getters
    public String getAirline()      { return airline;      }
    public String getFlightNumber() { return flightNumber; }
    public String getFlightClass()  { return flightClass;  }
    public String getOrigin()       { return origin;       }

    // Save to file
    @Override
    public String toFileString() {
        return "FLIGHT," + super.toFileString() + ","
             + airline + "," + flightNumber + ","
             + flightClass + "," + origin;
    }
}
