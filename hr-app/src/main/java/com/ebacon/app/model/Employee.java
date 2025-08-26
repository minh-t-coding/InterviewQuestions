package com.ebacon.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {
    @JsonProperty("employee")
    private String employeeName;
    @JsonProperty("timePunch")
    private List<TimePunch> timePunches;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employee) { this.employeeName = employee; }
    public List<TimePunch> getTimePunches() { return timePunches; }
    public void setTimePunches(List<TimePunch> timePunch) { this.timePunches = timePunch; }
}
