package com.ebacon.app.model;

public class JobMeta {
    private String jobTitle;
    private double rate;
    private double benefitRate;

    public String getJob() { return jobTitle; }
    public void setJob(String job) { this.jobTitle = job; }
    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
    public double getBenefitsRate() { return benefitRate; }
    public void setBenefitsRate(double benefitsRate) { this.benefitRate = benefitsRate; }
}
