package Algorithm;

import Decorators.ColorDecorator;
import Decorators.ThicknessDecorator;
import Providers.StyleProvider;
import ShapeFactory.CircleFactory;
import ShapeFactory.LineFactory;
import ShapeFactory.ShapeFactory;
import States.ShapeManager;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.util.*;

/**
 * Finds the shortest path between two circles using Dijkstra's algorithm
 */
public class ShortestPathAlgorithm implements GraphAlgorithm {
    
    @Override
    public void launch() {
        List<ShapeFactory> shapes =ShapeManager.getInstance().getShapes();
        List<CircleFactory> circles = new ArrayList<>();
        for (ShapeFactory shape : shapes) {
            if (shape instanceof CircleFactory) {
                circles.add((CircleFactory) shape);
            }
        }
        
        if (circles.size() < 2) {
            DialogUtils.showError("Not Enough Circles", "You need at least 2 circles to find a path.");
            return;
        }
        
        // Let user select start and end circles
        Pair<CircleFactory, CircleFactory> selection = DialogUtils.selectStartAndEndCircles(circles);
        if (selection == null) return;
        
        CircleFactory startCircle = selection.getKey();
        CircleFactory endCircle = selection.getValue();
        
        // Build the graph
        Map<CircleFactory, List<LineFactory>> adjacencyList = buildAdjacencyList();
        
        // Run Dijkstra's algorithm
        Map<CircleFactory, Double> distances = new HashMap<>();
        Map<CircleFactory, CircleFactory> previousNodes = new HashMap<>();
        PriorityQueue<CircleFactory> queue = new PriorityQueue<>(
            Comparator.comparingDouble(c -> distances.getOrDefault(c, Double.MAX_VALUE))
        );
        
        // Initialize distances
        for (CircleFactory circle : circles) {
            distances.put(circle, Double.MAX_VALUE);
        }
        distances.put(startCircle, 0.0);
        queue.add(startCircle);
        
        while (!queue.isEmpty()) {
            CircleFactory current = queue.poll();
            
            if (current == endCircle) {
                break; // Found the destination
            }
            
            if (!adjacencyList.containsKey(current)) {
                continue; // Skip if no connections
            }
            
            for (LineFactory line : adjacencyList.get(current)) {
                CircleFactory neighbor = (line.getFirstCircle() == current) ? 
                    line.getSecondCircle() : line.getFirstCircle();
                
                double weight = line.getValue();
                double distanceThroughCurrent = distances.get(current) + weight;
                
                if (distanceThroughCurrent < distances.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    queue.remove(neighbor);
                    distances.put(neighbor, distanceThroughCurrent);
                    previousNodes.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        // Reconstruct the path
        if (!previousNodes.containsKey(endCircle)) {
            DialogUtils.showError("No Path Found", "There is no path between the selected circles.");
            return;
        }
        
        List<CircleFactory> path = new ArrayList<>();
        CircleFactory current = endCircle;
        
        while (current != startCircle) {
            path.add(current);
            current = previousNodes.get(current);
        }
        path.add(startCircle);
        Collections.reverse(path);
        
        // Highlight the path
        highlightPath(path, adjacencyList);
        
        // Show the result
        double totalDistance = distances.get(endCircle);
        StringBuilder pathDescription = new StringBuilder();
        for (CircleFactory circle : path) {
            pathDescription.append(circle.getName()).append(" → ");
        }
        pathDescription.delete(pathDescription.length() - 3, pathDescription.length());
        
        DialogUtils.showAlert("Path Found", 
            "Shortest path: " + pathDescription.toString() + "\n" +
            "Total weight: " + totalDistance);
    }

    protected Map<CircleFactory, List<LineFactory>> buildAdjacencyList() {
        Map<CircleFactory, List<LineFactory>> adjacencyList = new HashMap<>();
        List<ShapeFactory> shapes =ShapeManager.getInstance().getShapes();

        List<CircleFactory> circles = new ArrayList<>();
        for (ShapeFactory shape : shapes) {
            if (shape instanceof CircleFactory) {
                circles.add((CircleFactory) shape);
            }
        }
        List<LineFactory> lines = new ArrayList<>();

        // Initialize adjacency list for all circles
        for (CircleFactory circle : circles) {
            adjacencyList.put(circle, new ArrayList<>());
        }

        // Find all lines
        for (ShapeFactory shape : ShapeManager.getInstance().getShapes()) {
            if (shape instanceof LineFactory) {
                lines.add((LineFactory) shape);
            }
        }

        // Build adjacency list
        for (LineFactory line : lines) {
            CircleFactory circle1 = line.getFirstCircle();
            CircleFactory circle2 = line.getSecondCircle();

            if (circle1 != null && circle2 != null) {
                adjacencyList.get(circle1).add(line);
                adjacencyList.get(circle2).add(line);
            }
        }

        return adjacencyList;
    }

    private void highlightPath(List<CircleFactory> path, Map<CircleFactory, List<LineFactory>> adjacencyList) {
        StyleProvider styleProvider =StyleProvider.getInstance();
        // Reset all shapes to original appearance
        ShapeManager.getInstance().reDrawAll();
        
        // Highlight circles in the path
        for (CircleFactory circle : path) {
            ShapeFactory highlightedCircle = new ColorDecorator(circle, styleProvider.getHighlightedColor());
            highlightedCircle = new ThicknessDecorator(highlightedCircle, styleProvider.getHiglightedLineWidth());
            highlightedCircle.draw();
        }
        
        // Highlight lines in the path
        for (int i = 0; i < path.size() - 1; i++) {
            CircleFactory current = path.get(i);
            CircleFactory next = path.get(i + 1);
            
            if (!adjacencyList.containsKey(current)) continue;
            
            for (LineFactory line : adjacencyList.get(current)) {
                if ((line.getFirstCircle() == current && line.getSecondCircle() == next) ||
                    (line.getFirstCircle() == next && line.getSecondCircle() == current)) {
                    ShapeFactory highlightedLine = new ColorDecorator(line, styleProvider.getHighlightedColor());
                    highlightedLine = new ThicknessDecorator(highlightedLine, styleProvider.getHiglightedLineWidth());
                    highlightedLine.draw();
                    break;
                }
            }
        }
    }
}