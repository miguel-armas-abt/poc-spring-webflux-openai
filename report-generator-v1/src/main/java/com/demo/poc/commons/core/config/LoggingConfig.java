package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.logging.ThreadContextErrorInjector;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.ThreadContextRestClientInjector;
import com.demo.poc.commons.core.logging.ThreadContextRestServerInjector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

  @Bean
  public ThreadContextInjector threadContextInjector() {
    return new ThreadContextInjector();
  }

  @Bean
  public ThreadContextErrorInjector threadContextErrorInjector(ThreadContextInjector context) {
    return new ThreadContextErrorInjector(context);
  }

  @Bean
  public ThreadContextRestClientInjector threadContextRestClientInjector(ThreadContextInjector context) {
    return new ThreadContextRestClientInjector(context);
  }

  @Bean
  public ThreadContextRestServerInjector threadContextRestServerInjector(ThreadContextInjector context) {
    return new ThreadContextRestServerInjector(context);
  }
}