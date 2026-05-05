package models;

// A hotel belongs to a destination.
// Users can book a hotel as part of a booking.

public class Hotel implements Reviewable {

    private int id;
    private int destinationId;
    private String name;
    private int rating;
    private String managerContact;
    private String address;
    private double pricePerNight;

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

    public int getId() { return id; }
    public int getDestinationId() { return destinationId; }
    public String getName() { return name; }
    public int getRating() { return rating; }
    public String getManagerContact() { return managerContact; }
    public String getAddress() { return address; }
    public double getPricePerNight() { return pricePerNight; }

    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String getReviewType() {
        return "HOTEL";
    }

    @Override
    public String toString() {
        return "Hotel ID: " + id + " | " + name + " | Stars: " + rating
                + " | Price per night: $" + pricePerNight + " | Address: " + address;
    }
}
