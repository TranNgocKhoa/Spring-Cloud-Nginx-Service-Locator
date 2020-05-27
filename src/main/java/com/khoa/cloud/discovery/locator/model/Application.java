package com.khoa.cloud.discovery.locator.model;

import java.io.Serializable;
import java.util.List;

public class Application implements Serializable {
    private String name;
    private List<ServiceInstance> instance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceInstance> getInstance() {
        return instance;
    }

    public void setInstance(List<ServiceInstance> instance) {
        this.instance = instance;
    }
}
