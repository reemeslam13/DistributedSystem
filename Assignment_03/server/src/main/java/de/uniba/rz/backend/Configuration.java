package de.uniba.rz.backend;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());

    public static Properties loadProperties() {
        try (InputStream stream = RESTApi.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (Exception e) {
        	System.out.print("******"+e.getMessage());
            LOGGER.severe("Cannot load configuration file.");
            return null;
        }
    }
}
