package com.khoa.cloud.discovery.locator.step;

import com.khoa.cloud.discovery.locator.tasklet.ChangeNginxConfigTasklet;
import com.khoa.cloud.discovery.locator.tasklet.GenerateNginxConfigTasklet;
import com.khoa.cloud.discovery.locator.tasklet.GetInstancesTasklet;
import com.khoa.cloud.discovery.locator.tasklet.NginxReloadTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SyncInstanceSteps {
    private final StepBuilderFactory stepBuilderFactory;

    public SyncInstanceSteps(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean("getInstances")
    public Step getInstances(@Qualifier(GetInstancesTasklet.COMPONENT_NAME) Tasklet getInstancesTasklet) {
        return stepBuilderFactory.get("getInstances")
                .tasklet(getInstancesTasklet)
                .build();
    }

    @Bean("generateNginxConfig")
    public Step generateNginxConfig(@Qualifier(GenerateNginxConfigTasklet.COMPONENT_NAME) Tasklet generateNginxConfigTasklet) {
        return stepBuilderFactory.get("generateNginxConfig")
                .tasklet(generateNginxConfigTasklet)
                .build();
    }

    @Bean("changeNginxConfig")
    public Step changeNginxConfig(@Qualifier(ChangeNginxConfigTasklet.COMPONENT_NAME) Tasklet changeNginxConfigTasklet) {
        return stepBuilderFactory.get("changeNginxConfig")
                .tasklet(changeNginxConfigTasklet)
                .build();
    }


    @Bean("nginxReload")
    public Step nginxReload(@Qualifier(NginxReloadTasklet.COMPONENT_NAME) Tasklet nginxReloadTasklet) {
        return stepBuilderFactory.get("nginxReload")
                .tasklet(nginxReloadTasklet)
                .build();
    }

}
