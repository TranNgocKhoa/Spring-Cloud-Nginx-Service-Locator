package com.khoa.cloud.discovery.locator.config;

import com.khoa.cloud.discovery.locator.step.SyncInstanceSteps;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobsList {
    private final JobBuilderFactory jobBuilderFactory;

    public BatchJobsList(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    public Job updateInstancesJob(@Qualifier(SyncInstanceSteps.GET_INSTANCES_STEP) Step getInstances,
                                  @Qualifier(SyncInstanceSteps.GENERATE_NGINX_CONFIG_STEP) Step generateNginxConfig,
                                  @Qualifier(SyncInstanceSteps.CHANGE_NGINX_CONFIG_STEP) Step changeNginxConfig,
                                  @Qualifier(SyncInstanceSteps.RELOAD_NGINX_CONFIG_STEP) Step nginxReload) {

        return jobBuilderFactory.get("updateInstancesJob")
                .start(getInstances)
                .next(generateNginxConfig)
                .next(changeNginxConfig)
                .next(nginxReload)
                .build();
    }
}
