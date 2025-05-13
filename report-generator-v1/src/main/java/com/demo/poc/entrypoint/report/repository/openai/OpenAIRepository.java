package com.demo.poc.entrypoint.report.repository.openai;

import java.util.Map;

import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.commons.core.restclient.error.RestClientErrorHandler;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.report.repository.openai.config.OpenAIProperties;
import com.demo.poc.entrypoint.report.repository.openai.error.OpenAIError;
import com.demo.poc.entrypoint.report.repository.openai.mapper.OpenAIRequestMapper;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.request.ChatRequestWrapper;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.response.ChatResponseWrapper;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
public class OpenAIRepository {

  private static final String SERVICE_NAME = "openai-completion";

  private final RestClientErrorHandler errorHandler;
  private final WebClient webClient;
  private final RestClient restClient;
  private final OpenAIRequestMapper openAIMapper;
  private final OpenAIProperties openAIProperties;

  public OpenAIRepository(ApplicationProperties properties,
                          RestClientErrorHandler errorHandler,
                          WebClientFactory webClientFactory,
                          OpenAIRequestMapper openAIMapper,
                          OpenAIProperties openAIProperties) {
    this.errorHandler = errorHandler;
    this.openAIMapper = openAIMapper;
    this.openAIProperties = openAIProperties;

    this.restClient = properties.searchRestClient(SERVICE_NAME);
    this.webClient = webClientFactory.createWebClient(restClient.getPerformance(), SERVICE_NAME);
  }

  public Mono<ChatResponseWrapper> analyzeImage(Map<String, String> headers,
                                                String prompt,
                                                String imageUrl) {
    ChatRequestWrapper request = openAIMapper.toAnalyzeImageRequest(openAIProperties.getMaxTokens(), openAIProperties.getTemperature(), prompt, imageUrl);
    return webClient.post()
        .uri(this.restClient.getRequest().getEndpoint())
        .headers(x -> fillHeaders(this.restClient.getRequest().getHeaders(), headers).accept(x))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(ChatResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  public Mono<ChatResponseWrapper> analyzeText(Map<String, String> headers,
                                               String prompt) {
    return webClient.post()
        .uri(this.restClient.getRequest().getEndpoint())
        .headers(x -> fillHeaders(this.restClient.getRequest().getHeaders(), headers).accept(x))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(openAIMapper.toAnalyzeTextRequest(openAIProperties.getMaxTokens(), openAIProperties.getTemperature(), prompt)))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(ChatResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody);
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return errorHandler.handleError(clientResponse, OpenAIError.class, SERVICE_NAME);
  }

}
