package com.ebacon.app;

import java.util.HashMap;
import java.util.Map;

import com.ebacon.app.model.JobMeta;

public class JobLookup {
    private Map<String, JobMeta> jobLookup;
    
    public JobLookup(JobMeta[] jobMetadata) {
        jobLookup = new HashMap<>();
        
        for (JobMeta job : jobMetadata) {
            jobLookup.put(job.getJob(), job);
        }
    }

    public JobMeta get(String jobName) {
        return jobLookup.get(jobName);
    }
}
