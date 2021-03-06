package com.khoa.cloud.discovery.locator.tasklet;

import com.khoa.cloud.discovery.locator.client.DiscoveryClientService;
import com.khoa.cloud.discovery.locator.constant.SyncServiceConstants;
import com.khoa.cloud.discovery.locator.model.Application;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@StepScope
@Component(GetInstancesTasklet.COMPONENT_NAME)
public class GetInstancesTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "getInstancesTasklet";

    private final DiscoveryClientService discoveryClientService;

    public GetInstancesTasklet(DiscoveryClientService discoveryClientService) {
        this.discoveryClientService = discoveryClientService;
    }

    @Override
    public RepeatStatus execute(@NonNull StepContribution stepContribution, ChunkContext chunkContext) {
        List<Application> applications = discoveryClientService.getApplicationList();

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
                .put(SyncServiceConstants.APPLICATION_LIST, applications);

        return RepeatStatus.FINISHED;
    }
}
