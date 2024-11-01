// src/RideSharingManagementSystem.java
import java.util.*;

public class RideSharingManagementSystem {
    private Map<String, Rider> riders = new HashMap<>();
    private Map<String, Driver> drivers = new HashMap<>();
    private PriorityQueue<Driver> availableDrivers;
    private Trie riderTrie = new Trie();
    private Trie driverTrie = new Trie();
    private Graph roadNetwork = new Graph();

    public RideSharingManagementSystem() {
        // Multi-level priority by sorting based on distance and rating
        availableDrivers = new PriorityQueue<>(Comparator.comparingDouble(d -> d.calculateDistance(new Location(0, 0)) - d.rating));
    }

    public void registerRider(String name, String id, Location location) {
        Rider newRider = new Rider(name, id, location);
        riders.put(id, newRider);
        riderTrie.insert(name);
        System.out.println("Rider registered: " + name);
    }

    public void registerDriver(String name, String id, Location location, double rating) {
        Driver newDriver = new Driver(name, id, location, rating);
        drivers.put(id, newDriver);
        driverTrie.insert(name);
        availableDrivers.add(newDriver);
        System.out.println("Driver registered: " + name);
    }

    public List<String> searchRiderByName(String prefix) {
        return riderTrie.autocomplete(prefix);
    }

    public List<String> searchDriverByName(String prefix) {
        return driverTrie.autocomplete(prefix);
    }

    // Matches drivers to riders based on nearest distance using Dijkstra
    public String matchDriver(String riderId) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            return "Rider not found!";
        }
        if (availableDrivers.isEmpty()) {
            return "No drivers available!";
        }

        Driver bestMatch = null;
        double bestDistance = Double.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            double distance = roadNetwork.calculateShortestDistance(driver.currentLocation, rider.currentLocation);
            if (distance < bestDistance && driver.isAvailable) {
                bestDistance = distance;
                bestMatch = driver;
            }
        }

        if (bestMatch != null) {
            bestMatch.isAvailable = false;
            availableDrivers.remove(bestMatch);
            return "Driver " + bestMatch.name + " matched with Rider " + rider.name + " at a distance of " + bestDistance;
        }
        return "No matching driver found.";
    }

    public static void main(String[] args) {
        RideSharingManagementSystem system = new RideSharingManagementSystem();

        // Register riders
        system.registerRider("Alice", "R1", new Location(1, 1));
        system.registerRider("Bob", "R2", new Location(2, 2));

        // Register drivers
        system.registerDriver("Charlie", "D1", new Location(1.5, 1.5), 4.5);
        system.registerDriver("David", "D2", new Location(5, 5), 4.0);

        // Search riders and drivers by name
        System.out.println("Searching riders with 'A': " + system.searchRiderByName("A"));
        System.out.println("Searching drivers with 'D': " + system.searchDriverByName("D"));

        // Route network setup
        Location loc1 = new Location(1, 1);
        Location loc2 = new Location(2, 2);
        Location loc3 = new Location(1.5, 1.5);
        system.roadNetwork.addEdge(loc1, loc2);
        system.roadNetwork.addEdge(loc1, loc3);
        system.roadNetwork.addEdge(loc2, loc3);

        // Match driver to rider
        System.out.println(system.matchDriver("R1"));
        System.out.println(system.matchDriver("R2"));
    }
}
