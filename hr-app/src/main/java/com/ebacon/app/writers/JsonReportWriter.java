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
    public void writeReport(List<PayrollSummary> payrollSummaries, String outputPath) throws Exception {
        Map<String, PayrollReportView> report = PayrollReportView.fromSummaries(payrollSummaries);

        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(new File(outputPath), report);
    }
}
