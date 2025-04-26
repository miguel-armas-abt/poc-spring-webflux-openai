package com.demo.poc.commons.core.interceptor.restserver;

import java.util.HashMap;

import com.demo.poc.commons.core.logging.RestServerThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestRequestLog;
import com.demo.poc.commons.core.logging.dto.RestResponseLog;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestServerInterceptor implements WebFilter {

  private final RestServerThreadContextInjector restServerContext;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String uri = exchange.getRequest().getURI().toString();
    String traceParent = exchange.getRequest().getHeaders().getFirst(TraceParam.TRACE_PARENT.getKey());

    generateTraceOfRequest(exchange.getRequest());
    return chain.filter(exchange)
        .doOnSuccess(ignore -> generateTraceOfResponse(exchange.getResponse(), uri, traceParent));
    }

    private void generateTraceOfRequest(ServerHttpRequest serverHttpRequest) {
      RestRequestLog log = RestRequestLog.builder()
          .uri(serverHttpRequest.getURI().toString())
          .method(serverHttpRequest.getMethod().toString())
          .requestHeaders(serverHttpRequest.getHeaders().toSingleValueMap())
          .requestBody("ToDo")
          .traceParent(serverHttpRequest.getHeaders().getFirst(TraceParam.TRACE_PARENT.getKey()))
          .build();
      restServerContext.populateFromRestServerRequest(log);
    }

    private void generateTraceOfResponse(ServerHttpResponse serverHttpResponse, String uri, String traceParent) {
      RestResponseLog log = RestResponseLog.builder()
          .uri(uri)
          .responseHeaders(new HashMap<>(serverHttpResponse.getHeaders().toSingleValueMap()))
          .httpCode(serverHttpResponse.getStatusCode().toString())
          .responseBody("ToDo")
          .traceParent(traceParent)
          .build();

      restServerContext.populateFromRestServerResponse(log);
    }
}
