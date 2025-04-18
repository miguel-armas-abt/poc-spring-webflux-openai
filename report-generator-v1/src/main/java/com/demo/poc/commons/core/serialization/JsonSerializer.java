package com.demo.poc.commons.core.serialization;

import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JsonSerializer {

  private final ObjectMapper objectMapper;

  @PostConstruct
  private void configureObjectMapper() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  public <T> Mono<T> readElementFromFile(String filePath, Class<T> objectClass) {
    return Mono.fromCallable(() -> {
          Resource resource = new ClassPathResource(filePath);
          try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, objectClass);
          }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorMap(IOException.class, ex -> new JsonReadException(ex.getMessage()));
  }

  @SuppressWarnings("unchecked")
  public <T> Flux<T> readListFromFile(String filePath, Class<T> objectClass) {
    return Mono.fromCallable(() -> {
          Resource resource = new ClassPathResource(filePath);
          try (InputStream inputStream = resource.getInputStream()) {
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass);
            return (List<T>) objectMapper.readValue(inputStream, collectionType);
          }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorMap(IOException.class, ex -> new JsonReadException(ex.getMessage()))
        .flatMapMany(Flux::fromIterable);
  }
}