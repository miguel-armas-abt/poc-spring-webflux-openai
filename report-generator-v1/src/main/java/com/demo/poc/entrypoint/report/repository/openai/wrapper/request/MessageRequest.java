package com.demo.poc.entrypoint.report.repository.openai.wrapper.request;

import java.io.Serializable;
import java.util.List;

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
public class MessageRequest implements Serializable {

  private String role;
  private List<ContentRequest> content;
}
