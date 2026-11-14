package Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Shared JDBC connection used by the DatabaseLog sink.
 *
 * Connection settings are read from the standard libpq environment variables so
 * that no credential is ever stored in the repository:
 * PGHOST, PGPORT, PGDATABASE, PGUSER, PGPASSWORD.
 * PGPASSWORD has no default: without it the sink stays disabled and the rest of
 * the application runs normally.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        String host = config("PGHOST", "localhost");
        String port = config("PGPORT", "5432");
        String database = config("PGDATABASE", "logs");
        String user = config("PGUSER", "postgres");
        String password = config("PGPASSWORD", null);

        if (password == null) {
            System.err.println("DatabaseLog disabled: set the PGPASSWORD environment variable "
                    + "(optional: PGHOST, PGPORT, PGDATABASE, PGUSER).");
            return;
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
            connection = DriverManager.getConnection(url, user, password);
            String sql = "CREATE TABLE IF NOT EXISTS logs (" +
                    "id SERIAL PRIMARY KEY, " +
                    "timestamp VARCHAR(50), " +
                    "startX DOUBLE PRECISION, " +
                    "startY DOUBLE PRECISION, " +
                    "endX DOUBLE PRECISION, " +
                    "endY DOUBLE PRECISION, " +
                    "shapeType VARCHAR(50), " +
                    "actionType VARCHAR(50), " +
                    "status VARCHAR(50)" +
                    ")";
            connection.createStatement().execute(sql);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    private static String config(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}