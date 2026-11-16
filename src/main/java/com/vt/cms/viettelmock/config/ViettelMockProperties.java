package com.vt.cms.viettelmock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "viettel.mock")
public class ViettelMockProperties {
    private boolean enabled = false;
    private String baseUrl = "http://localhost:8080";
    private String webhookUrl = "http://localhost:8080/internal/webhooks/viettelpost";
    private String webhookToken = "";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public String getWebhookUrl() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    public String getWebhookToken() { return webhookToken; }
    public void setWebhookToken(String webhookToken) { this.webhookToken = webhookToken; }
}