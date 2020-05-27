package com.khoa.cloud.discovery.locator.tasklet;

import com.khoa.cloud.discovery.locator.model.ServiceInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@StepScope
@Component(GenerateNginxConfigTasklet.COMPONENT_NAME)
public class GenerateNginxConfigTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "generateNginxConfigTasklet";

    @Value("#{jobExecutionContext[syncServiceConstants.SERVICE_INSTANCE_LIST]}")
    private List<ServiceInstance> serviceInstances;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if (CollectionUtils.isEmpty(serviceInstances)) {
            return RepeatStatus.FINISHED;
        }

        serviceInstances.forEach(System.out::println);

        return RepeatStatus.FINISHED;
    }
}
