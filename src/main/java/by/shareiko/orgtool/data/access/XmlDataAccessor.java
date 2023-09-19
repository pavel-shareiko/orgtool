package by.shareiko.orgtool.data.access;

import by.shareiko.orgtool.data.config.ApplicationConfig;
import by.shareiko.orgtool.data.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import java.io.File;
import java.util.Optional;

public class XmlDataAccessor<T> implements DataAccessor<T> {
    public static final String FILE_ENCODING = "data.encoding";
    public static final String FORMAT_OUTPUT = "data.format-output";
    public static final String ALLOW_FILE_OVERWRITES = "data.allow-file-overwrites";
    private static final Logger LOG = LoggerFactory.getLogger(XmlDataAccessor.class);
    private final Class<T> entityType;
    private final ApplicationConfig applicationConfig;

    private XmlDataAccessor(Class<T> entityType, ApplicationConfig applicationConfig) {
        this.entityType = entityType;
        this.applicationConfig = applicationConfig;
    }

    public static <T> XmlDataAccessor<T> forType(Class<T> entityType) {
        return new XmlDataAccessor<>(entityType, new NullApplicationConfig());
    }

    public static <T> XmlDataAccessor<T> forType(Class<T> entityType, ApplicationConfig applicationConfig) {
        return new XmlDataAccessor<>(entityType, applicationConfig);
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
        // apply marshaller configuration from data access config
        Optional<String> fileEncoding = applicationConfig.getString(FILE_ENCODING);
        if (fileEncoding.isPresent()) {
            marshaller.setProperty(Marshaller.JAXB_ENCODING, fileEncoding.get());
        }

        Optional<Boolean> formatOutput = applicationConfig.getBoolean(FORMAT_OUTPUT);
        if (formatOutput.isPresent()) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatOutput.get());
        }
        return marshaller;
    }

    private void doSave(Object object, String outputPath, Marshaller marshaller) throws JAXBException {
        File outputFile = new File(outputPath);

        // if the output file already exists, we need to validate whether we allowed to overwrite it
        if (outputFile.exists()) {
            Optional<Boolean> allowFileOverwrites = applicationConfig.getBoolean(ALLOW_FILE_OVERWRITES);

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

    private static class NullApplicationConfig implements ApplicationConfig {
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
