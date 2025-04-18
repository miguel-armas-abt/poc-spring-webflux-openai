package com.demo.poc.entrypoint.report.rest;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.entrypoint.report.enums.AreaType;

import java.util.Map;

import com.demo.poc.entrypoint.report.service.ReportGeneratorService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class ReportGeneratorHandler {

  private final HeaderValidator headerValidator;
  private final ReportGeneratorService reportGeneratorService;

  public Mono<ServerResponse> generateReport(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    return serverRequest.multipartData()
        .flatMap(parts -> {
          AreaType.validate(parts.toSingleValueMap());
          return reportGeneratorService.generateReport(headers, parts.toSingleValueMap());
        })
        .flatMap(bytes -> {
          DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
          return ServerResponse.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=psicologia.docx")
              .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
              .bodyValue(buffer);
        });
  }
}
