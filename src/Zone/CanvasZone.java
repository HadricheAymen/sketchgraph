package Zone;

import Logger.Logger;
import ShapeFactory.*;
import Providers.GraphicsContextProvider;
import States.Observable;
import States.Observer;
import States.ShapeManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasZone extends Canvas implements Observer {
    private static CanvasZone instance;
    private static GraphicsContext graphicsContext;
    private double startX, startY;
    private ShapeFactory currentFactory;

    private CanvasZone(double width, double height) {
        super(width, height);
        graphicsContext = GraphicsContextProvider.getInstance(this).getGraphicsContext();
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2);

        // Handle mouse press
        this.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
        });

        // Handle mouse release and draw shape
        this.setOnMouseReleased(e -> {
            if (currentFactory != null) {
                currentFactory.setCoordinates(startX, startY, e.getX(), e.getY());
                currentFactory.setShapeData();
                if (currentFactory.Validate()) {
                    currentFactory.AdjustCoordinates();
                    currentFactory.draw();
                    ShapeManager.getInstance().addShape(currentFactory);
                    Logger.getInstance().log(currentFactory, "Drawing", "Success");
                } else {
                    Logger.getInstance().log(currentFactory, "Drawing", "Failed");
                }
            }
            if (currentFactory instanceof CircleFactory) {
                ShapeManager.getInstance().setCurrentFactory(new CircleFactory());
            }
            else ShapeManager.getInstance().setCurrentFactory(new LineFactory());
        });
    }

    public static synchronized CanvasZone getInstance(double width, double height) {
        if (instance == null) {
            instance = new CanvasZone(width, height);
        }
        return instance;
    }

    @Override
    public void update(Observable observable) {
        if (observable instanceof ShapeManager)
            currentFactory =((ShapeManager) observable).getCurrentFactory();
    }
}