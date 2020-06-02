package com.khoa.cloud.discovery.locator.step;

import com.khoa.cloud.discovery.locator.tasklet.ChangeNginxConfigTasklet;
import com.khoa.cloud.discovery.locator.tasklet.GenerateNginxConfigTasklet;
import com.khoa.cloud.discovery.locator.tasklet.GetInstancesTasklet;
import com.khoa.cloud.discovery.locator.tasklet.NginxReloadTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SyncInstanceSteps {
    public static final String GET_INSTANCES_STEP = "getInstances";
    public static final String GENERATE_NGINX_CONFIG_STEP = "generateNginxConfig";
    public static final String CHANGE_NGINX_CONFIG_STEP = "changeNginxConfig";
    public static final String RELOAD_NGINX_CONFIG_STEP = "nginxReload";

    private final StepBuilderFactory stepBuilderFactory;

    public SyncInstanceSteps(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(GET_INSTANCES_STEP)
    public Step getInstances(@Qualifier(GetInstancesTasklet.COMPONENT_NAME) Tasklet getInstancesTasklet) {
        return stepBuilderFactory.get("getInstances")
                .tasklet(getInstancesTasklet)
                .build();
    }

    @Bean(GENERATE_NGINX_CONFIG_STEP)
    public Step generateNginxConfig(@Qualifier(GenerateNginxConfigTasklet.COMPONENT_NAME) Tasklet generateNginxConfigTasklet) {
        return stepBuilderFactory.get("generateNginxConfig")
                .tasklet(generateNginxConfigTasklet)
                .build();
    }

    @Bean(CHANGE_NGINX_CONFIG_STEP)
    public Step changeNginxConfig(@Qualifier(ChangeNginxConfigTasklet.COMPONENT_NAME) Tasklet changeNginxConfigTasklet) {
        return stepBuilderFactory.get("changeNginxConfig")
                .tasklet(changeNginxConfigTasklet)
                .build();
    }

    @Bean(RELOAD_NGINX_CONFIG_STEP)
    public Step nginxReload(@Qualifier(NginxReloadTasklet.COMPONENT_NAME) Tasklet nginxReloadTasklet) {
        return stepBuilderFactory.get("nginxReload")
                .tasklet(nginxReloadTasklet)
                .build();
    }
}
