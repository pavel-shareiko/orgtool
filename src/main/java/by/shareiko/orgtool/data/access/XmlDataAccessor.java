package by.shareiko.orgtool.data.access;

import by.shareiko.orgtool.data.config.DataAccessConfig;
import by.shareiko.orgtool.data.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import java.io.File;
import java.util.Optional;

public class XmlDataAccessor<T> implements DataAccessor<T> {
    private static final Logger LOG = LoggerFactory.getLogger(XmlDataAccessor.class);
    public static final String FILE_ENCODING = "data.encoding";
    public static final String FORMAT_OUTPUT = "data.format-output";
    public static final String ALLOW_FILE_OVERWRITES = "data.allow-file-overwrites";
    private final Class<T> entityType;
    private final DataAccessConfig dataAccessConfig;

    private XmlDataAccessor(Class<T> entityType, DataAccessConfig dataAccessConfig) {
        this.entityType = entityType;
        this.dataAccessConfig = dataAccessConfig == null
                ? new NullDataAccessConfig()
                : dataAccessConfig;
    }

    public static <T> XmlDataAccessor<T> forType(Class<T> entityType) {
        return new XmlDataAccessor<>(entityType, null);
    }

    public static <T> XmlDataAccessor<T> forType(Class<T> entityType, DataAccessConfig dataAccessConfig) {
        return new XmlDataAccessor<>(entityType, dataAccessConfig);
    }

    @Override
    public void save(T object, String outputPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Serializing object to XML file '{}': {}", object, outputPath);
        }

        try {
            Marshaller marshaller = createMarshaller(object);
            doSave(object, outputPath, marshaller);
        } catch (JAXBException e) {
            LOG.error("An unexpected JAXB error occurred while accessing the file", e);
            throw new DataAccessException("Failed to save object because of underlying JAXB error", e);
        }
    }

    @Override
    public T read(String path) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading a collection of objects from file '{}'", path);
        }

        try {
            return doRead(path);
        } catch (JAXBException e) {
            LOG.error("An unexpected JAXB error occurred while reading data from file", e);
            throw new DataAccessException("Failed to read object because of underlying JAXB error", e);
        }
    }

    private Marshaller createMarshaller(Object object) throws JAXBException {
        Class<?> requiredClass = object.getClass();
        JAXBContext jaxbContext = createJaxbContextForClass(requiredClass);
        Marshaller marshaller = jaxbContext.createMarshaller();

        return configureMarshaller(marshaller);
    }

    private Unmarshaller createUnmarshaller() throws JAXBException {
        JAXBContext jaxbContext = createJaxbContextForClass(entityType);
        return jaxbContext.createUnmarshaller();
    }

    private JAXBContext createJaxbContextForClass(Class<?> requiredClass) throws JAXBException {
        return JAXBContext.newInstance(requiredClass);
    }

    private Marshaller configureMarshaller(Marshaller marshaller) throws PropertyException {
        Optional<String> fileEncoding = dataAccessConfig.getString(FILE_ENCODING);
        if (fileEncoding.isPresent()) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, fileEncoding.get());
        }

        Optional<Boolean> formatOutput = dataAccessConfig.getBoolean(FORMAT_OUTPUT);
        if (formatOutput.isPresent()) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatOutput.get());
        }
        return marshaller;
    }

    private void doSave(Object object, String outputPath, Marshaller marshaller) throws JAXBException {
        File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            Optional<Boolean> allowFileOverwrites = dataAccessConfig.getBoolean(ALLOW_FILE_OVERWRITES);
            if (!allowFileOverwrites.isPresent() || !allowFileOverwrites.get()) {
                throw new DataAccessException("Unable to save data to file, because it is already exists." +
                        "If you want to replace the existing file, " +
                        "specify the " + ALLOW_FILE_OVERWRITES + " property in the configuration");
            }
        }
        marshaller.marshal(object, outputFile);
    }

    private T doRead(String path) throws JAXBException {
        Unmarshaller unmarshaller = createUnmarshaller();

        File contentFile = new File(path);
        if (!contentFile.exists()) {
            throw new DataAccessException("Unable to read file '" + contentFile + "' because it doesn't exist");
        }

        return (T) unmarshaller.unmarshal(contentFile);
    }

    private static class NullDataAccessConfig implements DataAccessConfig {
        @Override
        public Optional<Boolean> getBoolean(String key) {
            return Optional.empty();
        }

        @Override
        public Optional<String> getString(String key) {
            return Optional.empty();
        }
    }
}
