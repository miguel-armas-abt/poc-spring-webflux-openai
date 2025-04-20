package com.demo.poc.entrypoint.report.service;

import com.demo.poc.commons.core.serialization.JsonSerializer;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.report.dto.aggregate.ReportDto;
import com.demo.poc.entrypoint.report.dto.prompt.Prompt;
import com.demo.poc.entrypoint.report.helper.DocxGeneratorHelper;
import com.demo.poc.entrypoint.report.repository.openai.OpenAIRepository;
import com.demo.poc.entrypoint.report.utils.ReportUtils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportGeneratorServiceImpl implements ReportGeneratorService {

  private static final String TEMPLATE_FILE = "docx-template";
  private static final String PROMPT_FILE = "prompt";

  private final JsonSerializer jsonSerializer;
  private final OpenAIRepository openAIRepository;
  private final DocxGeneratorHelper docxGenerator;
  private final ApplicationProperties properties;
  private final Gson gson;

  @Override
  public Mono<byte[]> generateReport(Map<String, String> headers, FilePart formImage) {
    Map<String, String> files = properties.getFilePaths();
    String promptFilePath = files.get(PROMPT_FILE);
    String templateFilePath = files.get(TEMPLATE_FILE);

    return jsonSerializer.readElementFromFile(promptFilePath, Prompt.class)
        .flatMap(prompt -> openAIRepository.analyzeImage(headers, prompt.buildPrompt(), formImage))
        .map(response -> response.getChoices().get(0).getMessage().getContent())
        .map(response -> gson.fromJson(ReportUtils.cleanResponse.apply(response), ReportDto.class))
        .flatMap(report -> docxGenerator.generateReport(templateFilePath, ReportUtils.mapReportData.apply(report)));
  }
}
