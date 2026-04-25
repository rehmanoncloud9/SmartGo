package models;

// Transport: Abstract base for all travel modes
//
// In real life, transport can be a Flight, Bus, or Train.
// They all share some basic info (price, capacity, destination)
// but each one has its own special details.
//
// So we make Transport ABSTRACT: you can never book just
// a "Transport." You book a Flight, a Bus, or a Train.
//
// OOP Concepts:
//   - ABSTRACTION: Transport hides the details, shows the shape
//   - INHERITANCE: Flight, Bus, Train all extend this
//   - POLYMORPHISM: getTransportDetails() behaves differently for each

public abstract class Transport {

    private int    id;
    private int    destinationId; // links to a Destination
    private String type;          // "FLIGHT", "BUS", or "TRAIN"
    private double price;         // ticket cost
    private int    capacity;      // how many seats total
    private String departureTime;
    private String arrivalTime;

    // Constructor: called by Flight, Bus, Train via super(...)
    public Transport(int id, int destinationId, String type,
                     double price, int capacity,
                     String departureTime, String arrivalTime) {
        this.id            = id;
        this.destinationId = destinationId;
        this.type          = type;
        this.price         = price;
        this.capacity      = capacity;
        this.departureTime = departureTime;
        this.arrivalTime   = arrivalTime;
    }

    // Abstract method: each transport type shows its own details
    // Flight will show: airline, class
    // Bus will show: operator, bus type
    // Train will show: operator, train number
    public abstract void displayTransportDetails();

    // Abstract method: for calculating cost with extra logic
    // Each transport type may calculate cost differently
    public abstract double calculateCost(int passengers);

    // Show the basic info shared by all transport types
    public void displayBasicInfo() {
        System.out.println("  Type      : " + type);
        System.out.println("  Price     : Rs. " + price);
        System.out.println("  Capacity  : " + capacity + " seats");
        System.out.println("  Departs   : " + departureTime);
        System.out.println("  Arrives   : " + arrivalTime);
    }

    // Getters
    public int    getId()            { return id;            }
    public int    getDestinationId() { return destinationId; }
    public String getType()          { return type;          }
    public double getPrice()         { return price;         }
    public int    getCapacity()      { return capacity;      }
    public String getDepartureTime() { return departureTime; }
    public String getArrivalTime()   { return arrivalTime;   }

    // Setters
    public void setPrice(double price)       { this.price    = price;    }
    public void setCapacity(int capacity)    { this.capacity = capacity; }

    // File string: subclasses will add their own fields
    public String toFileString() {
        return id + "," + destinationId + "," + type + ","
             + price + "," + capacity + ","
             + departureTime + "," + arrivalTime;
    }
}
