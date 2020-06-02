package com.khoa.cloud.discovery.locator.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoa.cloud.discovery.locator.model.Application;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DiscoveryClientService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String discoveryUrl;
    private final String getAllAppsApiUrl;

    public DiscoveryClientService(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${eureka.client.serviceUrl.defaultZone:http://localhost:8761/eureka}") String discoveryUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.discoveryUrl = discoveryUrl;
        this.getAllAppsApiUrl = this.discoveryUrl + "/apps";
    }

    public List<Application> getApplicationList() {
        JsonNode response = restTemplate.getForObject(this.getAllAppsApiUrl, JsonNode.class);

        JsonNode applicationJsonNode = Optional.ofNullable(response)
                .map(responseJson -> responseJson.get("applications"))
                .map(applications -> applications.get("application"))
                .orElse(null);
        try {
            Application[] applications = objectMapper.treeToValue(applicationJsonNode, Application[].class);

            return Arrays.asList(applications);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when parse Discovery Server Response.", e);
        }
    }
}
