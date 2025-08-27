package com.ebacon.app.writers;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.ebacon.app.model.PayrollReportView;
import com.ebacon.app.model.PayrollSummary;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReportWriter implements IReportWriter{
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void writeReport(List<PayrollSummary> payrollSummaries, String outputPath) {
        Map<String, PayrollReportView> report = PayrollReportView.fromSummaries(payrollSummaries);

        try {
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(new File(outputPath), report);
        } catch (Exception e) {
            System.err.println("ERR - failed to write report due to " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
