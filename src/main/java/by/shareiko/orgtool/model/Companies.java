package by.shareiko.orgtool.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@XmlRootElement(name = "companies")
@XmlAccessorType(XmlAccessType.FIELD)
public class Companies {
    @XmlElement(name = "company")
    @Getter(AccessLevel.NONE)
    private List<Company> companies = new ArrayList<>();

    public boolean add(Company company) {
        return this.companies.add(company);
    }

    public boolean remove(Company company) {
        return this.companies.remove(company);
    }

    public Stream<Company> stream() {
        return companies.stream();
    }
}
