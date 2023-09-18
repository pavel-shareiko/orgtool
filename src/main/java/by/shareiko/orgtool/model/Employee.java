package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {
    @XmlAttribute
    private String firstName;

    @XmlAttribute
    private String middleName;

    @XmlAttribute
    private String lastName;

    @XmlAttribute
    @XmlSchemaType(name = "date")
    private Date birthDate;
}
