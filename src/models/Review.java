package models;

// A review can be left for a hotel, a flight, or a tour plan.
// reviewable_type tells us what kind of thing is being reviewed.
// reviewable_id is the ID of that specific hotel, flight, or tour plan.
// This is polymorphism in action - one Review class works for many types.

public class Review {

    // These fields capture the identity of the reviewer and the target of the feedback
    private int id;
    private int userId;
    private String reviewableType;
    private int reviewableId;
    private int rating;
    private String comment;
    private String createdAt;

    // This constructor initializes the review with the specific rating and descriptive comments
    public Review(int id, int userId, String reviewableType, int reviewableId,
                  int rating, String comment, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.reviewableType = reviewableType;
        this.reviewableId = reviewableId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getReviewableType() { return reviewableType; }
    public int getReviewableId() { return reviewableId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        // This provides a helpful textual summary of the user's feedback for the interface
        return "Review ID: " + id + " | For: " + reviewableType + " #" + reviewableId
                + " | Rating: " + rating + "/5 | Comment: " + comment
                + " | Posted: " + createdAt;
    }
}
