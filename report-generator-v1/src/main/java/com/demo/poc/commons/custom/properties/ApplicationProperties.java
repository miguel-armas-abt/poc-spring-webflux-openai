package com.demo.poc.commons.custom.properties;

import java.util.Map;
import java.util.Optional;

import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.custom.properties.restclient.HeaderTemplate;
import com.demo.poc.commons.custom.properties.restclient.PerformanceTemplate;
import com.demo.poc.commons.custom.properties.restclient.RestClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ApplicationProperties {

  private Map<String, String> errorMessages;

  private Map<String, RestClient> restClients;

  private Map<String, String> filePaths;

  public String searchEndpoint(String serviceName) {
    return searchRestClient(serviceName).getEndpoint();
  }

  public PerformanceTemplate searchPerformance(String serviceName) {
    return searchRestClient(serviceName).getPerformance();
  }

  public HeaderTemplate searchHeaderTemplate(String serviceName) {
    return this.getRestClients().get(serviceName).getHeaders();
  }

  private RestClient searchRestClient(String serviceName) {
    return Optional.ofNullable(restClients.get(serviceName))
        .orElseThrow(NoSuchRestClientException::new);
  }
}
