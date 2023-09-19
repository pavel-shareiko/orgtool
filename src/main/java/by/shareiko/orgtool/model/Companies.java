package by.shareiko.orgtool.model;


import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "companies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Companies {
    @XmlElement(name = "company")
    private List<Company> companies;
}
