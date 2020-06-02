package com.khoa.cloud.discovery.locator.producer;

import com.khoa.cloud.discovery.locator.model.ServiceInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NginxConfigProducer {
    private final ResourceLoader resourceLoader;

    @Value("${nginx.load-balancing-template:classpath:nginx/conf.d/default.conf}")
    private String loadBalancingTemplateLocation;

    @Value("${nginx.server-template:classpath:nginx/conf.d/server.conf}")
    private String serverTemplateLocation;

    @Value("${nginx.proxy-template:classpath:nginx/conf.d/location.conf}")
    private String proxyTemplateLocation;

    public NginxConfigProducer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getLoadBalancingTemplate() {
        return this.readConfigResourceToString(resourceLoader.getResource(loadBalancingTemplateLocation));
    }

    public String getServerConfigTemplate() {
        return this.readConfigResourceToString(resourceLoader.getResource(serverTemplateLocation));
    }

    public String getRouteConfigTemplate() {
        return this.readConfigResourceToString(resourceLoader.getResource(proxyTemplateLocation));
    }

    public List<String> getListServer(List<ServiceInstance> instances) {
        return instances.stream()
                .map(instance -> instance.getIpAddr() + ":" + instance.getPort())
                .collect(Collectors.toList());
    }

    private String readConfigResourceToString(Resource configResource) {
        try {
            InputStreamReader reader = new InputStreamReader(configResource.getInputStream(), StandardCharsets.UTF_8);

            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
