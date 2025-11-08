package app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
    private static final String URL = "jdbc:postgresql://localhost:5432/budget_bd";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection get() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Postgres driver not found", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
