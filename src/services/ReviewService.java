package services;

import models.Review;
import models.User;
import exceptions.SmartGoException;
import data.DataStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Review Service: Leave and view reviews
//
// Users can review a Hotel, Flight, or Tour Plan.
// This demonstrates the POLYMORPHIC ASSOCIATION from the ERD.

public class ReviewService {

    private List<Review> reviews;

    public ReviewService() {
        this.reviews = DataStore.loadReviews();
    }

    // Leave a Review
    public void addReview(User user, int referenceId,
                          String reviewType, int rating,
                          String comment) throws SmartGoException {

        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new SmartGoException("Rating must be between 1 and 5.");
        }

        // Validate review type
        if (!reviewType.equals("HOTEL")
                && !reviewType.equals("FLIGHT")
                && !reviewType.equals("TOUR_PLAN")) {
            throw new SmartGoException("Invalid review type.");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new SmartGoException("Please write a comment for your review.");
        }

        int newId = reviews.size() + 1;
        String now = LocalDateTime.now()
                     .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Review review = new Review(newId, user.getId(), referenceId,
                                   reviewType, rating, comment, now);

        reviews.add(review);
        DataStore.saveReviews(reviews);

        System.out.println("\n  ✅ Thank you for your review, " + user.getName() + "!");
    }

    // View all reviews for something (hotel, flight, or tour plan)
    public void displayReviews(int referenceId, String reviewType) throws SmartGoException {
        List<Review> filtered = new ArrayList<>();

        for (Review r : reviews) {
            if (r.getReferenceId() == referenceId
                    && r.getReviewType().equals(reviewType)) {
                filtered.add(r);
            }
        }

        if (filtered.isEmpty()) {
            throw new SmartGoException("No reviews yet for this " + reviewType.toLowerCase() + ".");
        }

        System.out.println("\n  ========== REVIEWS ==========");
        for (Review r : filtered) {
            r.displayInfo(); // polymorphic: shows different header based on type
        }
    }
}
