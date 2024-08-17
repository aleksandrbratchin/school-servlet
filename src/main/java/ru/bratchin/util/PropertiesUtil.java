package ru.bratchin.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadPropertiesUtil();
    }

    private PropertiesUtil() {
    }

    public static String get(String key){
        return PROPERTIES.getProperty(key);
    }

    private static void loadPropertiesUtil() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (Exception e){
            throw new RuntimeException("Sorry, unable to find application.properties");
        }
    }
}
