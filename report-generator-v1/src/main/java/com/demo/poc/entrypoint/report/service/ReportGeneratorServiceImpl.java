package com.demo.poc.entrypoint.report.service;

import com.demo.poc.entrypoint.report.dto.aggregate.ReportAggregate;
import com.demo.poc.entrypoint.report.enums.AreaType;
import com.demo.poc.entrypoint.report.enums.ConclusionType;
import com.demo.poc.entrypoint.report.helper.DocxGeneratorHelper;
import com.demo.poc.entrypoint.report.reporter.area.AreaTypeReporter;
import com.demo.poc.entrypoint.report.reporter.conclusion.ConclusionReporter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

  private final List<AreaTypeReporter> areaTypeReporters;
  private final List<ConclusionReporter> conclusionTypeReporters;
  private final DocxGeneratorHelper docxGenerator;

  @Override
  public Mono<byte[]> generateReport(Map<String, String> headers, Map<String, Part> images) {
    ReportAggregate aggregate = new ReportAggregate();
    return Mono.when(
        AreaTypeReporter.selectReporter(areaTypeReporters, AreaType.COGNITIVE_AREA)
            .generateReport(headers, (FilePart) images.get(AreaType.COGNITIVE_AREA.getLabel()), aggregate),

        AreaTypeReporter.selectReporter(areaTypeReporters, AreaType.CONDUCT_AREA)
            .generateReport(headers, (FilePart) images.get(AreaType.CONDUCT_AREA.getLabel()), aggregate),

        AreaTypeReporter.selectReporter(areaTypeReporters, AreaType.EMOTIONAL_AREA)
            .generateReport(headers, (FilePart) images.get(AreaType.EMOTIONAL_AREA.getLabel()), aggregate),

        AreaTypeReporter.selectReporter(areaTypeReporters, AreaType.SOCIAL_AREA)
            .generateReport(headers, (FilePart) images.get(AreaType.SOCIAL_AREA.getLabel()), aggregate)
    )
        .thenReturn(aggregate)
        .flatMap(currentAggregate -> Mono.when(
            ConclusionReporter.selectReporter(conclusionTypeReporters, ConclusionType.OBSERVATIONS)
                .generateReport(headers, aggregate),

            ConclusionReporter.selectReporter(conclusionTypeReporters, ConclusionType.SUGGESTIONS)
                .generateReport(headers, aggregate)
        ))
        .thenReturn(aggregate)
        .flatMap(reportAggregate -> {
          Map<String, String> data = Map.of(
              AreaType.COGNITIVE_AREA.name(), reportAggregate.getCognitiveAreaReport(),
              AreaType.CONDUCT_AREA.name(), reportAggregate.getConductAreaReport(),
              AreaType.EMOTIONAL_AREA.name(), reportAggregate.getEmotionalAreaReport(),
              AreaType.SOCIAL_AREA.name(), reportAggregate.getSocialAreaReport(),
              ConclusionType.OBSERVATIONS.name(), "\n" + reportAggregate.getObservationReport(),
              ConclusionType.SUGGESTIONS.name(), "\n" + reportAggregate.getSuggestionReport()
          );

          return docxGenerator.generateReport("/templates/template.docx", data);
        });
  }
}
