package com.khoa.cloud.discovery.locator.tasklet;

import com.khoa.cloud.discovery.locator.constant.NginxConfigTemplateConstants;
import com.khoa.cloud.discovery.locator.constant.SyncServiceConstants;
import com.khoa.cloud.discovery.locator.model.Application;
import com.khoa.cloud.discovery.locator.model.ServiceInstance;
import com.khoa.cloud.discovery.locator.producer.NginxConfigProducer;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@StepScope
@Component(GenerateNginxConfigTasklet.COMPONENT_NAME)
public class GenerateNginxConfigTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "generateNginxConfigTasklet";

    private final NginxConfigProducer nginxConfigProducer;

    @Value("#{jobExecutionContext[syncServiceConstants.APPLICATION_LIST]}")
    private List<Application> applications;

    public GenerateNginxConfigTasklet(NginxConfigProducer nginxConfigProducer) {
        this.nginxConfigProducer = nginxConfigProducer;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if (CollectionUtils.isEmpty(applications)) {
            return RepeatStatus.FINISHED;
        }

        StringBuilder serviceLoadBalancingConfigBuilder = new StringBuilder();

        for (Application application : applications) {
            String configTemplateForApplication = nginxConfigProducer.getConfigTemplate();

            configTemplateForApplication = configTemplateForApplication.replace(NginxConfigTemplateConstants.SERVICE_NAME_PLACE_HOLDER, application.getName());
            configTemplateForApplication = configTemplateForApplication.replace(NginxConfigTemplateConstants.SERVER_BAD_GATEWAY_PORT_PLACE_HOLDER, "80");

            List<String> listServer = nginxConfigProducer.getListServer(application.getInstance());

            Optional<String> instanceUpstreamConfig = listServer.stream()
                    .map(s -> MessageFormat.format("server {0},", s))
                    .reduce((result, server) -> String.join(result, server, "\n"));

            configTemplateForApplication = configTemplateForApplication.replace(NginxConfigTemplateConstants.INSTANCE_PLACE_HOLDER, instanceUpstreamConfig.orElse(""));

            serviceLoadBalancingConfigBuilder.append(configTemplateForApplication)
                    .append("\n");
        }

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
                .put(SyncServiceConstants.SERVICES_LOAD_BALANCING_CONFIG, serviceLoadBalancingConfigBuilder.toString());

        return RepeatStatus.FINISHED;
    }
}
