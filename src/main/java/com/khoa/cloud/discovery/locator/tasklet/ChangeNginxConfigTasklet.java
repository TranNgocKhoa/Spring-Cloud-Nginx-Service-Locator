package com.khoa.cloud.discovery.locator.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;

@StepScope
@Component(ChangeNginxConfigTasklet.COMPONENT_NAME)
public class ChangeNginxConfigTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "changeNginxConfigTasklet";

    @Value("${nginx.config-file}")
    private String nginxConfigFile;
    @Value("#{jobExecutionContext[syncServiceConstants.SERVICES_LOAD_BALANCING_CONFIG]}")
    private String serviceLoadBalancingConfig;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        FileWriter fileWriter = new FileWriter(nginxConfigFile, false);
        fileWriter.write(serviceLoadBalancingConfig);

        fileWriter.close();
        return RepeatStatus.FINISHED;
    }
}
