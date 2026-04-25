package models;

// Meal Plan: A food option that belongs to a Tour Plan
//
// Example: "Full Board" = breakfast + lunch + dinner
//          "Breakfast Only" = just breakfast

public class MealPlan {

    private int    id;
    private int    tourPlanId;   // which tour plan this meal belongs to
    private String name;         // e.g. "Full Board", "Half Board"
    private String description;  // what meals are included
    private double price;        // extra cost per person

    public MealPlan(int id, int tourPlanId, String name,
                    String description, double price) {
        this.id          = id;
        this.tourPlanId  = tourPlanId;
        this.name        = name;
        this.description = description;
        this.price       = price;
    }

    public void displayInfo() {
        System.out.println("  🍽  " + name + " : Rs. " + price);
        System.out.println("     " + description);
    }

    // Getters
    public int    getId()          { return id;          }
    public int    getTourPlanId()  { return tourPlanId;  }
    public String getName()        { return name;        }
    public String getDescription() { return description; }
    public double getPrice()       { return price;       }

    // Setters
    public void setPrice(double price) { this.price = price; }

    public String toFileString() {
        return id + "," + tourPlanId + "," + name + "," + description + "," + price;
    }
}
