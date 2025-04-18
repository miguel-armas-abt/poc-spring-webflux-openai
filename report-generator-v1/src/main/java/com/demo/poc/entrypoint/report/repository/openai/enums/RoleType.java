package com.demo.poc.entrypoint.report.repository.openai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

  USER("user"),
  DEVELOPER("developer");

  private final String label;
}
