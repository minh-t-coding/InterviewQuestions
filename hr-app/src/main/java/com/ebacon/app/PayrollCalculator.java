package com.ebacon.app;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ebacon.app.config.PayrollRulesConfig;
import com.ebacon.app.model.Employee;
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
                LocalDateTime s = LocalDateTime.parse(punch.getStart(), FORMATTER);
                LocalDateTime e = LocalDateTime.parse(punch.getEnd(), FORMATTER);
                double hours = Duration.between(s, e).toSeconds() / 3600.0;
                double rate = jobLookup.get(punch.getJob()).getRate();
                double benefitRate = jobLookup.get(punch.getJob()).getBenefitsRate();

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
                        hours -= hours;
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