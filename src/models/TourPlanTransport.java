package models;

// This links a tour plan to a transport option.
// One tour plan can have many transport options.

public class TourPlanTransport {

    private int id;
    private int tourPlanId;
    private int transportId;

    public TourPlanTransport(int id, int tourPlanId, int transportId) {
        this.id = id;
        this.tourPlanId = tourPlanId;
        this.transportId = transportId;
    }

    public int getId() {
        return id;
    }

    public int getTourPlanId() {
        return tourPlanId;
    }

    public int getTransportId() {
        return transportId;
    }

    @Override
    public String toString() {
        return "TourPlanTransport ID: " + id + " | Tour Plan: " + tourPlanId + " | Transport: " + transportId;
    }
}
