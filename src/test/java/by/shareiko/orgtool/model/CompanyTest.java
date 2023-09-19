package by.shareiko.orgtool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyTest {

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("Acme Inc.");
        company.setCreationDate(new Date());
        company.setStatus(CompanyStatus.OPEN);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());
        company.setEmployees(employees);
    }

    @Test
    void testGetName() {
        assertEquals("Acme Inc.", company.getName());
    }

    @Test
    void testSetName() {
        company.setName("XYZ Corp.");
        assertEquals("XYZ Corp.", company.getName());
    }

    @Test
    void testGetCreationDate() {
        assertNotNull(company.getCreationDate());
    }

    @Test
    void testSetCreationDate() {
        Date newDate = new Date();
        company.setCreationDate(newDate);
        assertEquals(newDate, company.getCreationDate());
    }

    @Test
    void testGetStatus() {
        assertEquals(CompanyStatus.OPEN, company.getStatus());
    }

    @Test
    void testSetStatus() {
        company.setStatus(CompanyStatus.CLOSED);
        assertEquals(CompanyStatus.CLOSED, company.getStatus());
    }

    @Test
    void testGetEmployees() {
        assertEquals(2, company.getEmployees().size());
    }

    @Test
    void testSetEmployees() {
        List<Employee> newEmployees = new ArrayList<>();
        newEmployees.add(new Employee());
        company.setEmployees(newEmployees);
        assertEquals(1, company.getEmployees().size());
    }
}