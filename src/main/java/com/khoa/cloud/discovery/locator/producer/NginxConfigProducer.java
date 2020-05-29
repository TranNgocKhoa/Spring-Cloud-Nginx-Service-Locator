package com.khoa.cloud.discovery.locator.producer;

import com.khoa.cloud.discovery.locator.model.ServiceInstance;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NginxConfigProducer {
    private final ResourceLoader resourceLoader;

    @Value("${nginx.config}")
    public String nginxConfigTemplateLocation;

    public NginxConfigProducer(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getConfigTemplate() {
        try {
            Resource resource = resourceLoader.getResource(nginxConfigTemplateLocation);
            Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getListServer(List<ServiceInstance> instances) {
        return instances.stream()
                .map(instance -> instance.getIpAddr() + ":" + instance.getPort())
                .collect(Collectors.toList());
    }
}
