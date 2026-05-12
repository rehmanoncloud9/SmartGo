package models;

// This links a tour plan to a hotel.
// isDefault means this is the main recommended hotel for that tour plan.

public class TourPlanHotel {

    private int id;
    private int tourPlanId;
    private int hotelId;
    private boolean isDefault;

    public TourPlanHotel(int id, int tourPlanId, int hotelId, boolean isDefault) {
        this.id = id;
        this.tourPlanId = tourPlanId;
        this.hotelId = hotelId;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public int getTourPlanId() {
        return tourPlanId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return "TourPlanHotel ID: " + id + " | Tour Plan: " + tourPlanId
                + " | Hotel: " + hotelId + " | Default: " + isDefault;
    }
}
