package com.demo.poc.entrypoint.report.rest;

import com.demo.poc.commons.custom.constants.RestConstants;

import static org.springframework.http.MediaType.APPLICATION_NDJSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ReportGeneratorRestService {

  @Bean("reports")
  public RouterFunction<ServerResponse> build(ReportGeneratorHandler reportGeneratorHandler) {
    return nest(
        path(RestConstants.BASE_URI),
        route()
            .POST("/reports", accept(APPLICATION_NDJSON), reportGeneratorHandler::generateReport)
            .build()
    );
  }
}