package com.demo.poc.entrypoint.report.reporter.area;

import com.demo.poc.commons.core.serialization.JsonSerializer;
import com.demo.poc.entrypoint.report.dto.aggregate.ReportAggregate;
import com.demo.poc.entrypoint.report.dto.prompt.Prompt;
import com.demo.poc.entrypoint.report.enums.AreaType;
import com.demo.poc.entrypoint.report.repository.openai.OpenAIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SocialAreaReporter implements AreaTypeReporter {

  private final JsonSerializer jsonSerializer;
  private final OpenAIRepository openAIRepository;

  @Override
  public Mono<String> generateReport(Map<String, String> headers, FilePart image, ReportAggregate aggregate) {
    return jsonSerializer.readListFromFile("prompts/prompts.json", Prompt.class)
        .filter(prompt -> AreaType.SOCIAL_AREA.getLabel().equals(prompt.getLabel()))
        .single()
        .flatMap(prompt -> openAIRepository.analyzeImage(headers, prompt.buildPrompt(), image))
        .map(response -> {
          String report = response.getChoices().get(0).getMessage().getContent();
          aggregate.setSocialAreaReport(report);
          return report;
        });
  }

  @Override
  public boolean supports(AreaType areaType) {
    return AreaType.SOCIAL_AREA.equals(areaType);
  }
}
