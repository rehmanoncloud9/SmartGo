package models;

// This is the base class for all types of transport.
// Flight, Bus, and Train all extend this class.
// It is abstract so you cannot create a plain Transport object.

public abstract class Transport {

    private int id;
    private int destinationId;
    private String type;
    private double price;
    private int capacity;

    public Transport(int id, int destinationId, String type, double price, int capacity) {
        this.id = id;
        this.destinationId = destinationId;
        this.type = type;
        this.price = price;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public int getDestinationId() { return destinationId; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getCapacity() { return capacity; }

    public void setPrice(double price) { this.price = price; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    // Every transport type must describe its schedule
    public abstract String getScheduleSummary();

    @Override
    public String toString() {
        return "Transport ID: " + id + " | Type: " + type + " | Price: $" + price + " | Capacity: " + capacity;
    }
}
