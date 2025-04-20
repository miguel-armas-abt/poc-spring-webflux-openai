package com.demo.poc.entrypoint.report.utils;

import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.demo.poc.entrypoint.report.dto.aggregate.ReportDto;
import com.demo.poc.entrypoint.report.enums.AreaType;
import com.demo.poc.entrypoint.report.enums.ConclusionType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportUtils {

  public static final UnaryOperator<String> cleanResponse = response ->
      response
          .replaceAll("```json\\n", StringUtils.EMPTY)
          .replaceAll("\\n```", StringUtils.EMPTY);

  public static final Function<ReportDto, Map<String, String>> mapReportData = report -> Map.of(
      AreaType.COGNITIVE_AREA.name(), report.getCognitiveAreaReport(),
      AreaType.CONDUCT_AREA.name(), report.getConductAreaReport(),
      AreaType.EMOTIONAL_AREA.name(), report.getEmotionalAreaReport(),
      AreaType.SOCIAL_AREA.name(), report.getSocialAreaReport(),
      ConclusionType.OBSERVATIONS.name(), report.getObservationReport(),
      ConclusionType.SUGGESTIONS.name(), report.getSuggestionReport()
  );
}
