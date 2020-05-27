package com.khoa.cloud.discovery.locator.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job updateInstancesJob(Step getInstances, Step generateNginxConfig, Step changeNginxConfig, Step nginxReload) {
        return jobBuilderFactory.get("updateInstancesJob")
                .start(getInstances)
                .next(generateNginxConfig)
                .next(changeNginxConfig)
                .next(nginxReload)
                .build();
    }
}
