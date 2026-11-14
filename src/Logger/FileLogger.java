package Logger;

import ShapeFactory.ShapeFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger implements LoggerStrategy {
    private final String filename = "log_file.txt";

    @Override
    public void log(ShapeFactory shapeFactory, String actiontype,String actionStatus) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            double startX = shapeFactory.getStartX();
            double startY = shapeFactory.getStartY();
            double endX = shapeFactory.getEndX();
            double endY = shapeFactory.getEndY();
            String shapeType = shapeFactory.getShapeName();
            String coordinates =" Start: (" + startX + ", " + startY + "), End: (" + endX + ", " + endY + ")" ;
            String action = actiontype + " " + shapeType;
            String status = "Status: " + actionStatus;
            writer.write("[" + timestamp + "] [" + coordinates + "] [" + action + "] [" + status + "]\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}

