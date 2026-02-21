package utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static Properties prop = new Properties();

    static {
        try {
            InputStream input = PropertyReader.class
                    .getClassLoader()
                    .getResourceAsStream("Global.properties");

            if (input == null) {
                throw new RuntimeException("Global.properties NOT found in classpath");
            }

            prop.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load Global.properties file");
        }
    }

    public static String getGlobalProperty(String key) {
        return prop.getProperty(key);
    }
}