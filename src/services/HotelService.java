package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.Hotel;

import java.util.ArrayList;
import java.util.List;

// HotelService handles everything related to hotels.
// It lets users browse available hotels and their ratings.
// It also lets admins add new hotels to a destination.

public class HotelService {

    private static int nextHotelId = 1;

    // Load existing data
    public static void init() {
        List<Hotel> hotels = DataStore.loadHotels();
        if (!hotels.isEmpty()) {
            nextHotelId = hotels.get(hotels.size() - 1).getId() + 1;
        } else {
            addSampleData();
        }
    }

    private static void addSampleData() {
        addHotel(1, "Dubai Luxury Suites", 5, "+971-50-1234567", "Sheikh Zayed Rd, Dubai", 450.00);
        addHotel(2, "Hotel de Paris", 4, "+33-1-4444-5555", "Rue de Rivoli, Paris", 350.00);
        addHotel(3, "Istanbul Old City Hotel", 4, "+90-212-555-6677", "Sultanahmet, Istanbul", 120.00);
        System.out.println("Sample hotels loaded.");
    }

    // Show all available hotels
    public static void showAllHotels() {
        List<Hotel> hotels = DataStore.loadHotels();

        if (hotels.isEmpty()) {
            System.out.println("No hotels available right now.");
            return;
        }

        System.out.println("\nAll Available Hotels:");
        System.out.println("======================");
        for (Hotel h : hotels) {
            System.out.println(h);
        }
        System.out.println();
    }

    // Search for hotels by name
    public static List<Hotel> searchByName(String query) {
        List<Hotel> results = new ArrayList<>();
        for (Hotel h : DataStore.loadHotels()) {
            if (h.getName().toLowerCase().contains(query.toLowerCase())) {
                results.add(h);
            }
        }
        return results;
    }

    // Get a hotel by its ID
    public static Hotel getHotelById(int id) throws SmartGoException {
        for (Hotel h : DataStore.loadHotels()) {
            if (h.getId() == id) return h;
        }
        throw new SmartGoException("No hotel found with ID " + id + ".");
    }

    // Add a new hotel (admin only)
    public static void addHotel(int destinationId, String name, int rating,
                                String managerContact, String address, double pricePerNight) {
        Hotel hotel = new Hotel(nextHotelId++, destinationId, name, rating, managerContact, address, pricePerNight);
        DataStore.saveHotel(hotel);
        System.out.println("Hotel added: " + name);
    }

    // Get all hotels
    public static List<Hotel> getAllHotels() {
        return DataStore.loadHotels();
    }
}
