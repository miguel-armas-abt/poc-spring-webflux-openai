package com.demo.poc.entrypoint.report.reporter.conclusion;

import com.demo.poc.commons.core.serialization.JsonSerializer;
import com.demo.poc.commons.custom.constants.FileConstants;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.report.dto.aggregate.ReportAggregate;
import com.demo.poc.entrypoint.report.dto.prompt.Prompt;
import com.demo.poc.entrypoint.report.enums.ConclusionType;
import com.demo.poc.entrypoint.report.repository.openai.OpenAIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ObservationReporter implements ConclusionReporter {

  private final JsonSerializer jsonSerializer;
  private final OpenAIRepository openAIRepository;
  private final ApplicationProperties properties;

  @Override
  public Mono<String> generateReport(Map<String, String> headers, ReportAggregate aggregate) {
    return jsonSerializer.readListFromFile(properties.getFilePaths().get(FileConstants.PROMPTS_FILE_PROPERTY), Prompt.class)
        .filter(prompt -> ConclusionType.OBSERVATIONS.getLabel().equals(prompt.getLabel()))
        .single()
        .flatMap(prompt -> openAIRepository.analyzeText(headers, ConclusionReporter.buildPrompt(prompt, aggregate)))
        .map(response -> {
          String report = response.getChoices().get(0).getMessage().getContent();
          aggregate.setObservationReport(report);
          return report;
        });
  }

  @Override
  public boolean supports(ConclusionType conclusionType) {
    return ConclusionType.OBSERVATIONS.equals(conclusionType);
  }
}