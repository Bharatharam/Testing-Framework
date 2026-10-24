package com.framework.qa.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties;
    private static final String environment;

    static {
        environment = System.getProperty("env", "qa");
        String propertiesFilePath = String.format("src/test/resources/%s.properties", environment);
        properties = new Properties();
        Path path = Paths.get(propertiesFilePath);
        if (!Files.exists(path)) {
            if (!environment.equals("qa")) {
                System.out.println(
                        "[Config] Environment '"
                                + environment
                                + "' file not found. Falling back to qa.properties");
            }
            propertiesFilePath = "src/test/resources/qa.properties";
        }
        try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
            properties.load(fis);
            System.out.println(
                    "[Config] Loaded configuration for env: "
                            + environment
                            + " from file: "
                            + propertiesFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not read properties file: " + propertiesFilePath, e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Property not found: " + key);
        return value;
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String getEnvironment() {
        return environment;
    }
}