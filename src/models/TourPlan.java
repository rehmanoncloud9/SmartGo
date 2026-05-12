package models;

// A tour plan is a travel package created by an admin.
// It belongs to a destination and has a price and duration.

public class TourPlan {

    // Every tour plan has its own identification and destination link
    private int id;
    private int destinationId;
    private int adminId;
    private String title;
    private int durationDays;
    private double basePrice;
    private String status;

    // This constructor builds the tour package with its title, duration and price
    public TourPlan(int id, int destinationId, int adminId, String title,
            int durationDays, double basePrice, String status) {
        this.id = id;
        this.destinationId = destinationId;
        this.adminId = adminId;
        this.title = title;
        this.durationDays = durationDays;
        this.basePrice = basePrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        // This gives a human readable summary of the entire tour package
        return "Tour Plan ID: " + id + " | " + title + " | Duration: " + durationDays
                + " days | Price: $" + basePrice + " | Status: " + status;
    }
}
