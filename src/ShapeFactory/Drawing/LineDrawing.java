package ShapeFactory.Drawing;

import ShapeFactory.*;
import Providers.GraphicsContextProvider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class LineDrawing implements Drawing {
    GraphicsContext gc = GraphicsContextProvider.getGraphicsContext();
    
    @Override
    public void draw(ShapeFactory shapeFactory) {
        double startX = shapeFactory.getStartX();
        double startY = shapeFactory.getStartY();
        double endX = shapeFactory.getEndX();
        double endY = shapeFactory.getEndY();

        // Draw the line
        gc.strokeLine(startX, startY, endX, endY);

        // Get the line value
        String value = String.valueOf(((LineFactory)shapeFactory).getValue());

        // Calculate center point of the line for text placement
        double centerX = (startX + endX) / 2;
        double centerY = (startY + endY) / 2;

        // Calculate line angle and determine if it's vertical
        double dx = endX - startX;
        double dy = endY - startY;
        double lineLength = Math.sqrt(dx * dx + dy * dy);
        boolean isVertical = Math.abs(dx) < 10; // Line is considered vertical if x difference is small
        
        // Configure text properties
        gc.setTextAlign(TextAlignment.CENTER);

        // Calculate font size (can be fixed or based on line length)
        double fontSize = Math.max(12, Math.min(lineLength / 4, 24));
        gc.setFont(new Font(gc.getFont().getName(), fontSize));

        // Draw the value with appropriate offset based on line orientation
        if (isVertical) {
            // For vertical lines, offset the text horizontally
            double textOffsetX = fontSize + 5; // Offset to position text to the right of the line
            gc.fillText(value, centerX + textOffsetX, centerY);
        } else {
            // For non-vertical lines, offset the text vertically
            double textOffsetY = 15; // Offset to position text above the line
            gc.fillText(value, centerX, centerY - textOffsetY);
        }
    }
}