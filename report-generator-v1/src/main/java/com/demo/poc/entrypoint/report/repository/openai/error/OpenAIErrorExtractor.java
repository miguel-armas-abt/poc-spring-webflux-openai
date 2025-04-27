package com.demo.poc.entrypoint.report.repository.openai.error;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.demo.poc.commons.core.restclient.error.RestClientErrorExtractor;
import com.demo.poc.commons.core.restclient.error.RestClientErrorMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OpenAIErrorExtractor implements RestClientErrorExtractor {

  private final RestClientErrorMapper mapper;

  @Override
  public Optional<Map.Entry<String, String>> getCodeAndMessage(String jsonBody) {
    return mapper.getCodeAndMessage(jsonBody, OpenAIError.class, errorMapper);
  }

  private final Function<OpenAIError, Map.Entry<String, String>> errorMapper = errorDetail
      -> Map.of(errorDetail.getError().getCode(), errorDetail.getError().getMessage()).entrySet().iterator().next();

  @Override
  public boolean supports(Class<?> wrapperClass) {
    return wrapperClass.isAssignableFrom(OpenAIError.class);
  }

}
