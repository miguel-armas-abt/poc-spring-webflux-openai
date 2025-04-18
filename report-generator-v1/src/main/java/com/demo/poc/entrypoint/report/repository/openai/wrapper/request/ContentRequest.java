package com.demo.poc.entrypoint.report.repository.openai.wrapper.request;

import java.io.Serializable;

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
public class ContentRequest implements Serializable {

  private String type;

  private String text;

  @JsonProperty("image_url")
  private ImageUrlRequest imageUrl;
}
