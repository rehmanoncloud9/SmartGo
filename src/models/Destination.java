package models;

// A destination is a place users can travel to.
// Hotels and tour plans are linked to a destination.

public class Destination {

    private int id;
    private String city;
    private String region;
    private String country;
    private String attractions;
    private String imageUrl;

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
        return "Destination ID: " + id + " | " + city + ", " + region + ", " + country
                + " | Attractions: " + attractions;
    }
}
