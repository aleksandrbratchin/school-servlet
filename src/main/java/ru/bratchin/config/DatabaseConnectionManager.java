package ru.bratchin.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {

    private static Connection connection;

    static {
        initializeDatabaseConnection();
    }

    private static void initializeDatabaseConnection() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find database.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load database properties", ex);
        }

        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            Class.forName(driver); // Загрузка драйвера JDBC

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection initialized successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}
