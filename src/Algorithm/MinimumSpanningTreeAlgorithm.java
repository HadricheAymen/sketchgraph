package Algorithm;

import Decorators.ColorDecorator;
import Decorators.ThicknessDecorator;
import Providers.StyleProvider;
import ShapeFactory.CircleFactory;
import ShapeFactory.LineFactory;
import ShapeFactory.ShapeFactory;
import States.ShapeManager;
import javafx.scene.paint.Color;
import java.util.*;

/**
 * Finds the minimum spanning tree using Kruskal's algorithm
 */
public class MinimumSpanningTreeAlgorithm implements GraphAlgorithm {
    
    private static class Edge implements Comparable<Edge> {
        CircleFactory source;
        CircleFactory destination;
        LineFactory line;
        double weight;
        
        public Edge(CircleFactory source, CircleFactory destination, LineFactory line, double weight) {
            this.source = source;
            this.destination = destination;
            this.line = line;
            this.weight = weight;
        }
        
        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }
    
    private static class DisjointSet {
        private Map<CircleFactory, CircleFactory> parent = new HashMap<>();
        
        public void makeSet(CircleFactory circle) {
            parent.put(circle, circle);
        }
        
        public CircleFactory find(CircleFactory circle) {
            if (parent.get(circle) != circle) {
                parent.put(circle, find(parent.get(circle)));
            }
            return parent.get(circle);
        }
        
        public void union(CircleFactory circle1, CircleFactory circle2) {
            CircleFactory root1 = find(circle1);
            CircleFactory root2 = find(circle2);
            if (root1 != root2) {
                parent.put(root1, root2);
            }
        }
    }
    
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
            DialogUtils.showError("Not Enough Circles", "You need at least 2 circles to create a minimum spanning tree.");
            return;
        }
        
        // Build the graph
        Map<CircleFactory, List<LineFactory>> adjacencyList = buildAdjacencyList();
        List<Edge> edges = new ArrayList<>();
        
        // Create edges from adjacency list
        for (CircleFactory source : adjacencyList.keySet()) {
            for (LineFactory line : adjacencyList.get(source)) {
                CircleFactory destination = (line.getFirstCircle() == source) ? 
                    line.getSecondCircle() : line.getFirstCircle();
                
                // Avoid duplicate edges
                boolean isDuplicate = false;
                for (Edge existingEdge : edges) {
                    if ((existingEdge.source == source && existingEdge.destination == destination) ||
                        (existingEdge.source == destination && existingEdge.destination == source)) {
                        isDuplicate = true;
                        break;
                    }
                }
                
                if (!isDuplicate) {
                    edges.add(new Edge(source, destination, line, line.getValue()));
                }
            }
        }
        
        // Sort edges by weight
        Collections.sort(edges);
        
        // Apply Kruskal's algorithm
        DisjointSet disjointSet = new DisjointSet();
        for (CircleFactory circle : circles) {
            disjointSet.makeSet(circle);
        }
        
        List<Edge> minimumSpanningTree = new ArrayList<>();
        double totalWeight = 0;
        
        for (Edge edge : edges) {
            CircleFactory root1 = disjointSet.find(edge.source);
            CircleFactory root2 = disjointSet.find(edge.destination);
            
            if (root1 != root2) {
                minimumSpanningTree.add(edge);
                totalWeight += edge.weight;
                disjointSet.union(edge.source, edge.destination);
            }
        }
        
        // Check if we have a spanning tree
        if (minimumSpanningTree.size() != circles.size() - 1 && !circles.isEmpty() && !edges.isEmpty()) {
            DialogUtils.showError("No Spanning Tree", "The graph is not connected. Cannot create a minimum spanning tree.");
            return;
        }
        
        // Highlight the minimum spanning tree
        highlightMinimumSpanningTree(minimumSpanningTree);
        
        // Show the result
        StringBuilder edgeList = new StringBuilder();
        for (Edge edge : minimumSpanningTree) {
            edgeList.append(edge.source.getName())
                   .append(" — ")
                   .append(edge.destination.getName())
                   .append(" (weight: ")
                   .append(edge.weight)
                   .append(")\n");
        }
        
        DialogUtils.showAlert("Minimum Spanning Tree", 
            "Minimum spanning tree created with total weight: " + totalWeight + "\n\n" +
            "Edges in the tree:\n" + edgeList.toString());
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

    private void highlightMinimumSpanningTree(List<Edge> minimumSpanningTree) {
        StyleProvider styleProvider =StyleProvider.getInstance();
        // Reset all shapes to original appearance
        ShapeManager.getInstance().reDrawAll();
        
        // Highlight circles and lines in the MST
        Set<CircleFactory> highlightedCircles = new HashSet<>();
        
        for (Edge edge : minimumSpanningTree) {
            // Highlight the line
            ShapeFactory highlightedLine = new ColorDecorator(edge.line, styleProvider.getHighlightedColor());
            highlightedLine = new ThicknessDecorator(highlightedLine, styleProvider.getHiglightedLineWidth());
            highlightedLine.draw();
            
            // Highlight the circles if not already highlighted
            if (!highlightedCircles.contains(edge.source)) {
                ShapeFactory highlightedSource = new ColorDecorator(edge.source, styleProvider.getHighlightedColor());
                highlightedSource = new ThicknessDecorator(highlightedSource, styleProvider.getHiglightedLineWidth());
                highlightedSource.draw();
                highlightedCircles.add(edge.source);
            }
            
            if (!highlightedCircles.contains(edge.destination)) {
                ShapeFactory highlightedDest = new ColorDecorator(edge.destination, styleProvider.getHighlightedColor());
                highlightedDest = new ThicknessDecorator(highlightedDest, styleProvider.getHiglightedLineWidth());
                highlightedDest.draw();
                highlightedCircles.add(edge.destination);
            }
        }
    }
}