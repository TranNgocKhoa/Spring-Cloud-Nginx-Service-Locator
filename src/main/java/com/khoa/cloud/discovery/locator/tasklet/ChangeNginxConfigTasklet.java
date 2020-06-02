package com.khoa.cloud.discovery.locator.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@StepScope
@Component(ChangeNginxConfigTasklet.COMPONENT_NAME)
public class ChangeNginxConfigTasklet implements Tasklet {
    public final static String COMPONENT_NAME = "changeNginxConfigTasklet";

    @Value("${nginx.load-balancing-config-file}")
    private String nginxConfigFile;

    @Value("${nginx.proxy-config-file}")
    private String nginxProxyConfigFile;

    @Value("#{jobExecutionContext[syncServiceConstants.SERVICES_LOAD_BALANCING_CONFIG]}")
    private String serviceLoadBalancingConfig;

    @Value("#{jobExecutionContext[syncServiceConstants.SERVICES_PROXY_ROUTING_CONFIG]}")
    private String servicesProxyRoutingConfig;

    @Override
    public RepeatStatus execute(@NonNull StepContribution stepContribution, @NonNull ChunkContext chunkContext) throws Exception {
        this.writeFile(nginxConfigFile, serviceLoadBalancingConfig);
        this.writeFile(nginxProxyConfigFile, servicesProxyRoutingConfig);

        return RepeatStatus.FINISHED;
    }

    private void writeFile(String nginxConfigFile, String serviceLoadBalancingConfig) {
        try (FileWriter fileWriter = new FileWriter(nginxConfigFile, false)) {
            fileWriter.write(serviceLoadBalancingConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
