package by.shareiko.orgtool.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@XmlRootElement(name="company")
@XmlAccessorType(XmlAccessType.FIELD)
public class Company {
    @XmlElement
    private String name;

    @XmlElement
    @XmlSchemaType(name = "date")
    private Date creationDate;

    @XmlElement
    private CompanyStatus status;

    @XmlElement(name="employee-ref")
    @XmlIDREF
    private List<Employee> employees = new ArrayList<>();
}