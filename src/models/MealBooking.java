package models;

// A meal booking links a booking to the meal plan the user chose.
// isCancelled lets us cancel the meal without cancelling the whole booking.

public class MealBooking {

    private int id;
    private int bookingId;
    private int mealPlanId;
    private boolean isCancelled;

    public MealBooking(int id, int bookingId, int mealPlanId, boolean isCancelled) {
        this.id = id;
        this.bookingId = bookingId;
        this.mealPlanId = mealPlanId;
        this.isCancelled = isCancelled;
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getMealPlanId() { return mealPlanId; }
    public boolean isCancelled() { return isCancelled; }

    public void setCancelled(boolean cancelled) { this.isCancelled = cancelled; }

    @Override
    public String toString() {
        return "Meal Booking ID: " + id + " | Booking: " + bookingId
                + " | Meal Plan: " + mealPlanId + " | Cancelled: " + isCancelled;
    }
}
