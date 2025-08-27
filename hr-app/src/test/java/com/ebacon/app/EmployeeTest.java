package com.ebacon.app;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.ebacon.app.model.Employee;
import com.ebacon.app.model.TimePunch;

public class EmployeeTest {
    @Test
    void testGettersAndSetters() {
        Employee emp = new Employee();

        // Create sample TimePunches
        TimePunch punch1 = new TimePunch();
        punch1.setJob("DEV");
        punch1.setStart("2025-08-26T09:00");
        punch1.setEnd("2025-08-26T17:00");

        TimePunch punch2 = new TimePunch();
        punch2.setJob("QA");
        punch2.setStart("2025-08-26T18:00");
        punch2.setEnd("2025-08-26T20:00");

        List<TimePunch> punches = Arrays.asList(punch1, punch2);

        emp.setEmployeeName("Alice");
        emp.setTimePunches(punches);

        // Assertions
        assertEquals("Alice", emp.getEmployeeName());
        assertEquals(2, emp.getTimePunches().size());
        assertEquals("DEV", emp.getTimePunches().get(0).getJob());
        assertEquals("QA", emp.getTimePunches().get(1).getJob());
    }
}
