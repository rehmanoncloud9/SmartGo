package services;

import data.DataStore;
import exceptions.SmartGoException;
import models.MealPlan;
import models.TourPlan;

import java.util.ArrayList;
import java.util.List;

// TourPlanService handles everything related to tour plans.
// It lets users browse available tours and their meal options.
// It also lets admins add new tour plans and meal plans.

public class TourPlanService {

    private static int nextPlanId = 1;
    private static int nextMealId = 1;

    // Load existing data and add sample tour plans if none exist
    public static void init() {
        List<TourPlan> existing = DataStore.loadTourPlans();
        if (!existing.isEmpty()) {
            nextPlanId = existing.get(existing.size() - 1).getId() + 1;
        }

        List<MealPlan> meals = DataStore.loadMealPlans();
        if (!meals.isEmpty()) {
            nextMealId = meals.get(meals.size() - 1).getId() + 1;
        }

        if (existing.isEmpty()) {
            addSampleData();
        }
    }

    // Add sample tour plans so the app is not empty on first run
    private static void addSampleData() {
        TourPlan t1 = new TourPlan(nextPlanId++, 1, 1, "Multan Heritage Tour", 4, 180.00, "ACTIVE");
        TourPlan t2 = new TourPlan(nextPlanId++, 2, 1, "Lahore Cultural Experience", 5, 220.00, "ACTIVE");
        TourPlan t3 = new TourPlan(nextPlanId++, 3, 1, "Skardu Mountain Adventure", 7, 350.00, "ACTIVE");

        DataStore.saveTourPlan(t1);
        DataStore.saveTourPlan(t2);
        DataStore.saveTourPlan(t3);

        MealPlan m1 = new MealPlan(nextMealId++, t1.getId(), "Full Board", "All meals included", 40.00);
        MealPlan m2 = new MealPlan(nextMealId++, t2.getId(), "Breakfast and Dinner", "Breakfast and dinner included", 35.00);
        MealPlan m3 = new MealPlan(nextMealId++, t3.getId(), "Standard Meals", "Breakfast and dinner included", 50.00);
        MealPlan m4 = new MealPlan(nextMealId++, t3.getId(), "Premium Meals", "All meals plus room service", 90.00);

        DataStore.saveMealPlan(m1);
        DataStore.saveMealPlan(m2);
        DataStore.saveMealPlan(m3);
        DataStore.saveMealPlan(m4);

        System.out.println("Sample tour plans and meal plans loaded.");
    }

    // Show all active tour plans
    public static void showAllTourPlans() {
        List<TourPlan> plans = DataStore.loadTourPlans();

        if (plans.isEmpty()) {
            System.out.println("No tour plans available right now.");
            return;
        }

        System.out.println("\nAll Available Tour Plans:");
        System.out.println("==========================");
        for (TourPlan p : plans) {
            if (p.getStatus().equals("ACTIVE")) {
                System.out.println(p);
            }
        }
        System.out.println();
    }

    // Show meal plans available for a specific tour plan
    public static void showMealPlansForTour(int tourPlanId) {
        List<MealPlan> meals = DataStore.loadMealPlans();
        boolean found = false;

        System.out.println("\nMeal Plans for Tour Plan #" + tourPlanId + ":");
        for (MealPlan m : meals) {
            if (m.getTourPlanId() == tourPlanId) {
                System.out.println(m);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No meal plans available for this tour.");
        }
        System.out.println();
    }

    // Get a tour plan by its ID
    public static TourPlan getTourPlanById(int id) throws SmartGoException {
        for (TourPlan p : DataStore.loadTourPlans()) {
            if (p.getId() == id) return p;
        }
        throw new SmartGoException("No tour plan found with ID " + id + ". Please check and try again.");
    }

    // Get a meal plan by its ID
    public static MealPlan getMealPlanById(int id) throws SmartGoException {
        for (MealPlan m : DataStore.loadMealPlans()) {
            if (m.getId() == id) return m;
        }
        throw new SmartGoException("No meal plan found with ID " + id + ". Please check and try again.");
    }

    // Get all meal plans for a specific tour plan
    public static List<MealPlan> getMealPlansForTour(int tourPlanId) {
        List<MealPlan> result = new ArrayList<>();
        for (MealPlan m : DataStore.loadMealPlans()) {
            if (m.getTourPlanId() == tourPlanId) result.add(m);
        }
        return result;
    }

    // Add a new tour plan (admin only)
    public static void addTourPlan(int destinationId, int adminId, String title,
                                    int durationDays, double basePrice) {
        TourPlan plan = new TourPlan(nextPlanId++, destinationId, adminId, title, durationDays, basePrice, "ACTIVE");
        DataStore.saveTourPlan(plan);
        System.out.println("Tour plan added: " + title);
    }

    // Add a meal plan to an existing tour (admin only)
    public static void addMealPlan(int tourPlanId, String name, String description, double price) {
        MealPlan meal = new MealPlan(nextMealId++, tourPlanId, name, description, price);
        DataStore.saveMealPlan(meal);
        System.out.println("Meal plan added: " + name);
    }

    // Get all tour plans
    public static List<TourPlan> getAllTourPlans() {
        return DataStore.loadTourPlans();
    }
}

