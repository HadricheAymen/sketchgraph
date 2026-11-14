import Algorithm.*;
import Providers.StyleProvider;
import ShapeFactory.*;
import States.ShapeManager;
import Zone.CanvasZone;
import Zone.SidebarZone;
import Zone.ToolbarZone;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dessin de Formes Géométriques");
        StyleProvider styleProvider = StyleProvider.getInstance();
        CanvasZone canvasZone = CanvasZone.getInstance(800, 600);

        ShapeManager shapeManager = ShapeManager.getInstance();
        shapeManager.addObserver(canvasZone);
        shapeManager.setCurrentFactory(new CircleFactory()); // default shape

        ToolbarZone toolbar = new ToolbarZone(
                () -> shapeManager.setCurrentFactory(new CircleFactory()),
                () -> shapeManager.setCurrentFactory(new LineFactory()),
                () -> shapeManager.removeLastShape()
        );

        SidebarZone sidebar = new SidebarZone(
                () -> shapeManager.restoreShapes(canvasZone.getHeight(),canvasZone.getWidth()),
                () -> {
                    shapeManager.clearShapes();
                    canvasZone.getGraphicsContext2D().clearRect(0, 0, canvasZone.getWidth(), canvasZone.getHeight());
                },
                () -> AlgorithmManager.getInstance().setAlgorithm(new MinimumSpanningTreeAlgorithm()),
                () -> AlgorithmManager.getInstance().setAlgorithm(new ShortestPathAlgorithm()),
                () -> AlgorithmManager.getInstance().setAlgorithm(new TravelingSalesmanAlgorithm()),
                () -> AlgorithmManager.getInstance().Execute()
        );
        
        // Create the main layout
        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setLeft(sidebar);
        root.setCenter(canvasZone);
        
        // Create and show the scene
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
