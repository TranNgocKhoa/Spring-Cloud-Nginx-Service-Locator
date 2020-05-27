package com.khoa.cloud.discovery.locator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class ServiceInstanceSyncApplication {
    private final JobLauncher jobLauncher;
    private final Job updateInstancesJob;

    public ServiceInstanceSyncApplication(JobLauncher jobLauncher, Job updateInstancesJob) {
        this.jobLauncher = jobLauncher;
        this.updateInstancesJob = updateInstancesJob;
    }


    public static void main(String[] args) {
        SpringApplication.run(ServiceInstanceSyncApplication.class, args);
    }


    @Scheduled(cron = "*/30 * * * * *")
    public void updateInstances() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("updateInstancesID: ", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(updateInstancesJob, jobParameters);
    }
}
