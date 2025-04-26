package com.demo.poc.commons.core.logging;

import com.demo.poc.commons.core.tracing.enums.TraceParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;

import static com.demo.poc.commons.core.logging.ThreadContextInjector.RestConstants.METHOD;
import static com.demo.poc.commons.core.logging.ThreadContextInjector.RestConstants.URI;
import static com.demo.poc.commons.core.logging.enums.LoggingType.REST_CLIENT_REQ;

@Slf4j
@RequiredArgsConstructor
public class ThreadContextErrorInjector {

  private final ThreadContextInjector injector;

  public void populateFromException(Throwable ex, ServerWebExchange exchange) {
    if (ex instanceof WebClientRequestException webClientRequestException) {
      ThreadContext.put(REST_CLIENT_REQ.getCode() + METHOD, webClientRequestException.getMethod().toString());
      ThreadContext.put(REST_CLIENT_REQ.getCode() + URI, webClientRequestException.getUri().toString());
    }

    injector.populateFromHeaders(TraceParam.Util.extractTraceHeadersAsMap(exchange.getRequest().getHeaders()::getFirst));
    log.error(ex.getMessage(), ex);
    ThreadContext.clearAll();
  }
}
