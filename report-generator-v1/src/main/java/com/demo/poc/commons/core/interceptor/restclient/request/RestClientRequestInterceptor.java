package com.demo.poc.commons.core.interceptor.restclient.request;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestClientRequestInterceptor implements ExchangeFilterFunction {

  private final ThreadContextInjector contextInjector;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    ClientRequest decorated = ClientRequest.from(request)
        .body((httpRequest, context) -> request.body()
                .insert(new RestClientRequestDecorator(httpRequest, contextInjector, request), context))
        .build();

    return next.exchange(decorated);
  }
}