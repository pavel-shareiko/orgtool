package by.shareiko.orgtool.data.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Exception cause) {
        super(message, cause);
    }
}
