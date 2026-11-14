package ShapeFactory.Drawing;

import ShapeFactory.*;
import Providers.GraphicsContextProvider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

public class CircleDrawing implements Drawing {
    GraphicsContext gc = GraphicsContextProvider.getGraphicsContext();
    @Override
    public void draw(ShapeFactory shapeFactory) {
        double startX = shapeFactory.getStartX();
        double startY = shapeFactory.getStartY();
        double endX = shapeFactory.getEndX();
        double endY = shapeFactory.getEndY();

        // Calculate center and radius
        double centerX = (startX + endX) / 2;
        double centerY = (startY + endY) / 2;
        double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) / 2;


        // Draw the circle
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Get the circle name
        String name = String.valueOf(((CircleFactory)shapeFactory).getName());

        // Configure text properties
        gc.setTextAlign(TextAlignment.CENTER);

        // Calculate font size based on circle size (adjust as needed)
        double fontSize = Math.max(12, Math.min(radius / 2, 24));
        gc.setFont(new Font(gc.getFont().getName(), fontSize));

        // Draw the text centered in the circle
        gc.fillText(name, centerX, centerY + fontSize/4);
    }
}
