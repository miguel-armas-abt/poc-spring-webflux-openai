package com.demo.poc.entrypoint.report.reporter.conclusion;

import com.demo.poc.commons.core.serialization.JsonSerializer;
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
public class SuggestionReporter implements ConclusionReporter {

  private final JsonSerializer jsonSerializer;
  private final OpenAIRepository openAIRepository;

  @Override
  public Mono<String> generateReport(Map<String, String> headers, ReportAggregate aggregate) {
    return jsonSerializer.readListFromFile("prompts/prompts.json", Prompt.class)
        .filter(prompt -> ConclusionType.SUGGESTIONS.getLabel().equals(prompt.getLabel()))
        .single()
        .flatMap(prompt -> openAIRepository.analyzeText(headers, ConclusionReporter.buildPrompt(prompt, aggregate)))
        .map(response -> {
          String report = response.getChoices().get(0).getMessage().getContent();
          aggregate.setSuggestionReport(report);
          return report;
        });
  }

  @Override
  public boolean supports(ConclusionType conclusionType) {
    return ConclusionType.SUGGESTIONS.equals(conclusionType);
  }
}