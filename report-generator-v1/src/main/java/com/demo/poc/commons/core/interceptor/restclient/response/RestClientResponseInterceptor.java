package com.demo.poc.commons.core.interceptor.restclient.response;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static com.demo.poc.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@Slf4j
@RequiredArgsConstructor
public class RestClientResponseInterceptor implements ExchangeFilterFunction {

  private final ThreadContextInjector threadContextInjector;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    String traceId = request.headers().getFirst(TRACE_ID.getKey());
    String uri = request.url().toString();
    return next.exchange(request)
        .flatMap(clientResponse -> decorateResponse(clientResponse, uri, traceId));
  }

  private Mono<ClientResponse> decorateResponse(ClientResponse clientResponse, String uri, String traceId) {
    return clientResponse
        .bodyToMono(String.class)
        .defaultIfEmpty(StringUtils.EMPTY)
        .flatMap(responseBody -> {
          generateTraceIfLoggerIsPresent(clientResponse, uri, responseBody, traceId);
          return Mono.just(ClientResponse.create(clientResponse.statusCode())
              .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
              .body(responseBody)
              .build());
        });
  }

  private void generateTraceIfLoggerIsPresent(ClientResponse response, String uri, String responseBody, String traceId) {
    generateTrace(response, uri, responseBody, traceId);
  }

  private void generateTrace(ClientResponse response, String uri, String responseBody, String traceId) {
    try {
      Map<String, String> headers = new HashMap<>(response.headers().asHttpHeaders().toSingleValueMap());
      headers.put(TRACE_ID.getKey(), traceId);

      threadContextInjector.populateFromRestClientResponse(
          headers,
          uri,
          responseBody,
          getHttpCode(response));
    } catch (Exception ex) {
      log.error("Error reading response body: {}", ex.getClass(), ex);
    }
  }

  private static String getHttpCode(ClientResponse response) {
    try {
      return response.statusCode().toString();
    } catch (IllegalArgumentException ex) {
      return String.valueOf(response.statusCode());
    }
  }

}