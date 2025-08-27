package com.ebacon.app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ebacon.app.config.PayrollRulesConfig;
import com.ebacon.app.model.Employee;
import com.ebacon.app.model.JobMeta;
import com.ebacon.app.model.PayrollSummary;
import com.ebacon.app.model.TimePunch;

public class PayrollCalculator {
    private final JobLookup jobLookup;
    private final DateTimeFormatter FORMATTER;
    private final int OVERTIME_LIMIT;
    private final int DOUBLETIME_LIMIT;
    private final double OVERTIME_RATE;
    private final double DOUBLETIME_RATE;

    public PayrollCalculator(JobLookup jobLookup, PayrollRulesConfig rules) {
        this.jobLookup = jobLookup;
        this.FORMATTER = rules.getDateTimeFormatter();
        this.OVERTIME_LIMIT = rules.getOvertimeLimit();
        this.OVERTIME_RATE = rules.getOvertimeRate();
        this.DOUBLETIME_LIMIT = rules.getDoubletimeLimit();
        this.DOUBLETIME_RATE = rules.getDoubletimeRate();
    }

    public PayrollSummary calculate(Employee emp) {
        List<TimePunch> timePunches = emp.getTimePunches();
        PayrollSummary payrollSummary = new PayrollSummary(emp.getEmployeeName());

        double totalHours = 0;
        double wageTotal = 0;
        double benefitTotal = 0;
        double regularHours = 0;
        double overtimeHours = 0;
        double doubletimeHours = 0;

        for (TimePunch punch : timePunches) {
            LocalDateTime start;
            LocalDateTime end;
            try {
                start = LocalDateTime.parse(punch.getStart(), FORMATTER);
                end = LocalDateTime.parse(punch.getEnd(), FORMATTER);
            } catch (Exception e) {
                System.err.println(String.format("ERR - Could not format time punch %s for Employee: %s", punch.toString(), emp.getEmployeeName()));
                continue;
            }
            double hours = Duration.between(start, end).toSeconds() / 3600.0; // 3600 seconds in an hour

            JobMeta jobMeta = jobLookup.get(punch.getJob());
            if (jobMeta == null) {
                System.err.println(String.format("ERR - Could not find job: %s in lookup", punch.getJob()));
                continue;
            }
            double rate = jobMeta.getRate();
            double benefitRate = jobMeta.getBenefitsRate();

            while (hours > 0) {
                double relevantHours;
                if (totalHours < OVERTIME_LIMIT) {
                    relevantHours = Math.min(OVERTIME_LIMIT - totalHours, hours);
                    wageTotal += relevantHours * rate;
                    benefitTotal += relevantHours * benefitRate;
                    regularHours += relevantHours;
                    totalHours += relevantHours;
                    hours -= relevantHours;
                }
                else if (totalHours >= OVERTIME_LIMIT && totalHours < DOUBLETIME_LIMIT) {
                    relevantHours = Math.min(DOUBLETIME_LIMIT - totalHours, hours);
                    wageTotal += OVERTIME_RATE * relevantHours * rate;
                    benefitTotal += relevantHours * benefitRate;
                    overtimeHours += relevantHours;
                    totalHours += relevantHours;
                    hours -= relevantHours;
                } 
                else {
                    wageTotal += DOUBLETIME_RATE * hours * rate;
                    benefitTotal += hours * benefitRate;
                    doubletimeHours += hours;
                    totalHours += hours;
                    hours = 0;
                }
            }                
        }

        payrollSummary.setRegular(regularHours);
        payrollSummary.setDoubletime(doubletimeHours);
        payrollSummary.setOvertime(overtimeHours);
        payrollSummary.setBenefitTotal(benefitTotal);
        payrollSummary.setWageTotal(wageTotal);

        return payrollSummary;
        }
}