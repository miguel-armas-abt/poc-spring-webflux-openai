package com.demo.poc.entrypoint.report.rest;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.entrypoint.report.repository.openai.OpenAIRepository;
import com.demo.poc.commons.core.restserver.ServerResponseBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class ReportGeneratorHandler {

  private final OpenAIRepository openAIRepository;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> generateReport(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    // recover image from ServerRequest here!
    FilePart filePart = null;

    // the prompt is write here
    String prompt = "Analiza la siguiente imagen e indicame que puedes ver";

    return serverRequest.multipartData()
        .flatMap(parts -> {
          FilePart imageFile = (FilePart) parts.toSingleValueMap().get("image");

          if (Objects.isNull(imageFile)) {
            return ServerResponse.badRequest().bodyValue("Missing 'image' part");
          }

          return openAIRepository.analyzeImage(headers, prompt, imageFile)
              .flatMap(response -> ServerResponseBuilder.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
        });
  }
}
