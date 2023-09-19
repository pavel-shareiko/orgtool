package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {

    @XmlAttribute
    @XmlID
    private String id;

    @XmlElement
    private String firstName;

    @XmlElement
    private String middleName;

    @XmlElement
    private String lastName;

    @XmlElement
    @XmlSchemaType(name = "date")
    private Date birthDate;
}