package by.shareiko.orgtool.data.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationExceptionTest {

    @Test
    void constructor_withMessage_shouldSetMessage() {
        String message = "Test Configuration Exception";
        ConfigurationException exception = new ConfigurationException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_withMessageAndCause_shouldSetMessageAndCause() {
        String message = "Test Configuration Exception";
        Exception cause = new Exception("Test Cause");
        ConfigurationException exception = new ConfigurationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
