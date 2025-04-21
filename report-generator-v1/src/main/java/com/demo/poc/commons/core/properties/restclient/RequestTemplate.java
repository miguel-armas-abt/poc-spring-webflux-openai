package com.demo.poc.commons.core.properties.restclient;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTemplate {

  private String endpoint;
  private HeaderTemplate headers;
  private Map<String, String> formData;
}
