package com.khoa.cloud.discovery.locator.tasklet;

import com.khoa.cloud.discovery.locator.constant.NginxConfigTemplateConstants;
import com.khoa.cloud.discovery.locator.constant.SyncServiceConstants;
import com.khoa.cloud.discovery.locator.model.Application;
import com.khoa.cloud.discovery.locator.producer.NginxConfigProducer;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
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
    private List<Application> applicationList;

    public GenerateNginxConfigTasklet(NginxConfigProducer nginxConfigProducer) {
        this.nginxConfigProducer = nginxConfigProducer;
    }

    @Override
    public RepeatStatus execute(@NonNull StepContribution stepContribution, @NonNull ChunkContext chunkContext) {
        if (CollectionUtils.isEmpty(applicationList)) {
            return RepeatStatus.FINISHED;
        }

        this.setLoadBalancingConfig(chunkContext);
        this.setProxyRoutingConfig(chunkContext);

        return RepeatStatus.FINISHED;
    }

    private void setLoadBalancingConfig(ChunkContext chunkContext) {
        var serviceLoadBalancingConfigBuilder = new StringBuilder();
        String loadBalancingTemplate = nginxConfigProducer.getLoadBalancingTemplate();

        for (Application application : applicationList) {
            serviceLoadBalancingConfigBuilder.append(this.generateLoadBalancingConfigForApplication(application, loadBalancingTemplate))
                    .append("\n");
        }

        chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext()
                .put(SyncServiceConstants.SERVICES_LOAD_BALANCING_CONFIG, serviceLoadBalancingConfigBuilder.toString());
    }

    private void setProxyRoutingConfig(ChunkContext chunkContext) {
        var locationBuilder = new StringBuilder();
        String routeConfigTemplate = nginxConfigProducer.getRouteConfigTemplate();

        for (Application application : applicationList) {
            locationBuilder.append(routeConfigTemplate
                    .replaceAll(NginxConfigTemplateConstants.SERVICE_NAME_PLACE_HOLDER, application.getName()))
                    .append("\n");
        }

        String serverProxyConfig = nginxConfigProducer.getServerConfigTemplate()
                .replace(NginxConfigTemplateConstants.LOCATION_PLACE_HOLDER, locationBuilder.toString());

        chunkContext.getStepContext()
                .getStepExecution().getJobExecution()
                .getExecutionContext()
                .put(SyncServiceConstants.SERVICES_PROXY_ROUTING_CONFIG, serverProxyConfig);
    }

    private String generateLoadBalancingConfigForApplication(Application application, String loadBalancingConfig) {
        loadBalancingConfig = loadBalancingConfig.replaceAll(NginxConfigTemplateConstants.SERVICE_NAME_PLACE_HOLDER, application.getName());

        List<String> listServer = nginxConfigProducer.getListServer(application.getInstance());

        Optional<String> instanceUpstreamConfig = listServer.stream()
                .map(s -> MessageFormat.format("server {0};", s))
                .reduce((result, server) -> String.join(result, server, "\n"));

        loadBalancingConfig = loadBalancingConfig.replace(NginxConfigTemplateConstants.INSTANCE_PLACE_HOLDER, instanceUpstreamConfig.orElse(""));

        return loadBalancingConfig;
    }
}
