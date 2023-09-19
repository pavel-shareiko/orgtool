package by.shareiko.orgtool.data.exception;

public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Exception cause) {
        super(message, cause);
    }
}
