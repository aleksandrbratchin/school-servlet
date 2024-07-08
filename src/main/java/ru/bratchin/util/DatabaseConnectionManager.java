package ru.bratchin.util;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    public static final String PASSWORD_KEY = "db.password";
    public static final String USERNAME_KEY = "db.username";
    public static final String URL_KEY = "db.url";
    public static final String DRIVER_KEY = "db.driver";

    @Getter
    private static Connection connection;

    static {
        initializeDatabaseConnection();
    }

    private static void initializeDatabaseConnection() {
        try {
            Class.forName(PropertiesUtil.get(DRIVER_KEY));

            connection = DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
            //System.out.println("Database connection initialized successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

}
