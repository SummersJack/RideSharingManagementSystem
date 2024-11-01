// src/Graph.java
import java.util.*;

// Represents a graph where each Location is a node
public class Graph {
    private Map<Location, List<Location>> adjList; // Bug: adjList is not initialized

    public void addEdge(Location src, Location dest) {
        adjList.computeIfAbsent(src, k -> new ArrayList<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(src);
    }

    // Dijkstra’s algorithm to find the shortest path between two locations
    public double calculateShortestDistance(Location src, Location dest) {
        Map<Location, Double> distance = new HashMap<>();
        PriorityQueue<Map.Entry<Location, Double>> pq = new PriorityQueue<>(Map.Entry.comparingByValue);

        distance.put(src, 0.0);
        pq.add(new AbstractMap.SimpleEntry<>(src, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<Location, Double> current = pq.poll();
            Location loc = current.getKey();
            double dist = current.getValue();

            if (loc.equals(dest)) return dist;

            for (Location neighbor : adjList.getOrDefault(loc, new ArrayList<>())) {
                double newDist = dist + loc.calculateDistance(neighbor);
                if (newDist < distance.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    distance.put(neighbor, newDist);
                    pq.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }
        return Double.MAX_VALUE;
    }
}
