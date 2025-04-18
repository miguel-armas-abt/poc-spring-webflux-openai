package com.demo.poc.commons.custom.properties.restclient;

import java.util.Map;

import com.demo.poc.commons.core.tracing.enums.TraceParamType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeaderTemplate {

  private Map<String, String> provided;
  private Map<String, TraceParamType> generated;
  private Map<String, String> forwarded;

}
