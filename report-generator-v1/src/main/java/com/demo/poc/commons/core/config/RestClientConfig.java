package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.restclient.WebClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.List;

@Configuration
public class RestClientConfig {

  @Bean
  public WebClientFactory webClient(List<ExchangeFilterFunction> filters) {
    return new WebClientFactory(filters);
  }
}
