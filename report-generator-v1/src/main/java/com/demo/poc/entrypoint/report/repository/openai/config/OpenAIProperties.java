package com.demo.poc.entrypoint.report.repository.openai.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration.rest-clients.openai-completion.configuration")
public class OpenAIProperties {

  private Integer maxTokens;
  private Double temperature;
}
