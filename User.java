
import java.util.*;

public abstract class User implements Comparable<User> {
    String name;
    String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int compareTo(User other) {
        return this.name.compareTo(other.name);
    }
}

// Rider subclass
class Rider extends User {
    Location currentLocation;

    public Rider(String name, String id, Location location) {
        super(name, id);
        this.currentLocation = location;
    }
}

// Driver subclass with rating and availability
class Driver extends User {
    Location currentLocation;
    double rating;
    boolean isAvailable;

    public Driver(String name, String id, Location location, double rating) {
        super(name, id);
        this.currentLocation = location;
        this.rating = rating;
        this.isAvailable = true;
    }

    public double calculateDistance(Location riderLocation) {
        return this.currentLocation.calculateDistance(riderLocation);
    }
}
