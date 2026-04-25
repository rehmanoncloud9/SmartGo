package models;

// Meal Booking: Stores which meal plan was picked in a booking
//
// When a user picks a Tour Plan, they also pick a Meal Plan.
// This class records that choice.

public class MealBooking {

    private int id;
    private int bookingId;  // links to the main Booking
    private int mealPlanId; // which meal plan they chose

    public MealBooking(int id, int bookingId, int mealPlanId) {
        this.id         = id;
        this.bookingId  = bookingId;
        this.mealPlanId = mealPlanId;
    }

    public void displayInfo() {
        System.out.println("  Meal Booking ID : " + id);
        System.out.println("  Booking ID      : " + bookingId);
        System.out.println("  Meal Plan ID    : " + mealPlanId);
    }

    // Getters
    public int getId()         { return id;         }
    public int getBookingId()  { return bookingId;  }
    public int getMealPlanId() { return mealPlanId; }

    public String toFileString() {
        return id + "," + bookingId + "," + mealPlanId;
    }
}
