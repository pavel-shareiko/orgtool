package by.shareiko.orgtool.data;

import by.shareiko.orgtool.data.exception.DataAccessException;

import java.util.List;

/**
 * An interface for accessing data and performing operations for serialization and deserialization of objects to/from a data format.
 *
 * @param <T> The type of objects to be serialized and deserialized.
 */
public interface DataAccessor<T> {
    /**
     * Saves the specified object to a data source in a specific format.
     *
     * @param object     The object to be serialized and saved.
     * @param outputPath The path or identifier of the data source where the object should be saved.
     * @throws DataAccessException If an error occurs during the serialization or saving process.
     */
    void save(T object, String outputPath) throws DataAccessException;

    /**
     * Saves a list of objects to a data source in a specific format.
     *
     * @param objects    The list of objects to be serialized and saved.
     * @param outputPath The path or identifier of the data source where the objects should be saved.
     * @throws DataAccessException If an error occurs during the serialization or saving process.
     */
    void saveAll(List<T> objects, String outputPath) throws DataAccessException;

    /**
     * Reads and deserializes a list of objects from a data source in a specific format.
     *
     * @param source The path or identifier of the data source from which objects should be read and deserialized.
     * @return A list of deserialized objects.
     * @throws DataAccessException If an error occurs during the reading or deserialization process.
     */
    List<T> readAll(String source) throws DataAccessException;
}

