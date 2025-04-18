package com.demo.poc.entrypoint.report.repository.openai.wrapper.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequestWrapper implements Serializable {

  private String model;

  private List<MessageRequest> messages;

  @JsonProperty("max_tokens")
  private Integer maxTokens;

  @JsonProperty("temperature")
  private Double temperature;


}
