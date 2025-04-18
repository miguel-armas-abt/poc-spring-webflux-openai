package com.demo.poc.entrypoint.report.dto.aggregate;

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
public class ReportAggregate {

  private String cognitiveAreaReport;
  private String conductAreaReport;
  private String emotionalAreaReport;
  private String socialAreaReport;

  private String observationReport;
  private String suggestionReport;
}
