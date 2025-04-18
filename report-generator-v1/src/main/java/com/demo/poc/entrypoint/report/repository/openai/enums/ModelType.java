package com.demo.poc.entrypoint.report.repository.openai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelType {

  GPT_4_1_MINI("gpt-4.1-mini"),
  GPT_4_1("gpt-4.1");

  private final String label;

}
