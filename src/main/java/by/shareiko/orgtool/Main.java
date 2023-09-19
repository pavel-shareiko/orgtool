package by.shareiko.orgtool;

import by.shareiko.orgtool.data.access.DataAccessor;
import by.shareiko.orgtool.data.access.XmlDataAccessor;
import by.shareiko.orgtool.data.config.ApplicationConfig;
import by.shareiko.orgtool.data.config.PropertiesConfiguration;
import by.shareiko.orgtool.model.CompaniesRoot;
import by.shareiko.orgtool.model.Company;
import by.shareiko.orgtool.model.CompanyStatus;
import by.shareiko.orgtool.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static final String DEFAULT_APPLICATION_CONFIGURATION_PATH = "src/main/resources/application.properties";
    public static final String DEFAULT_CUSTOM_COMPANIES_OUTPUT_PATH = "custom-companies.xml";
    public static final String DEFAULT_OPEN_COMPANIES_OUTPUT_PATH = "open-companies.xml";
    public static final String DEFAULT_INPUT_PATH = "src/main/resources/input/input.xml";
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.info("Application started with CLI args: {}", (Object) args);

        ApplicationConfig config = new PropertiesConfiguration(DEFAULT_APPLICATION_CONFIGURATION_PATH);
        DataAccessor<CompaniesRoot> rootDataAccessor = XmlDataAccessor.forType(CompaniesRoot.class, config);

        // Необходимо выполнить десериализацию файла, вывести в консоль только открытые организации.

        /* ========= Read information about open companies from file ========= */
        List<Company> openCompanies = readAndPrintAllOpenCompanies(rootDataAccessor, config);

        // Необходимо выполнить сериализацию в xml массива организаций с перечислением её участников.

        /* ========= Save open companies to file ========= */
        saveOpenCompaniesToFile(openCompanies, rootDataAccessor, config);

        /* ========= Save generated companies to file ========= */
        generateAndSaveCustomCompanies(rootDataAccessor, config);
    }

    private static List<Company> readAndPrintAllOpenCompanies(DataAccessor<CompaniesRoot> rootDataAccessor, ApplicationConfig config) {
        String inputPath = config.getString("input.path").orElse(DEFAULT_INPUT_PATH);
        CompaniesRoot root = rootDataAccessor.read(inputPath);
        List<Company> openCompanies = root.getCompanies().stream()
                .filter(company -> company.getStatus() == CompanyStatus.OPEN)
                .collect(Collectors.toList());
        openCompanies.forEach(System.out::println);
        return openCompanies;
    }

    private static void saveOpenCompaniesToFile(List<Company> openCompanies, DataAccessor<CompaniesRoot> rootDataAccessor, ApplicationConfig config) {
        CompaniesRoot openCompaniesRoot = new CompaniesRoot();

        // Collect company's employees
        List<Employee> employees = openCompanies.stream()
                .flatMap(company -> company.getEmployees().stream())
                .distinct()
                .collect(Collectors.toList());

        openCompaniesRoot.setCompanies(openCompanies);
        openCompaniesRoot.setEmployees(employees);

        String outputPath = config.getString("output.open-companies-path").orElse(DEFAULT_OPEN_COMPANIES_OUTPUT_PATH);
        rootDataAccessor.save(openCompaniesRoot, outputPath);
    }

    private static void generateAndSaveCustomCompanies(DataAccessor<CompaniesRoot> rootDataAccessor, ApplicationConfig config) {
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

        String outputPath = config.getString("output.custom-companies-path").orElse(DEFAULT_CUSTOM_COMPANIES_OUTPUT_PATH);
        rootDataAccessor.save(customCompaniesRoot, outputPath);
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