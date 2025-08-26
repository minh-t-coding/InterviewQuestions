package com.ebacon.app;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebacon.app.model.Employee;
import com.ebacon.app.model.JobMeta;
import com.ebacon.app.model.PayrollSummary;
import com.ebacon.app.model.TimePunch;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) throws IOException {
        InputStream in = App.class.getResourceAsStream("/PunchLogicTest.jsonc");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);

        JsonNode root = mapper.readTree(in);

        // Convert JobMeta JSON to POJOs
        JobMeta[] jobs = mapper.treeToValue(root.get("jobMeta"), JobMeta[].class);

        // Convert Employee JSON to POJOs
        Employee[] employees = mapper.treeToValue(root.get("employeeData"), Employee[].class);

        // Read jobs, create a lookup table
        Map<String, JobMeta> jobLookup = new HashMap<>();
        for (JobMeta job : jobs) {
            jobLookup.put(job.getJob(), job);
        }

        List<PayrollSummary> allEmployeesPayrollSummary = new ArrayList<>();

        // Run through employees
        for (Employee emp : employees) {
            List<TimePunch> timePunches = emp.getTimePunches();
            PayrollSummary payrollSummary = new PayrollSummary(emp.getEmployeeName());

            double totalHours = 0;
            double wageTotal = 0;
            double benefitTotal = 0;
            double regularHours = 0;
            double overtimeHours = 0;
            double doubletimeHours = 0;

            for (TimePunch punch : timePunches) {
                DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime s = LocalDateTime.parse(punch.getStart(), FORMATTER);
                LocalDateTime e = LocalDateTime.parse(punch.getEnd(), FORMATTER);
                double hours = Duration.between(s, e).toSeconds() / 3600.0;
                double rate = jobLookup.get(punch.getJob()).getRate();
                double benefitRate = jobLookup.get(punch.getJob()).getBenefitsRate();

                while (hours > 0) {
                    double relevantHours;
                    if (totalHours < 40) {
                        relevantHours = Math.min(40 - totalHours, hours);
                        wageTotal += relevantHours * rate;
                        benefitTotal += relevantHours * benefitRate;
                        regularHours += relevantHours;
                        totalHours += relevantHours;
                        hours -= relevantHours;
                    }
                    else if (totalHours >= 40 && totalHours < 48) {
                        relevantHours = Math.min(48 - totalHours, hours);
                        wageTotal += 1.5 * relevantHours * rate;
                        benefitTotal += relevantHours * benefitRate;
                        overtimeHours += relevantHours;
                        totalHours += relevantHours;
                        hours -= relevantHours;
                    } 
                    else {
                        wageTotal += 2 * hours * rate;
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

            allEmployeesPayrollSummary.add(payrollSummary);
        }
        
        for (PayrollSummary payrollSummary : allEmployeesPayrollSummary) {
            System.out.println(payrollSummary);
        }
    }
}
