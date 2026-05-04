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
        addHotel(1, "Multan Continental Hotel", 4, "+92-61-111-2233", "Nawan Shehar, Multan", 35.00);
        addHotel(2, "Pearl Continental Lahore", 5, "+92-42-111-5050", "Shahrah-e-Quaid-e-Azam, Lahore", 85.00);
        addHotel(3, "Shangrila Resort Skardu", 5, "+92-58-145-0101", "Lower Kachura Lake, Skardu", 70.00);
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
