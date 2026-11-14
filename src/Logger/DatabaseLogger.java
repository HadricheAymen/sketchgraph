package Logger;

import ShapeFactory.ShapeFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DatabaseLogger implements LoggerStrategy {
    private DatabaseConnection dbConnection;

    public DatabaseLogger() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public void log(ShapeFactory shapeFactory, String actiontype,String actionStatus) {
        if (dbConnection.getConnection() == null) {
            // No JDBC connection: the sink was left unconfigured, so logging is skipped.
            return;
        }
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String sql = "INSERT INTO logs (timestamp,startX, startY, endX, endY, shapeType, actiontype,status) " +
                    "VALUES (?, ?,?,?, ?, ?, ?,?)";
            PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql);
            double startX = shapeFactory.getStartX();
            double startY = shapeFactory.getStartY();
            double endX = shapeFactory.getEndX();
            double endY = shapeFactory.getEndY();
            String shapeType = shapeFactory.getShapeName();
            stmt.setString(1, timestamp);
            stmt.setDouble(2, startX);
            stmt.setDouble(3, startY);
            stmt.setDouble(4, endX);
            stmt.setDouble(5, endY);
            stmt.setString(6, shapeType);
            stmt.setString(7, actiontype);
            stmt.setString(8, actionStatus);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging to database: " + e.getMessage());
        }
    }
}
