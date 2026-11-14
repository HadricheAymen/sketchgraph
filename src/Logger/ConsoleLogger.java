package Logger;

import ShapeFactory.ShapeFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger implements LoggerStrategy {
    @Override
    public void log(ShapeFactory shapeFactory, String actiontype,String actionStatus) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        double startX = shapeFactory.getStartX();
        double startY = shapeFactory.getStartY();
        double endX = shapeFactory.getEndX();
        double endY = shapeFactory.getEndY();
        String shapeType = shapeFactory.getShapeName();
        String coordinates =" Start: (" + startX + ", " + startY + "), End: (" + endX + ", " + endY + ")" ;
        String action = actiontype + " " + shapeType;
        String status = "Status: " + actionStatus;
        String message = "[" + coordinates + "] [" + action + "] [" + status + "]";
        System.out.println("[" + timestamp + "] " + message);
    }
}
