package com.ebacon.app.config;

import java.time.format.DateTimeFormatter;

public class PayrollRulesConfig {
    private final int overtimeLimit;
    private final int doubletimeLimit;
    private final double overtimeRate;
    private final double doubletimeRate;
    private final DateTimeFormatter dateTimeFormatter;

    public PayrollRulesConfig() {
        this.overtimeLimit = 40;
        this.doubletimeLimit = 48;
        this.overtimeRate = 1.5;
        this.doubletimeRate = 2.0;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public PayrollRulesConfig(
            int overtimeLimit, 
            int doubletimeLimit, 
            double overtimeRate, 
            double doubletimeRate,
            String dateFormat) {
        this.overtimeLimit = overtimeLimit;
        this.doubletimeLimit = doubletimeLimit;
        this.overtimeRate = overtimeRate;
        this.doubletimeRate = doubletimeRate;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    public int getOvertimeLimit() { return overtimeLimit; }
    public int getDoubletimeLimit() { return doubletimeLimit; }
    public double getOvertimeRate() { return overtimeRate; }
    public double getDoubletimeRate() { return doubletimeRate; }
    public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatter; }
}
