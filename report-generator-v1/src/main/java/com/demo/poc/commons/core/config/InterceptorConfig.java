package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor;
import com.demo.poc.commons.core.logging.ThreadContextErrorInjector;
import com.demo.poc.commons.core.logging.ThreadContextRestClientInjector;
import com.demo.poc.commons.core.logging.ThreadContextRestServerInjector;
import com.demo.poc.commons.core.serialization.ByteSerializer;
import com.demo.poc.commons.custom.properties.ApplicationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {

  @Bean
  public ErrorInterceptor errorInterceptor(ByteSerializer byteSerializer,
                                           ThreadContextErrorInjector context,
                                           ApplicationProperties properties) {
    return new ErrorInterceptor(byteSerializer, context, properties);
  }

  @Bean
  public RestClientRequestInterceptor restClientRequestInterceptor(ThreadContextRestClientInjector context) {
    return new RestClientRequestInterceptor(context);
  }

  @Bean
  public RestClientResponseInterceptor restClientResponseInterceptor(ThreadContextRestClientInjector context) {
    return new RestClientResponseInterceptor(context);
  }

  @Bean
  public RestServerInterceptor restServerInterceptor(ThreadContextRestServerInjector context) {
    return new RestServerInterceptor(context);
  }

}
