package by.shareiko.orgtool.data.config;

import java.util.Optional;

/**
 * The {@code ApplicationConfig} interface provides methods for retrieving configuration
 * settings in the form of optional values.
 *
 * <p>Implementations of this interface allow applications to access configuration settings
 * by specifying a key and obtaining an optional value. If a configuration setting for the
 * given key exists, the optional will contain the value; otherwise, it will be empty.
 *
 * <p>Configuration settings can be of various types, such as strings, booleans, integers, etc.
 * This interface defines methods for retrieving string and boolean configuration values.
 *
 * @see Optional
 */
public interface ApplicationConfig {

    /**
     * Retrieves an optional string configuration value associated with the specified key.
     *
     * @param key The key of the configuration setting to retrieve.
     * @return An optional containing the string value if the setting exists; otherwise, an empty optional.
     */
    Optional<String> getString(String key);

    /**
     * Retrieves an optional boolean configuration value associated with the specified key.
     *
     * @param key The key of the configuration setting to retrieve.
     * @return An optional containing the boolean value if the setting exists; otherwise, an empty optional.
     */
    Optional<Boolean> getBoolean(String key);
}