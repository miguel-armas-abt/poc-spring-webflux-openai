package com.demo.poc.commons.custom.properties;

import java.util.Map;

import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ApplicationProperties extends ConfigurationBaseProperties {

  private Map<String, String> filePaths;
}