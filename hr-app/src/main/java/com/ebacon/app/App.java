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
    public static void main(String[] args) throws Exception {
        System.out.println("Program starting...");
        
        String inputFilePath = (args.length > 0) ? args[0] : "/PunchLogicTest.jsonc";

        DataLoader dataLoader = new DataLoader(inputFilePath);
        System.out.println("Reading in data from: " + inputFilePath);

        // Convert JobMeta JSON to POJOs
        JobMeta[] jobs = dataLoader.loadArray("jobMeta", JobMeta[].class);
        // Convert Employee JSON to POJOs
        Employee[] employees = dataLoader.loadArray("employeeData", Employee[].class);

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
