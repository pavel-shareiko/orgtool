package by.shareiko.orgtool;

import by.shareiko.orgtool.data.access.DataAccessor;
import by.shareiko.orgtool.data.access.XmlDataAccessor;
import by.shareiko.orgtool.data.config.DataAccessConfig;
import by.shareiko.orgtool.data.config.PropertiesConfiguration;
import by.shareiko.orgtool.model.*;

import java.sql.Date;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        DataAccessConfig config = new PropertiesConfiguration("src/main/resources/configuration.properties");
        DataAccessor<Companies> dataAccessor = XmlDataAccessor.forType(Companies.class, config);

        // Необходимо выполнить десериализацию файла, вывести в консоль только открытые организации.
        Companies result = dataAccessor.read("input.xml");
        result.stream()
                .filter(company -> company.getStatus() == CompanyStatus.OPEN)
                .forEach(System.out::println);

        // Необходимо выполнить сериализацию в xml массива организаций с перечислением её участников.
        Companies companies = generateCompanies();
        dataAccessor.save(companies, "output.xml");
    }

    private static Companies generateCompanies() {
        Companies companies = new Companies();

        for (int i = 0; i < 10; ++i) {
            Company company = generateCompany(i);
            companies.add(company);
        }

        return companies;
    }

    private static Company generateCompany(int id) {
        Company company = new Company();
        company.setStatus(Math.random() > 0.6 ? CompanyStatus.OPEN : CompanyStatus.CLOSED);
        company.setName("Company " + id);
        company.setCreationDate(Date.from(Instant.now()));

        int employeesCount = (int) (Math.random() * 10);
        Employees employees = generateEmployees(employeesCount);

        company.setEmployees(employees);
        return company;
    }

    private static Employees generateEmployees(int employeesCount) {
        Employees employees = new Employees();
        for (int i = 0; i < employeesCount; ++i) {
            Employee employee = generateEmployee(i);
            employees.add(employee);
        }
        return employees;
    }

    private static Employee generateEmployee(int j) {
        Employee employee = new Employee();
        employee.setFirstName("FirstName " + j);
        employee.setMiddleName("MiddleName " + j);
        employee.setLastName("LastName " + j);
        employee.setBirthDate(Date.from(Instant.now()));
        return employee;
    }
}