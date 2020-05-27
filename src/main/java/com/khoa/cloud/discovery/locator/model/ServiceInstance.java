package com.khoa.cloud.discovery.locator.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

public class ServiceInstance implements Serializable {
    private String instanceId;
    private String host;
    private String ipAddr;
    private int port;
    private boolean isSecure;
    private URI uri;
    private Map<String, String> metadata;
    private String scheme;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(Map<String, Object> port) {
        this.port = (int) port.get("$");
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "instanceId='" + instanceId + '\'' +
                ", host='" + host + '\'' +
                ", ipAddr='" + ipAddr + '\'' +
                ", port=" + port +
                ", isSecure=" + isSecure +
                ", uri=" + uri +
                ", metadata=" + metadata +
                ", scheme='" + scheme + '\'' +
                '}';
    }
}
