package Zone;

import Providers.StyleProvider;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Vertical toolbar that appears on the left side of the application
 */
public class SidebarZone extends VBox {
    private final Button restoreButton;
    private final Button clearButton;
    private final Button mstButton;
    private final Button shortestPathButton;
    private final Button tspButton;
    private final ColorPicker colorPicker;
    private final Button executeButton;
    private final ComboBox<Integer> lineWidthPicker;
    
    public SidebarZone(Runnable onRestoreClick,
                       Runnable onClearClick,
                       Runnable onMSTClick,
                       Runnable onShortestPathClick,
                       Runnable onTSPClick,
                       Runnable onExecuteClick
    ) {


        // Title
        Label titleLabel = new Label("Algorithm");
        titleLabel.setStyle("-fx-font-weight: bold;");


        
        // Separator
        Separator separator1 = new Separator(Orientation.HORIZONTAL);

        mstButton = new Button("Minumum Spanning Tree");
        mstButton.setPrefWidth(130);
        mstButton.setOnAction(e -> onMSTClick.run());
        shortestPathButton = new Button("Shortest Path");
        shortestPathButton.setPrefWidth(130);
        shortestPathButton.setOnAction(e -> onShortestPathClick.run());
        tspButton = new Button("Traveling Salesman");
        tspButton.setPrefWidth(130);
        tspButton.setOnAction(e -> onTSPClick.run());

        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> {
            Color selectedColor = colorPicker.getValue();
            StyleProvider.getInstance().setHighlightedColor(selectedColor);
        });

        lineWidthPicker = new ComboBox<>();
        lineWidthPicker.getItems().addAll(1, 2, 3, 4, 5, 10); // Valeurs possibles
        lineWidthPicker.setValue(2); // Valeur par défaut
        lineWidthPicker.setOnAction(e -> {
            int selectedWidth = lineWidthPicker.getValue();
            StyleProvider.getInstance().setHiglightedLineWidth(selectedWidth);
        });

        executeButton = new Button("Execute");
        executeButton.setPrefWidth(130);
        executeButton.setOnAction(e -> onExecuteClick.run());

        
        // Separator
        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        
        // Actions
        Label actionsLabel = new Label("Actions:");
        actionsLabel.setStyle("-fx-font-weight: bold;");

        restoreButton = new Button("Restore Graph");
        restoreButton.setPrefWidth(130);
        restoreButton.setOnAction(e -> onRestoreClick.run());
        
        clearButton = new Button("Clear All");
        clearButton.setPrefWidth(130);
        clearButton.setOnAction(e -> onClearClick.run());
        
        // Add all components to the sidebar
        this.getChildren().addAll(
            titleLabel, separator1,
            mstButton,shortestPathButton,tspButton,colorPicker,lineWidthPicker, executeButton,
            separator2, actionsLabel,
                restoreButton, clearButton
        );
    }
    

    }
