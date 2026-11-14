package Decorators;

import Providers.GraphicsContextProvider;
import ShapeFactory.ShapeFactory;
import javafx.scene.canvas.GraphicsContext;

public class ThicknessDecorator  extends ShapeDecorator{
    private double linewidth;

    public ThicknessDecorator(ShapeFactory decoratedShape, double linewidth) {
        super(decoratedShape);
        this.linewidth= linewidth;
    }

    @Override
    public void draw() {
        GraphicsContext gc = GraphicsContextProvider.getGraphicsContext();
        double originalLineWidth = gc.getLineWidth();

        gc.setLineWidth(linewidth);

        decoratedShape.draw();

        gc.setLineWidth(originalLineWidth);
    }
}
