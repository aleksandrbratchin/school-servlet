package ru.bratchin.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static final String PASSWORD_KEY = "db.password";
    public static final String USERNAME_KEY = "db.username";
    public static final String URL_KEY = "db.url";
    public static final String DRIVER_KEY = "db.driver";

    static {
        config.setJdbcUrl( PropertiesUtil.get(URL_KEY) );
        config.setUsername( PropertiesUtil.get(USERNAME_KEY) );
        config.setPassword( PropertiesUtil.get(PASSWORD_KEY) );
        config.setDriverClassName(PropertiesUtil.get(DRIVER_KEY));
        config.setMaximumPoolSize(10);
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
