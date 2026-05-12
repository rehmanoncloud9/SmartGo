package models;

// A hotel belongs to a destination.
// Users can book a hotel as part of a booking.

public class Hotel implements Reviewable {

    // These private fields store all the essential information about each hotel
    private int id;
    private int destinationId;
    private String name;
    private int rating;
    private String managerContact;
    private String address;
    private double pricePerNight;

    // This constructor initializes a new hotel record for a specific destination
    public Hotel(int id, int destinationId, String name, int rating,
            String managerContact, String address, double pricePerNight) {
        this.id = id;
        this.destinationId = destinationId;
        this.name = name;
        this.rating = rating;
        this.managerContact = managerContact;
        this.address = address;
        this.pricePerNight = pricePerNight;
    }

    // This is the getter for ID
    public int getId() {
        return id;
    }

    // This is the getter for destination ID
    public int getDestinationId() {
        return destinationId;
    }

    // This is the getter for name
    public String getName() {
        return name;
    }

    // This is the getter for rating
    public int getRating() {
        return rating;
    }

    // This is the getter for manager contact
    public String getManagerContact() {
        return managerContact;
    }

    public String getAddress() {
        return address;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String getDisplayName() {
        // This gives the review service a simple name to show to the user
        return name;
    }

    @Override
    public String getReviewType() {
        // This categorizes the review so the system knows it belongs to a hotel
        return "HOTEL";
    }

    @Override
    public String toString() {
        // This provides a clean summary of the hotel for the user interface
        return "Hotel ID: " + id + " | " + name + " | Stars: " + rating
                + " | Price per night: $" + pricePerNight + " | Address: " + address;
    }
}
