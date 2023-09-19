package by.shareiko.orgtool.data.access;

import by.shareiko.orgtool.data.config.ApplicationConfig;
import by.shareiko.orgtool.data.exception.DataAccessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class XmlDataAccessorTest {
    private static final String TEST_XML_PATH = "src/test/resources/bob.xml";
    private Map<String, Object> defaultConfig = getDefaultConfig();
    private XmlDataAccessor<TestEntity> dataAccessor;
    private String outputPath;

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(XmlDataAccessor.ALLOW_FILE_OVERWRITES, true);
        config.put(XmlDataAccessor.FILE_ENCODING, "UTF-8");
        config.put(XmlDataAccessor.FORMAT_OUTPUT, true);

        return config;
    }

    @BeforeEach
    void setUp() throws IOException {
        // Initialize XmlDataAccessor with a test entity and a mock ApplicationConfig
        dataAccessor = XmlDataAccessor.forType(TestEntity.class, new MockApplicationConfig(defaultConfig));
        Path outputFile = Files.createTempFile("test", ".xml");
        outputPath = outputFile.toAbsolutePath().toString();

        // we only need the path to the temp file, not the file itself
        Files.delete(outputFile);
    }

    @Test
    void read_shouldReturnObject() {
        // Create a test object to save
        TestEntity expectedObject = new TestEntity("Bob", 25);

        // Read the object from XML
        TestEntity readObject = dataAccessor.read(TEST_XML_PATH);

        // Check if the read object matches the original object
        assertEquals(expectedObject, readObject);
    }

    @Test
    void read_withNullConfig_shouldReturnObject() {
        // Don't pass config for this test
        dataAccessor = XmlDataAccessor.forType(TestEntity.class);

        // Create a test object to save
        TestEntity expectedObject = new TestEntity("Bob", 25);

        // Read the object from XML
        TestEntity readObject = dataAccessor.read(TEST_XML_PATH);

        // Check if the read object matches the original object
        assertEquals(expectedObject, readObject);
    }

    @Test
    void read_requestedFileDoesNotExist_shouldThrowDataAccessException() {
        // Attempt to read a non-existent file
        assertThrows(DataAccessException.class, () -> dataAccessor.read("nonexistent.xml"));
    }

    @Test
    void saveAndReadObject_writtenAndReadValuesShouldMatch() {
        // Create a test object to save
        TestEntity testObject = new TestEntity("John", 30);

        // Save the object to XML
        dataAccessor.save(testObject, outputPath);

        // Read the object from XML
        TestEntity readObject = dataAccessor.read(outputPath);

        // Check if the read object matches the original object
        assertEquals(testObject, readObject);
    }

    @Test
    void saveWithFileOverwrite_shouldReplaceFirstValue() {
        // Create a test object to save
        TestEntity testObject = new TestEntity("Alice", 25);

        // Save the object to XML once
        dataAccessor.save(testObject, outputPath);

        // Attempt to save the object again with file overwrite
        TestEntity newObject = new TestEntity("Bob", 26);
        dataAccessor.save(newObject, outputPath);

        // Read the object from XML
        TestEntity readObject = dataAccessor.read(outputPath);

        // Check if the read object matches the original object
        assertEquals(newObject, readObject);
    }

    @Test
    void saveWithoutFileOverwrite_shouldThrowDataAccessException() {
        // Disable file overwrite for this test
        Map<String, Object> config = getDefaultConfig();
        config.put(XmlDataAccessor.ALLOW_FILE_OVERWRITES, false);
        this.dataAccessor = XmlDataAccessor.forType(TestEntity.class, new MockApplicationConfig(config));

        // Create a test object to save
        TestEntity testObject = new TestEntity("Bob", 35);

        // Save the object to XML once
        dataAccessor.save(testObject, outputPath);

        // Attempt to save the object again without file overwrite
        assertThrows(DataAccessException.class, () -> dataAccessor.save(testObject, outputPath));

        // Read the object from XML
        TestEntity readObject = dataAccessor.read(outputPath);

        // Check if the read object still matches the original object
        assertEquals(testObject, readObject);
    }

    // Define a mock ApplicationConfig for testing
    private static class MockApplicationConfig implements ApplicationConfig {
        private final Map<String, Object> config;

        public MockApplicationConfig(Map<String, Object> defaultConfig) {
            this.config = defaultConfig;
        }

        @Override
        public Optional<Boolean> getBoolean(String key) {
            if (config.containsKey(key)) {
                return Optional.of((Boolean) config.get(key));
            }
            return Optional.empty();
        }

        @Override
        public Optional<String> getString(String key) {
            if (config.containsKey(key)) {
                return Optional.of((String) config.get(key));
            }
            return Optional.empty();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "testEntity")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class TestEntity {
        @XmlElement
        private String name;
        @XmlElement
        private int age;
    }
}
