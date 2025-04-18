package com.demo.poc.entrypoint.report.reporter.area;

import com.demo.poc.commons.custom.exceptions.NoSuchAreaTypeReporterException;
import com.demo.poc.entrypoint.report.dto.aggregate.ReportAggregate;
import com.demo.poc.entrypoint.report.enums.AreaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface AreaTypeReporter {

  Mono<String> generateReport(Map<String, String> headers, FilePart image, ReportAggregate aggregate);

  boolean supports(AreaType areaType);

  static AreaTypeReporter selectReporter(List<AreaTypeReporter> reporters, AreaType areaType) {
    return reporters.stream()
        .filter(reporter -> reporter.supports(areaType))
        .findFirst()
        .orElseThrow(NoSuchAreaTypeReporterException::new);
  }
}
