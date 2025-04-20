package com.demo.poc.entrypoint.report.dto.prompt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prompt {

  private List<String> messages;
  private List<String> examples;

  public String buildPrompt() {
    return String.join(" ", this.messages)
        + "\nPor ejemplo: "
        + "\n" + String.join(" ", this.examples);
  }
}
