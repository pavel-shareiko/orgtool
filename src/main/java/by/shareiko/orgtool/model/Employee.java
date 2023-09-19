package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {
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
