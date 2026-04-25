package models;

// Review: A rating left by a User on something they experienced
//
// A Review can be for:
//   - A Hotel       (reviewType = "HOTEL",     referenceId = hotelId)
//   - A Flight      (reviewType = "FLIGHT",    referenceId = transportId)
//   - A Tour Plan   (reviewType = "TOUR_PLAN", referenceId = tourPlanId)
//
// OOP: POLYMORPHIC ASSOCIATION
// Instead of making three separate review classes, one Review class
// handles all three using a "type" field. This is the same idea as
// polymorphism: one shape, different behaviors based on type.

public class Review {

    private int    id;
    private int    userId;       // who wrote the review
    private int    referenceId;  // ID of what they reviewed (hotel/flight/tourplan)
    private String reviewType;   // "HOTEL", "FLIGHT", or "TOUR_PLAN"
    private int    rating;       // 1 to 5
    private String comment;      // what they said
    private String createdAt;

    public Review(int id, int userId, int referenceId,
                  String reviewType, int rating,
                  String comment, String createdAt) {
        this.id          = id;
        this.userId      = userId;
        this.referenceId = referenceId;
        this.reviewType  = reviewType;
        this.rating      = rating;
        this.comment     = comment;
        this.createdAt   = createdAt;
    }

    // Display review: behavior changes based on reviewType
    // This is polymorphism without subclasses (type-based)
    public void displayInfo() {
        System.out.println("------------------------------");
        // Show what kind of thing was reviewed
        switch (reviewType) {
            case "HOTEL":     System.out.println("  🏨 Hotel Review");     break;
            case "FLIGHT":    System.out.println("  ✈  Flight Review");    break;
            case "TOUR_PLAN": System.out.println("  🗺  Tour Plan Review"); break;
            default:          System.out.println("  📝 Review");           break;
        }
        System.out.println("  Rating  : " + "⭐".repeat(rating) + " (" + rating + "/5)");
        System.out.println("  Comment : " + comment);
        System.out.println("  Date    : " + createdAt);
        System.out.println("------------------------------");
    }

    // Getters
    public int    getId()          { return id;          }
    public int    getUserId()      { return userId;      }
    public int    getReferenceId() { return referenceId; }
    public String getReviewType()  { return reviewType;  }
    public int    getRating()      { return rating;      }
    public String getComment()     { return comment;     }
    public String getCreatedAt()   { return createdAt;   }

    public String toFileString() {
        return id + "," + userId + "," + referenceId + ","
             + reviewType + "," + rating + "," + comment + "," + createdAt;
    }
}
