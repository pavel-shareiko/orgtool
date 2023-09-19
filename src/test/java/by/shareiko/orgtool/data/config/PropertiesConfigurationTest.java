package by.shareiko.orgtool.data.config;

import by.shareiko.orgtool.data.config.ApplicationConfig;
import by.shareiko.orgtool.data.exception.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PropertiesConfigurationTest {

    private PropertiesConfiguration config;

    @BeforeEach
    void setUp(@TempDir File tempDir) throws IOException {
        // Create a temporary properties file for testing
        File configFile = new File(tempDir, "test.properties");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("booleanProperty=true\n");
            writer.write("stringProperty=Hello, World!\n");
        }

        config = new PropertiesConfiguration(configFile.getAbsolutePath());
    }

    @Test
    void getBoolean_withValidProperty_shouldReturnTrue() {
        Optional<Boolean> booleanValue = config.getBoolean("booleanProperty");
        assertTrue(booleanValue.isPresent());
        assertTrue(booleanValue.get());
    }

    @Test
    void getString_withValidProperty_shouldReturnExpectedString() {
        Optional<String> stringValue = config.getString("stringProperty");
        assertTrue(stringValue.isPresent());
        assertEquals("Hello, World!", stringValue.get());
    }

    @Test
    void get_withPropertyNotFound_shouldReturnEmptyOptional() {
        Optional<Boolean> booleanValue = config.getBoolean("nonExistentProperty");
        assertFalse(booleanValue.isPresent());
    }

    @Test
    void constructor_withInvalidFilePath_shouldThrowConfigurationException() {
        // Create a configuration with an invalid properties file path
        assertThrows(ConfigurationException.class, () -> new PropertiesConfiguration("nonExistentFile.properties"));
    }
}
