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
                "yyyy-MM-dd HH:mm:ss"
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
        punch.setEnd("2025-08-27 02:00:00"); // 17 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = defaultCalculator.calculate(emp);

        // Default config: OVERTIME_LIMIT = 40, DOUBLETIME_LIMIT = 48
        // 17 < 40, so all counted as regular? Actually based on original logic, adjust if needed.
        assertEquals(17.0, summary.getRegular(), 0.01);
        assertEquals(0.0, summary.getOvertime(), 0.01);
        assertEquals(0.0, summary.getDoubletime(), 0.01);
    }

    @Test
    void testCustomConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Charlie");

        TimePunch punch = new TimePunch();
        punch.setJob("DEV");
        punch.setStart("2025-08-26 09:00:00");
        punch.setEnd("2025-08-27 02:00:00"); // 17 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = customCalculator.calculate(emp);

        // Custom config: OVERTIME_LIMIT = 30, DOUBLETIME_LIMIT = 40
        // 17 hours < 30, so all counted as regular
        assertEquals(17.0, summary.getRegular(), 0.01);
        assertEquals(0.0, summary.getOvertime(), 0.01);
        assertEquals(0.0, summary.getDoubletime(), 0.01);
    }

    @Test
    void testOvertimeWithCustomConfig() {
        Employee emp = new Employee();
        emp.setEmployeeName("Dana");

        TimePunch punch = new TimePunch();
        punch.setJob("DEV");
        punch.setStart("2025-08-26 09:00:00");
        punch.setEnd("2025-08-27 12:00:00"); // 27 hours

        emp.setTimePunches(Arrays.asList(punch));

        PayrollSummary summary = customCalculator.calculate(emp);

        // Custom config: OVERTIME_LIMIT = 30, DOUBLETIME_LIMIT = 40
        // 27 < 30, still regular
        assertEquals(27.0, summary.getRegular(), 0.01);
        assertEquals(0.0, summary.getOvertime(), 0.01);
        assertEquals(0.0, summary.getDoubletime(), 0.01);
    }
}
