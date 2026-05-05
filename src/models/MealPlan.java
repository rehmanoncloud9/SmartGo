package models;

// A meal plan is an optional food package linked to a tour plan.
// Users can add a meal plan to their booking.

public class MealPlan {

    // These fields store the details for the optional dining packages
    private int id;
    private int tourPlanId;
    private String name;
    private String description;
    private double price;

    // This constructor links the meal plan to a specific tour and sets its price
    public MealPlan(int id, int tourPlanId, String name, String description, double price) {
        this.id = id;
        this.tourPlanId = tourPlanId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId() { return id; }
    public int getTourPlanId() { return tourPlanId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        // This provides a helpful summary of the meal option for the user
        return "Meal Plan ID: " + id + " | " + name + " | Price: $" + price + " | " + description;
    }
}
