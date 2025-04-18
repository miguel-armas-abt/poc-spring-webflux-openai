package com.demo.poc.entrypoint.report.repository.openai;

import java.util.Map;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.exceptions.RestClientException;
import com.demo.poc.commons.core.restclient.WebClientFactory;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.report.repository.openai.mapper.OpenAIRequestMapper;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.response.ChatResponseWrapper;
import com.demo.poc.entrypoint.report.utils.ImageSerializer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.fillHeaders;

@Repository
@RequiredArgsConstructor
public class OpenAIRepository {

  private static final String SERVICE_NAME = "openai-completion";

  private final ApplicationProperties properties;
  private final WebClientFactory webClientFactory;
  private final OpenAIRequestMapper openAIMapper;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    this.webClient = webClientFactory.createWebClient(properties.searchPerformance(SERVICE_NAME), SERVICE_NAME);
  }

  public Mono<ChatResponseWrapper> analyzeImage(Map<String, String> headers,
                                                String prompt,
                                                FilePart imageFile) {
    return ImageSerializer.serializeImageAndGetBase64(imageFile)
        .map(imageInBase64 -> openAIMapper.toAnalyzeImageRequest(300, 0.7, prompt, imageInBase64))
        .flatMap(request -> webClient.post()
        .uri(properties.searchEndpoint(SERVICE_NAME))
        .headers(x -> fillHeaders(properties.searchHeaderTemplate(SERVICE_NAME), headers).accept(x))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request))
        .retrieve()
        .onStatus(HttpStatusCode::isError, this::handleError)
        .toEntity(ChatResponseWrapper.class)
        .mapNotNull(HttpEntity::getBody));
  }

  private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
    return clientResponse.bodyToMono(String.class)
        .flatMap(jsonBody -> StringUtils.EMPTY.equals(jsonBody)
            ? Mono.error(new RestClientException(ErrorDto.CODE_DEFAULT, "Unexpected", HttpStatusCode.valueOf(409)))
            : Mono.error(new RestClientException(ErrorDto.CODE_DEFAULT, jsonBody, clientResponse.statusCode())));
  }

}
