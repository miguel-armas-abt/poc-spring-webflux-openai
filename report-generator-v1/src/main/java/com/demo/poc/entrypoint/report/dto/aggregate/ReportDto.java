package com.demo.poc.entrypoint.report.dto.aggregate;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto implements Serializable {

  private String cognitiveAreaReport;
  private String conductAreaReport;
  private String emotionalAreaReport;
  private String socialAreaReport;
  private String observationReport;
  private String suggestionReport;
}
