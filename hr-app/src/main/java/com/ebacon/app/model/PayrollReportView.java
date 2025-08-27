package com.ebacon.app.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayrollReportView {
    @JsonProperty("employee")
    private final String employee;

    @JsonProperty("regular")
    private final String regular;

    @JsonProperty("overtime")
    private final String overtime;

    @JsonProperty("doubletime")
    private final String doubletime;

    @JsonProperty("wageTotal")
    private final String wageTotal;

    @JsonProperty("benefitTotal")
    private final String benefitTotal;

    public PayrollReportView(PayrollSummary summary) {
        this.employee = summary.getEmployee();
        this.regular = String.format("%.4f", summary.getRegular());
        this.overtime = String.format("%.4f", summary.getOvertime());
        this.doubletime = String.format("%.4f", summary.getDoubletime());
        this.wageTotal = String.format("%.4f", summary.getWageTotal());
        this.benefitTotal = String.format("%.4f", summary.getBenefitTotal());
    }

    public static Map<String, PayrollReportView> fromSummaries(Iterable<PayrollSummary> summaries) {
        Map<String, PayrollReportView> map = new HashMap<>();
        for (PayrollSummary s : summaries) {
            map.put(s.getEmployee(), new PayrollReportView(s));
        }
        return map;
    }
}
