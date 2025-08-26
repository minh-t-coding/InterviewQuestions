package com.ebacon.app.model;

public class TimePunch {
    private String job;
    private String start;
    private String end;

    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }
    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }
    
    @Override
    public String toString() {
        return String.format("%s: start=%s, end=%s",
                job, start, end);
    }
}
