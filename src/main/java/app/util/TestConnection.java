package app.util;

import app.database.DatabaseConnect;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnect.get()) {
            System.out.println("Connection successful: " + (conn != null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
