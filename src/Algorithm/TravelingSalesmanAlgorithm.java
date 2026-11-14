package Algorithm;

import Providers.StyleProvider;
import ShapeFactory.CircleFactory;
import ShapeFactory.LineFactory;
import ShapeFactory.ShapeFactory;
import Decorators.*;
import Providers.GraphicsContextProvider;
import States.ShapeManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

/**
 * Approximates the Traveling Salesman Problem using a nearest neighbor approach
 */
public class TravelingSalesmanAlgorithm implements GraphAlgorithm {
    
    @Override
    public void launch() {
        List<ShapeFactory> shapes = ShapeManager.getInstance().getShapes();
        List<CircleFactory> circles = new ArrayList<>();
        for (ShapeFactory shape : shapes) {
            if (shape instanceof CircleFactory) {
                circles.add((CircleFactory) shape);
            }
        }
        
        if (circles.size() < 3) {
            DialogUtils.showError("Not Enough Circles", "You need at least 3 circles for the Traveling Salesman Problem.");
            return;
        }
        
        // Let user select start circle
        CircleFactory startCircle = DialogUtils.selectCircle(circles, "Select Start Circle", 
                                                           "Choose the starting circle for the tour:");
        if (startCircle == null) return;
        
        // Build the complete graph (all possible connections)
        Map<CircleFactory, Map<CircleFactory, Double>> graph = new HashMap<>();
        
        // Find all existing lines to get their values
        Map<Pair<CircleFactory, CircleFactory>, LineFactory> existingLines = findExistingLines();
        
        for (CircleFactory circle1 : circles) {
            Map<CircleFactory, Double> edges = new HashMap<>();
            graph.put(circle1, edges);
            
            for (CircleFactory circle2 : circles) {
                if (circle1 != circle2) {
                    edges.put(circle2, getDistance(circle1, circle2, existingLines));
                }
            }
        }
        
        // Apply nearest neighbor algorithm
        List<CircleFactory> tour = new ArrayList<>();
        Set<CircleFactory> unvisited = new HashSet<>(circles);
        
        CircleFactory current = startCircle;
        tour.add(current);
        unvisited.remove(current);
        double totalDistance = 0;
        
        while (!unvisited.isEmpty()) {
            CircleFactory nearest = null;
            double minDistance = Double.MAX_VALUE;
            
            for (CircleFactory next : unvisited) {
                double distance = graph.get(current).get(next);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = next;
                }
            }
            
            current = nearest;
            tour.add(current);
            unvisited.remove(current);
            totalDistance += minDistance;
        }
        
        // Add the return to start
        totalDistance += graph.get(tour.get(tour.size() - 1)).get(startCircle);
        tour.add(startCircle); // Complete the cycle
        
        // Highlight the tour
        highlightTour(tour, existingLines);
        
        // Show the result
        StringBuilder pathDescription = new StringBuilder();
        for (CircleFactory circle : tour) {
            pathDescription.append(circle.getName()).append(" → ");
        }
        pathDescription.delete(pathDescription.length() - 3, pathDescription.length());
        
        DialogUtils.showAlert("Traveling Salesman Tour", 
            "Tour: " + pathDescription.toString() + "\n" +
            "Total distance: " + String.format("%.2f", totalDistance));
    }
    
    /**
     * Finds all existing lines between circles
     * @return A map of circle pairs to their connecting lines
     */
    private Map<Pair<CircleFactory, CircleFactory>, LineFactory> findExistingLines() {
        Map<Pair<CircleFactory, CircleFactory>, LineFactory> lines = new HashMap<>();
        
        for (ShapeFactory shape : ShapeManager.getInstance().getShapes()) {
            if (shape instanceof LineFactory) {
                LineFactory line = (LineFactory) shape;
                CircleFactory circle1 = line.getFirstCircle();
                CircleFactory circle2 = line.getSecondCircle();
                
                if (circle1 != null && circle2 != null) {
                    lines.put(new Pair<>(circle1, circle2), line);
                    lines.put(new Pair<>(circle2, circle1), line); // Add both directions
                }
            }
        }
        
        return lines;
    }
    
    /**
     * Gets the distance between two circles
     * If a line exists between them, use its value
     * Otherwise, calculate Euclidean distance
     */
    private double getDistance(CircleFactory circle1, CircleFactory circle2, 
                              Map<Pair<CircleFactory, CircleFactory>, LineFactory> existingLines) {
        Pair<CircleFactory, CircleFactory> pair = new Pair<>(circle1, circle2);
        
        if (existingLines.containsKey(pair)) {
            // If a line exists between these circles, use its value
            return existingLines.get(pair).getValue();
        } else {
            // Otherwise calculate Euclidean distance between circle centers
            double c1x = (circle1.getStartX() + circle1.getEndX()) / 2;
            double c1y = (circle1.getStartY() + circle1.getEndY()) / 2;
            double c2x = (circle2.getStartX() + circle2.getEndX()) / 2;
            double c2y = (circle2.getStartY() + circle2.getEndY()) / 2;
            
            return Math.sqrt(Math.pow(c2x - c1x, 2) + Math.pow(c2y - c1y, 2));
        }
    }
    
    private void highlightTour(List<CircleFactory> tour, 
                              Map<Pair<CircleFactory, CircleFactory>, LineFactory> existingLines) {
        StyleProvider styleProvider =StyleProvider.getInstance();
        // Reset all shapes to original appearance
        ShapeManager.getInstance().reDrawAll();
        
        // Highlight circles in the tour
        for (CircleFactory circle : tour) {
            ShapeFactory highlightedCircle = new ColorDecorator(circle, styleProvider.getHighlightedColor());
            highlightedCircle = new ThicknessDecorator(highlightedCircle, styleProvider.getHiglightedLineWidth());
            highlightedCircle.draw();
        }
        
        // Draw lines for the tour
        GraphicsContext gc = GraphicsContextProvider.getGraphicsContext();
        Color originalColor = (Color) gc.getStroke();
        double originalWidth = gc.getLineWidth();
        
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2.0);
        
        for (int i = 0; i < tour.size() - 1; i++) {
            CircleFactory current = tour.get(i);
            CircleFactory next = tour.get(i + 1);
            Pair<CircleFactory, CircleFactory> pair = new Pair<>(current, next);
            
            if (existingLines.containsKey(pair)) {
                // If a line exists, highlight it
                LineFactory line = existingLines.get(pair);
                ShapeFactory highlightedLine = new ColorDecorator(line, Color.BLUE);
                highlightedLine.draw();
            } else {
                // Otherwise draw a dashed line to show the connection
                double c1x = (current.getStartX() + current.getEndX()) / 2;
                double c1y = (current.getStartY() + current.getEndY()) / 2;
                double c2x = (next.getStartX() + next.getEndX()) / 2;
                double c2y = (next.getStartY() + next.getEndY()) / 2;
                
                // Draw dashed line
                double[] dashPattern = {5, 5};
                gc.setLineDashes(dashPattern);
                gc.strokeLine(c1x, c1y, c2x, c2y);
                gc.setLineDashes(null); // Reset dash pattern
            }
        }
        
        // Restore original graphics context settings
        gc.setStroke(originalColor);
        gc.setLineWidth(originalWidth);
    }
}