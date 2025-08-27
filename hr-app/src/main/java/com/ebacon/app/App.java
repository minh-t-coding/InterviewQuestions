package com.ebacon.app;

import java.util.ArrayList;
import java.util.List;

import com.ebacon.app.config.PayrollRulesConfig;
import com.ebacon.app.model.Employee;
import com.ebacon.app.model.JobMeta;
import com.ebacon.app.model.PayrollSummary;
import com.ebacon.app.writers.IReportWriter;
import com.ebacon.app.writers.JsonReportWriter;

public class App {
    public static void main(String[] args) {
        System.out.println("Program starting...");
        
        // If no arguments are given, load PunchLogicTest from bundled resources
        String inputFilePath = (args.length > 0) ? args[0] : "/PunchLogicTest.jsonc";
        JobMeta[] jobs;
        Employee[] employees;
        try {
        DataLoader dataLoader = new DataLoader(inputFilePath);
        System.out.println("Reading in data from: " + inputFilePath);

        // Convert JobMeta JSON to POJOs
        jobs = dataLoader.loadArray("jobMeta", JobMeta[].class);
        // Convert Employee JSON to POJOs
        employees = dataLoader.loadArray("employeeData", Employee[].class);
        } catch (Exception e) {
            System.err.println("ERR - There was some error in processing the input data");
            e.printStackTrace(System.err);
            return;
        }

        // Read jobs
        JobLookup jobLookup = new JobLookup(jobs);

        PayrollRulesConfig defaultConfig = new PayrollRulesConfig();
        PayrollCalculator payrollCalculator = new PayrollCalculator(jobLookup, defaultConfig);

        List<PayrollSummary> allSummaries = new ArrayList<>();

        // Calculate summary for each employee
        System.out.println("Calculating payrolls...");
        for (Employee emp : employees) {
            PayrollSummary currentEmployeeSummary = payrollCalculator.calculate(emp);
            allSummaries.add(currentEmployeeSummary);
        }
        
        IReportWriter writer = new JsonReportWriter();
        writer.writeReport(allSummaries, "output.json");
        System.out.println("Payroll report successfully written to output.json!");
    }
}
