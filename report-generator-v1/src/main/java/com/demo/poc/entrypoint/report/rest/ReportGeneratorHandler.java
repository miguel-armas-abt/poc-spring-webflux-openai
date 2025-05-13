package com.demo.poc.entrypoint.report.rest;

import com.demo.poc.commons.core.restserver.utils.RestServerUtils;
import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;

import java.util.Map;

import com.demo.poc.entrypoint.report.dto.request.ReportRequestDto;
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
import reactor.util.function.Tuple2;

@Component
@RequiredArgsConstructor
public class ReportGeneratorHandler {

  private final ParamValidator paramValidator;
  private final BodyValidator bodyValidator;
  private final ReportGeneratorService reportGeneratorService;

  public Mono<ServerResponse> generateReport(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(serverRequest.bodyToMono(ReportRequestDto.class))
        .map(Tuple2::getT2)
        .flatMap(bodyValidator::validateAndGet)
        .flatMap(body -> reportGeneratorService.generateReport(headers, body))
        .flatMap(bytes -> {
          DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
          return ServerResponse.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=psicologia.docx")
              .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
              .bodyValue(buffer);
        });
  }
}
