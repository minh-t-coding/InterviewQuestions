package com.ebacon.app.writers;

import java.util.List;

import com.ebacon.app.model.PayrollSummary;

public interface IReportWriter {
    void writeReport(List<PayrollSummary> payrollSummaries, String outputPath) throws Exception;
}
