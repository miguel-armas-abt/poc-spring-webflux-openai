package com.demo.poc.entrypoint.report.reporter.conclusion;

import com.demo.poc.commons.custom.exceptions.NoSuchConclusionTypeReporterException;
import com.demo.poc.entrypoint.report.dto.aggregate.ReportAggregate;
import com.demo.poc.entrypoint.report.dto.prompt.Prompt;
import com.demo.poc.entrypoint.report.enums.ConclusionType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface ConclusionReporter {

  Mono<String> generateReport(Map<String, String> headers, ReportAggregate aggregate);

  boolean supports(ConclusionType conclusionType);

  static ConclusionReporter selectReporter(List<ConclusionReporter> reporters, ConclusionType conclusionType) {
    return reporters.stream()
        .filter(reporter -> reporter.supports(conclusionType))
        .findFirst()
        .orElseThrow(NoSuchConclusionTypeReporterException::new);
  }

  static String buildPrompt(Prompt prompt, ReportAggregate aggregate) {
    return prompt.buildPrompt()
        + "\n Los reportes por cada área son los siguientes: "
        + "\n" + aggregate.getCognitiveAreaReport()
        + "\n" + aggregate.getConductAreaReport()
        + "\n" + aggregate.getEmotionalAreaReport()
        + "\n" + aggregate.getSocialAreaReport();
  }
}
