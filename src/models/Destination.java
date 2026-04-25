package models;

// Destination: A travel location like Lahore, Dubai, Bangkok
//
// OOP: plain model class with encapsulation (private fields + getters/setters)

public class Destination {

    private int    id;
    private String city;
    private String region;
    private String country;
    private String attractions; // comma-separated, e.g. "Badshahi Mosque, Lahore Fort"

    public Destination(int id, String city, String region,
                       String country, String attractions) {
        this.id          = id;
        this.city        = city;
        this.region      = region;
        this.country     = country;
        this.attractions = attractions;
    }

    public void displayInfo() {
        System.out.println("  📍 " + city + ", " + region + " - " + country);
        System.out.println("  Attractions : " + attractions);
    }

    // Getters
    public int    getId()          { return id;          }
    public String getCity()        { return city;        }
    public String getRegion()      { return region;      }
    public String getCountry()     { return country;     }
    public String getAttractions() { return attractions; }

    // Setters
    public void setAttractions(String attractions) { this.attractions = attractions; }

    public String toFileString() {
        return id + "," + city + "," + region + "," + country + "," + attractions;
    }
}
