package ShapeFactory.Validation;

import ShapeFactory.*;
import States.ShapeManager;

import java.util.List;

public class ValidateCercle implements Validate {

    @Override
    public boolean isValid(ShapeFactory shapeFactory) {
        
        // First check if it's a zero-size circle (same start and end points)
        if (shapeFactory.getStartX() == shapeFactory.getEndX() && 
            shapeFactory.getStartY() == shapeFactory.getEndY()) {
            return false;
        }
        
        // Check if radius is too small
        double startX = shapeFactory.getStartX();
        double startY = shapeFactory.getStartY();
        double endX = shapeFactory.getEndX();
        double endY = shapeFactory.getEndY();
        double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) / 2;
        
        if (radius < 10) {
            return false;
        }
        
        // Then check for duplicate names
        if (shapeFactory instanceof CircleFactory) {
            CircleFactory newCircle = (CircleFactory) shapeFactory;
            char newName = newCircle.getName();
            
            List<ShapeFactory> shapes = ShapeManager.getInstance().getShapes();
            for (ShapeFactory shape : shapes) {
                if (shape instanceof CircleFactory) {
                    CircleFactory existingCircle = (CircleFactory) shape;
                    if (existingCircle.getName() == newName) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}