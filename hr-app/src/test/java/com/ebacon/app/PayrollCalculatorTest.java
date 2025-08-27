package com.ebacon.app;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ebacon.app.config.PayrollRulesConfig;
import com.ebacon.app.model.Employee;
import com.ebacon.app.model.JobMeta;
import com.ebacon.app.model.PayrollSummary;
import com.ebacon.app.model.TimePunch;

class PayrollCalculatorTest {

    private PayrollCalculator defaultCalculator;
    private PayrollCalculator customCalculator;
    private JobLookup jobLookup;

    @BeforeEach
    void setup() {
        // Define sample jobs
        JobMeta devJob = new JobMeta();
        devJob.setJob("DEV");
        devJob.setRate(50.0);
        devJob.setBenefitsRate(5.0);

        JobMeta qaJob = new JobMeta();
        qaJob.setJob("QA");
        qaJob.setRate(40.0);
        qaJob.setBenefitsRate(4.0);

        jobLookup = new JobLookup(new JobMeta[]{devJob, qaJob});

        // Default rules
        PayrollRulesConfig defaultRules = new PayrollRulesConfig();
        defaultCalculator = new PayrollCalculator(jobLookup, defaultRules);

        // Custom rules
        PayrollRulesConfig customRules = new PayrollRulesConfig(
                30,      // overtime limit
                40,      // doubletime limit
                2.0,     // overtime rate
                2.5,     // doubletime rate
                "MM-dd-yyyy HH:mm:ss"
        );
        customCalculator = new PayrollCalculator(jobLookup, customRules);
    }

    @Test
    void testRegularHoursDefaultConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Alice");

        TimePunch punch = new TimePunch();
        punch.setJob("DEV");
        punch.setStart("2025-08-26 09:00:00");
        punch.setEnd("2025-08-26 17:00:00");

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = defaultCalculator.calculate(emp);

        assertEquals(8.0, summary.getRegular(), 0.01);
        assertEquals(0.0, summary.getOvertime(), 0.01);
        assertEquals(0.0, summary.getDoubletime(), 0.01);
        assertEquals(8 * 50.0, summary.getWageTotal(), 0.01);
        assertEquals(8 * 5.0, summary.getBenefitTotal(), 0.01);
    }

    @Test
    void testOvertimeAndDoubletimeDefaultConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Bob");

        TimePunch punch = new TimePunch();
        punch.setJob("DEV");
        punch.setStart("2025-08-26 09:00:00");
        punch.setEnd("2025-08-28 11:00:00"); // 50 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = defaultCalculator.calculate(emp);

        assertEquals(40.0, summary.getRegular(), 0.01);
        assertEquals(8.0, summary.getOvertime(), 0.01);
        assertEquals(2.0, summary.getDoubletime(), 0.01);
        assertEquals(40 * 50.0 + 8 * 50 * 1.5 + 2 * 50 * 2, summary.getWageTotal(), 0.01);
        assertEquals(50 * 5.0, summary.getBenefitTotal(), 0.01);
    }

    @Test
    void testCustomConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Charlie");

        TimePunch punch = new TimePunch();
        punch.setJob("DEV");
        punch.setStart("08-26-2025 09:00:00");
        punch.setEnd("08-27-2025 02:00:00"); // 17 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = customCalculator.calculate(emp);

        assertEquals(17.0, summary.getRegular(), 0.01);
        assertEquals(0.0, summary.getOvertime(), 0.01);
        assertEquals(0.0, summary.getDoubletime(), 0.01);
        assertEquals(17 * 50.0, summary.getWageTotal(), 0.01);
        assertEquals(17 * 5.0, summary.getBenefitTotal(), 0.01);
    }

    @Test
    void testOvertimeAndDoubletimeWithCustomConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Dana");

        TimePunch punch = new TimePunch();
        punch.setJob("QA");
        punch.setStart("08-26-2025 09:00:00");
        punch.setEnd("08-28-2025 06:00:00"); // 45 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = customCalculator.calculate(emp);

        assertEquals(30.0, summary.getRegular(), 0.01);
        assertEquals(10.0, summary.getOvertime(), 0.01);
        assertEquals(5.0, summary.getDoubletime(), 0.01);
        assertEquals(30 * 40.0 + 10 * 40 * 2 + 5 * 40 * 2.5, summary.getWageTotal(), 0.01);
        assertEquals(45 * 4.0, summary.getBenefitTotal(), 0.01);
    }
}
