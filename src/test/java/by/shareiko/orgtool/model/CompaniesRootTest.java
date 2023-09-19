package by.shareiko.orgtool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class CompaniesRootTest {

    private CompaniesRoot companiesRoot;

    @BeforeEach
    void setUp() {
        companiesRoot = new CompaniesRoot();
        List<Company> companies = new ArrayList<>();
        companies.add(new Company());
        companies.add(new Company());
        companiesRoot.setCompanies(companies);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());
        companiesRoot.setEmployees(employees);
    }

    @Test
    void testGetCompanies() {
        assertEquals(2, companiesRoot.getCompanies().size());
    }

    @Test
    void testSetCompanies() {
        List<Company> newCompanies = new ArrayList<>();
        newCompanies.add(new Company());
        companiesRoot.setCompanies(newCompanies);
        assertEquals(1, companiesRoot.getCompanies().size());
    }

    @Test
    void testGetEmployees() {
        assertEquals(2, companiesRoot.getEmployees().size());
    }

    @Test
    void testSetEmployees() {
        List<Employee> newEmployees = new ArrayList<>();
        newEmployees.add(new Employee());
        companiesRoot.setEmployees(newEmployees);
        assertEquals(1, companiesRoot.getEmployees().size());
    }
}