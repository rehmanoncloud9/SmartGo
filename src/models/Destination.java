package models;

// A destination is a place users can travel to.
// Hotels and tour plans are linked to a destination.

public class Destination {

    // These fields store the geographical location and highlight features of a travel spot
    private int id;
    private String city;
    private String region;
    private String country;
    private String attractions;
    private String imageUrl;

    // This constructor sets up the initial details for a new city in our travel database
    public Destination(int id, String city, String region, String country, String attractions, String imageUrl) {
        this.id = id;
        this.city = city;
        this.region = region;
        this.country = country;
        this.attractions = attractions;
        this.imageUrl = imageUrl;
    }

    public int getId() { return id; }
    public String getCity() { return city; }
    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public String getAttractions() { return attractions; }
    public String getImageUrl() { return imageUrl; }

    public void setAttractions(String attractions) { this.attractions = attractions; }

    @Override
    public String toString() {
        // This provides a readable summary of the destination for the travel listings
        return "Destination ID: " + id + " | " + city + ", " + region + ", " + country
                + " | Attractions: " + attractions;
    }
}
