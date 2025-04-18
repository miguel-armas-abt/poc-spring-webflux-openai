package com.demo.poc.entrypoint.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConclusionType {

  OBSERVATIONS("observations"),
  SUGGESTIONS("suggestions"),;

  private final String label;
}
