package com.khoa.cloud.discovery.locator.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@StepScope
@Component(ChangeNginxConfigTasklet.COMPONENT_NAME)
public class ChangeNginxConfigTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "changeNginxConfigTasklet";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        // Interact with file to change old nginx.conf to new nginx.conf
        return null;
    }
}
