package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Data
@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
public class Company {
    @XmlElement
    private String name;

    @XmlElement
    @XmlSchemaType(name = "date")
    private Date creationDate;

    @XmlElement
    private CompanyStatus status;

    @XmlElement(name = "employees")
    private Employees employees;
}
