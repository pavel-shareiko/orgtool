package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Company {
    @XmlAttribute
    private String name;

    @XmlAttribute
    @XmlSchemaType(name = "date")
    private Date creationDate;

    @XmlAttribute
    private CompanyStatus status;

    @XmlElementWrapper(name = "employees")
    @XmlElement(name = "employee")
    private List<Employee> employees;
}
