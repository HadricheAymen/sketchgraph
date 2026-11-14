package ShapeFactory.Validation;

import ShapeFactory.CircleFactory;
import ShapeFactory.LineFactory;
import ShapeFactory.ShapeFactory;
import States.ShapeManager;

import java.util.List;

public class ValidateLine implements Validate {

    @Override
    public boolean isValid(ShapeFactory shapeFactory) {

            
        // Cast to LineFactory
        if (!(shapeFactory instanceof LineFactory)) return false;

        LineFactory line = (LineFactory) shapeFactory;

        // Check if line has zero length
        if (line.getStartX() == line.getEndX() && line.getStartY() == line.getEndY()) {
            return false;
        }

        List<ShapeFactory> instances = ShapeManager.getInstance().getShapes();

        CircleFactory circle1 = null;
        CircleFactory circle2 = null;
        double minStartDist = Double.MAX_VALUE;
        double minEndDist = Double.MAX_VALUE;

        for (ShapeFactory instance : instances) {
            if (instance instanceof CircleFactory) {
                CircleFactory circle = (CircleFactory) instance;

                double centerX = (circle.getStartX() + circle.getEndX()) / 2;
                double centerY = (circle.getStartY() + circle.getEndY()) / 2;
                double radius = Math.sqrt(Math.pow(circle.getEndX() - circle.getStartX(), 2) + 
                                         Math.pow(circle.getEndY() - circle.getStartY(), 2)) / 2;


                double distStart = distance(centerX, centerY, line.getStartX(), line.getStartY());
                double distEnd = distance(centerX, centerY, line.getEndX(), line.getEndY());


                // Find the closest circle to the start point
                if (distStart < minStartDist) {
                    minStartDist = distStart;
                    circle1 = circle;
                }
                
                // Find the closest circle to the end point
                if (distEnd < minEndDist) {
                    minEndDist = distEnd;
                    circle2 = circle;
                }
            }
        }

        if (circle1 != null && circle2 != null && circle1 != circle2) {
            line.setFirstCircle(circle1);
            line.setSecondCircle(circle2);
            return true;
        }

        return false;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}