package by.shareiko.orgtool.data.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessExceptionTest {

    @Test
    void constructor_withMessage_shouldSetMessage() {
        String message = "Test Data Access Exception";
        DataAccessException exception = new DataAccessException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_withMessageAndCause_shouldSetMessageAndCause() {
        String message = "Test Data Access Exception";
        Exception cause = new Exception("Test Cause");
        DataAccessException exception = new DataAccessException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
