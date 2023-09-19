package by.shareiko.orgtool;

import by.shareiko.orgtool.data.access.DataAccessor;
import by.shareiko.orgtool.data.access.XmlDataAccessor;
import by.shareiko.orgtool.data.config.ApplicationConfig;
import by.shareiko.orgtool.data.config.PropertiesConfiguration;
import by.shareiko.orgtool.model.CompaniesRoot;
import by.shareiko.orgtool.model.Company;
import by.shareiko.orgtool.model.CompanyStatus;
import by.shareiko.orgtool.model.Employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ApplicationConfig config = new PropertiesConfiguration("src/main/resources/configuration.properties");
        DataAccessor<CompaniesRoot> rootDataAccessor = XmlDataAccessor.forType(CompaniesRoot.class, config);

        // Необходимо выполнить десериализацию файла, вывести в консоль только открытые организации.

        /* ========= Read information about open companies from file ========= */
        CompaniesRoot root = rootDataAccessor.read("src/main/resources/input/input.xml");
        List<Company> openCompanies = root.getCompanies().stream()
                .filter(company -> company.getStatus() == CompanyStatus.OPEN)
                .collect(Collectors.toList());
        openCompanies.forEach(System.out::println);

        // Необходимо выполнить сериализацию в xml массива организаций с перечислением её участников.

        /* ========= Save open companies to file ========= */
        CompaniesRoot openCompaniesRoot = new CompaniesRoot();

        // Collect company's employees
        List<Employee> employees = openCompanies.stream()
                .flatMap(company -> company.getEmployees().stream())
                .distinct()
                .collect(Collectors.toList());

        openCompaniesRoot.setCompanies(openCompanies);
        openCompaniesRoot.setEmployees(employees);

        rootDataAccessor.save(openCompaniesRoot, "open-companies.xml");

        /* ========= Save custom companies to file ========= */
        CompaniesRoot customCompaniesRoot = new CompaniesRoot();

        // Create custom companies
        int companiesCount = (int) (Math.random() * 10);
        List<Company> customCompanies = generateCompanies(companiesCount);

        // create a list of employees for each company
        List<Employee> customEmployees = new ArrayList<>();
        for (Company company : customCompanies) {
            int employeesCount = (int) (Math.random() * 10);
            List<Employee> companyEmployees = generateEmployees(employeesCount);
            customEmployees.addAll(companyEmployees);
            company.setEmployees(companyEmployees);
        }
        customCompaniesRoot.setEmployees(customEmployees);
        customCompaniesRoot.setCompanies(customCompanies);

        rootDataAccessor.save(customCompaniesRoot, "custom-companies.xml");
    }

    private static List<Company> generateCompanies(int count) {
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            Company company = new Company();
            company.setName("Company " + i);
            company.setCreationDate(new Date());
            company.setStatus(CompanyStatus.OPEN);

            companies.add(company);
        }

        return companies;
    }

    // static variable for tracking last generated employee id, used for unique id generation
    private static long lastEmployeeId = 0;

    private static List<Employee> generateEmployees(int count) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            Employee employee = new Employee();
            employee.setId(String.valueOf(lastEmployeeId++));
            employee.setFirstName("First name " + i);
            employee.setMiddleName("Middle name " + i);
            employee.setLastName("Last name " + i);
            employee.setBirthDate(new Date());

            employees.add(employee);
        }

        return employees;
    }
}