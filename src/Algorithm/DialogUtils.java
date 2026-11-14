package Algorithm;

import ShapeFactory.CircleFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.util.Pair;
import java.util.*;

/**
 * Utility class for creating dialog boxes for algorithm inputs
 */
public class DialogUtils {
    
    /**
     * Shows an alert dialog with the given title and message
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows an error dialog with the given title and message
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a dialog for selecting a single circle
     * @return The selected circle or null if canceled
     */
    public static CircleFactory selectCircle(List<CircleFactory> circles, String title, String message) {
        if (circles.isEmpty()) {
            showError("No Circles", "There are no circles in the drawing.");
            return null;
        }
        
        // Create a map of circle names to circle objects
        Map<String, CircleFactory> circleMap = new HashMap<>();
        for (CircleFactory circle : circles) {
            String name = String.valueOf(circle.getName());
            circleMap.put(name, circle);
        }
        
        // Create the choice dialog
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            circleMap.keySet().iterator().next(), 
            circleMap.keySet()
        );
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        
        Optional<String> result = dialog.showAndWait();
        return result.map(circleMap::get).orElse(null);
    }
    
    /**
     * Shows a dialog for selecting start and end circles
     * @return A pair containing the start and end circles, or null if canceled
     */
    public static Pair<CircleFactory, CircleFactory> selectStartAndEndCircles(List<CircleFactory> circles) {
        if (circles.size() < 2) {
            showError("Not Enough Circles", "You need at least 2 circles to select a start and end.");
            return null;
        }
        
        // Create a map of circle names to circle objects
        Map<String, CircleFactory> circleMap = new HashMap<>();
        List<String> circleNames = new ArrayList<>();
        for (CircleFactory circle : circles) {
            String name = String.valueOf(circle.getName());
            circleMap.put(name, circle);
            circleNames.add(name);
        }
        
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Select Start and End Circles");
        dialog.setHeaderText("Please select the start and end circles for the path");
        
        // Set the button types
        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);
        
        // Create the start and end circle dropdown fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ComboBox<String> startCircleCombo = new ComboBox<>();
        startCircleCombo.getItems().addAll(circleNames);
        startCircleCombo.setValue(circleNames.get(0));
        
        ComboBox<String> endCircleCombo = new ComboBox<>();
        endCircleCombo.getItems().addAll(circleNames);
        endCircleCombo.setValue(circleNames.size() > 1 ? circleNames.get(1) : circleNames.get(0));
        
        grid.add(new Label("Start Circle:"), 0, 0);
        grid.add(startCircleCombo, 1, 0);
        grid.add(new Label("End Circle:"), 0, 1);
        grid.add(endCircleCombo, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        // Convert the result to a pair when the select button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return new Pair<>(startCircleCombo.getValue(), endCircleCombo.getValue());
            }
            return null;
        });
        
        Optional<Pair<String, String>> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            Pair<String, String> selectedNames = result.get();
            
            // Check if start and end are the same
            if (selectedNames.getKey().equals(selectedNames.getValue())) {
                showError("Invalid Selection", "Start and end circles must be different.");
                return null;
            }
            
            CircleFactory startCircle = circleMap.get(selectedNames.getKey());
            CircleFactory endCircle = circleMap.get(selectedNames.getValue());
            
            return new Pair<>(startCircle, endCircle);
        }
        
        return null;
    }
}