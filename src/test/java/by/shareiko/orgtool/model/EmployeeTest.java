package by.shareiko.orgtool.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId("1");
        employee.setFirstName("John");
        employee.setMiddleName("Doe");
        employee.setLastName("Smith");
        employee.setBirthDate(new Date());
    }

    @Test
    void testGetId() {
        assertEquals("1", employee.getId());
    }

    @Test
    void testSetId() {
        employee.setId("2");
        assertEquals("2", employee.getId());
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", employee.getFirstName());
    }

    @Test
    void testSetFirstName() {
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());
    }

    @Test
    void testGetMiddleName() {
        assertEquals("Doe", employee.getMiddleName());
    }

    @Test
    void testSetMiddleName() {
        employee.setMiddleName("Mary");
        assertEquals("Mary", employee.getMiddleName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    void testSetLastName() {
        employee.setLastName("Doe");
        assertEquals("Doe", employee.getLastName());
    }

    @Test
    void testGetBirthDate() {
        assertNotNull(employee.getBirthDate());
    }

    @Test
    void testSetBirthDate() {
        Date newDate = new Date();
        employee.setBirthDate(newDate);
        assertEquals(newDate, employee.getBirthDate());
    }
}