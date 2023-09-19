package by.shareiko.orgtool.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employees {
    @XmlElement(name = "employee")
    @Getter(AccessLevel.NONE)
    private List<Employee> employees = new ArrayList<>();

    public boolean add(Employee employee) {
        return this.employees.add(employee);
    }

    public boolean remove(Employee employee) {
        return this.employees.remove(employee);
    }

    public Stream<Employee> stream() {
        return this.employees.stream();
    }
}
