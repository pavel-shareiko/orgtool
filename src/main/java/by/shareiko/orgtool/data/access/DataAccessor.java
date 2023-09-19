package by.shareiko.orgtool.data.access;

import by.shareiko.orgtool.data.exception.DataAccessException;

/**
 * An interface for accessing data and performing operations for serialization and deserialization of objects to/from a data format.
 *
 * @param <T> The type of objects to be serialized and deserialized.
 */
public interface DataAccessor<T> {
    /**
     * Saves the specified object to a data source in a specific format.
     * The actual format is decided by the implementation
     *
     * @param object     The object to be serialized and saved.
     * @param outputPath The path or identifier of the data source where the object should be saved.
     * @throws DataAccessException If an error occurs during the serialization or saving process.
     */
    void save(T object, String outputPath) throws DataAccessException;

    /**
     * Reads and deserializes a list of objects from a data source in a specific format.
     *
     * @param source The path or identifier of the data source from which objects should be read and deserialized.
     * @return A deserialized object.
     * @throws DataAccessException If an error occurs during the reading or deserialization process.
     */
    T read(String source) throws DataAccessException;
}

