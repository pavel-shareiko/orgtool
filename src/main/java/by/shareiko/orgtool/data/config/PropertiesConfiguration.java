package by.shareiko.orgtool.data.config;

import by.shareiko.orgtool.data.exception.ConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class PropertiesConfiguration implements ApplicationConfig {
    private final Properties properties;

    public PropertiesConfiguration(String filePath) {
        this.properties = readPropertiesFromFile(filePath);
    }

    private Properties readPropertiesFromFile(String filePath) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
        } catch (IOException e) {
            throw new ConfigurationException("Unable to read properties from file", e);
        }

        return props;
    }

    @Override
    public Optional<Boolean> getBoolean(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return Optional.empty();
        }
        return Optional.of(Boolean.parseBoolean(value));
    }

    @Override
    public Optional<String> getString(String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }
}
