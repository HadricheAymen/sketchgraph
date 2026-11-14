package ShapeFactory.AdjustCoordinates;

import ShapeFactory.*;

public class AdjustLine implements AdjustCoordinates{
    @Override
    public void AdjustCoordinates(ShapeFactory shapeFactory) {
        CircleFactory circle1 = ((LineFactory)shapeFactory).getFirstCircle();
        CircleFactory circle2 = ((LineFactory)shapeFactory).getSecondCircle();
        if (circle1 != null && circle2 != null && circle1 != circle2) {
            System.out.println("Line connects two different circles - VALID");

            // Calculate circle centers and radii
            double center1X = (circle1.getStartX() + circle1.getEndX()) / 2;
            double center1Y = (circle1.getStartY() + circle1.getEndY()) / 2;
            double radius1 = Math.sqrt(Math.pow(circle1.getEndX() - circle1.getStartX(), 2) +
                    Math.pow(circle1.getEndY() - circle1.getStartY(), 2)) / 2;

            double center2X = (circle2.getStartX() + circle2.getEndX()) / 2;
            double center2Y = (circle2.getStartY() + circle2.getEndY()) / 2;
            double radius2 = Math.sqrt(Math.pow(circle2.getEndX() - circle2.getStartX(), 2) +
                    Math.pow(circle2.getEndY() - circle2.getStartY(), 2)) / 2;

            // Calculate line direction vector
            double dirX = shapeFactory.getEndX() - shapeFactory.getStartX();
            double dirY = shapeFactory.getEndY() - shapeFactory.getStartY();
            double length = Math.sqrt(dirX * dirX + dirY * dirY);
            dirX /= length;
            dirY /= length;

            // Calculate new start point (on circle1's edge)
            double newStartX = center1X + dirX * radius1;
            double newStartY = center1Y + dirY * radius1;

            // Calculate new end point (on circle2's edge)
            double newEndX = center2X - dirX * radius2;
            double newEndY = center2Y - dirY * radius2;

            // Update line coordinates
            shapeFactory.setCoordinates(newStartX, newStartY, newEndX, newEndY);

            System.out.println("Adjusted line: " +
                    "Start(" + newStartX + "," + newStartY + "), " +
                    "End(" + newEndX + "," + newEndY + ")");

            ((LineFactory) shapeFactory).setFirstCircle(circle1);
            ((LineFactory) shapeFactory).setSecondCircle(circle2);
        }
    }
}
