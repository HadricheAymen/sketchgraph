package Providers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GraphicsContextProvider {
    private static GraphicsContextProvider instance;
    private static GraphicsContext gc;

    private GraphicsContextProvider(GraphicsContext gc) {
        this.gc=gc;
    }
    private GraphicsContextProvider(){}

    public static synchronized GraphicsContextProvider getInstance(Canvas canvas) {
        if (instance == null)
             instance = new GraphicsContextProvider(canvas.getGraphicsContext2D());
        return instance;
    }
    public static synchronized GraphicsContextProvider getInstance() {
        if (instance == null)
            instance = new GraphicsContextProvider();
        return instance;
    }
    public static GraphicsContext getGraphicsContext() {
        return gc;
    }
}
