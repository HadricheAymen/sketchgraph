package Zone;

import States.ShapeManager;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import Logger.*;

public class ToolbarZone extends HBox {
    private final Button circleBtn;
    private final Button lineBtn;
    private MenuButton logMenu;
    private final Button undoBtn;


    public ToolbarZone(Runnable onCircleClick, Runnable onLineClick,Runnable onUndoClick) {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        circleBtn = new Button("Cercle");
        lineBtn = new Button("Ligne");
        undoBtn = new Button("Undo");

        MenuItem logMenuItem = new MenuItem("ConsoleLog");
        MenuItem fileMenuItem = new MenuItem("FileLog");
        MenuItem databaseMenuItem = new MenuItem("DatabaseLog");
        logMenuItem.setOnAction(e -> Logger.getInstance().setStrategy(new ConsoleLogger()));
        fileMenuItem.setOnAction(e -> Logger.getInstance().setStrategy(new FileLogger()));
        databaseMenuItem.setOnAction(e -> Logger.getInstance().setStrategy(new DatabaseLogger()));

        MenuButton logMenu = new MenuButton("Log", null, logMenuItem, fileMenuItem, databaseMenuItem);



        circleBtn.setOnAction(e -> onCircleClick.run());
        lineBtn.setOnAction(e -> onLineClick.run());
        undoBtn.setOnAction(e -> onUndoClick.run());

        this.getChildren().addAll(circleBtn, lineBtn, logMenu,undoBtn);
    }
}

