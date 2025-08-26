package com.ebacon.app.model;

public class PayrollSummary {
    private String employeeName;
    private double regularHours;
    private double overtimeHours;
    private double doubletimeHours;
    private double wageTotal;
    private double benefitTotal;

    public PayrollSummary(String employee) {
        this.employeeName = employee;
    }

    // Getters and setters
    public String getEmployee() { return employeeName; }
    public double getRegular() { return regularHours; }
    public void setRegular(double regular) { this.regularHours = regular; }
    public double getOvertime() { return overtimeHours; }
    public void setOvertime(double overtime) { this.overtimeHours = overtime; }
    public double getDoubletime() { return doubletimeHours; }
    public void setDoubletime(double doubletime) { this.doubletimeHours = doubletime; }
    public double getWageTotal() { return wageTotal; }
    public void setWageTotal(double wageTotal) { this.wageTotal = wageTotal; }
    public double getBenefitTotal() { return benefitTotal; }
    public void setBenefitTotal(double benefitTotal) { this.benefitTotal = benefitTotal; }

    @Override
    public String toString() {
        return String.format(
            "PayrollSummary for %s:\n" +
            "  Regular Hours : %.4f\n" +
            "  Overtime Hours: %.4f\n" +
            "  Doubletime Hours: %.4f\n" +
            "  Wage Total    : $%.4f\n" +
            "  Benefit Total : $%.4f\n",
            employeeName,
            regularHours,
            overtimeHours,
            doubletimeHours,
            wageTotal,
            benefitTotal
        );
    }
}
