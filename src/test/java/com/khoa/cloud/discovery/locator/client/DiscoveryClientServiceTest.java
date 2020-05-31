package com.khoa.cloud.discovery.locator.client;

import com.khoa.cloud.discovery.locator.model.Application;
import com.khoa.cloud.discovery.locator.model.ServiceInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
class DiscoveryClientServiceTest {

    @Autowired
    DiscoveryClientService discoveryClientService;

    @Test
    void getServiceInstances() {
        List<Application> serviceInstances = discoveryClientService.getApplicationList();

        Assertions.assertEquals(4, serviceInstances.size());

    }
}