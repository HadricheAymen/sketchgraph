package Decorators;

import ShapeFactory.ShapeFactory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ColorDecorator extends ShapeDecorator{
    private Color color;

    public ColorDecorator(ShapeFactory decoratedShape, Color color) {
        super(decoratedShape);
        this.color = color;
    }

    @Override
    public void draw() {
        // Set the color before drawing
        GraphicsContext gc =Providers.GraphicsContextProvider.getGraphicsContext();
        Color originalColor =(Color) gc.getStroke();

        gc.setStroke(color);

        decoratedShape.draw();

        gc.setStroke(originalColor);
    }
}
