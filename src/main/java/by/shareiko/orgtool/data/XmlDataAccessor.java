package by.shareiko.orgtool.data;

import by.shareiko.orgtool.data.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import java.io.File;
import java.util.List;

public class XmlDataAccessor<T> implements DataAccessor<T> {
    private static final Logger LOG = LoggerFactory.getLogger(XmlDataAccessor.class);
    private final Class<T> entityType;

    private XmlDataAccessor(Class<T> entityType) {
        this.entityType = entityType;
    }

    public static <T> XmlDataAccessor<T> forType(Class<T> entityType) {
        return new XmlDataAccessor<>(entityType);
    }

    @Override
    public void saveAll(List<T> objects, String outputPath) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Serializing collection to XML file '{}': {}", outputPath, objects);
        }

        try {
            Marshaller marshaller = createMarshaller(objects);
            File file = new File(outputPath);
            marshaller.marshal(objects, file);
        } catch (JAXBException e) {
            LOG.error("An unexpected JAXB error occurred while accessing the file", e);
            throw new DataAccessException("Failed to save object because of underlying JAXB error", e);
        }
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
    public List<T> readAll(String path) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading a collection of objects from file '{}'", path);
        }

        try {
            T readResult = doRead(path);
            return (List<T>) readResult;
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
        // TODO: move to config
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        return marshaller;
    }

    private void doSave(T object, String outputPath, Marshaller marshaller) throws JAXBException {
        File outputFile = new File(outputPath);
        marshaller.marshal(object, outputFile);
    }

    private T doRead(String path) throws JAXBException {
        Unmarshaller unmarshaller = createUnmarshaller();

        File contentFile = new File(path);
        if (!contentFile.exists()) {
            throw new DataAccessException("Unable to access file '" + contentFile + "' because it doesn't exist");
        }

        return (T) unmarshaller.unmarshal(contentFile);
    }
}
