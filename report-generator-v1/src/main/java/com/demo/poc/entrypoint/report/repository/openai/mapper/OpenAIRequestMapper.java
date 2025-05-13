package com.demo.poc.entrypoint.report.repository.openai.mapper;

import java.util.List;

import com.demo.poc.entrypoint.report.repository.openai.enums.ContentType;
import com.demo.poc.entrypoint.report.repository.openai.enums.ModelType;
import com.demo.poc.entrypoint.report.repository.openai.enums.RoleType;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.request.ChatRequestWrapper;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.request.ContentRequest;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.request.ImageUrlRequest;
import com.demo.poc.entrypoint.report.repository.openai.wrapper.request.MessageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", imports = ModelType.class)
public interface OpenAIRequestMapper {

  @Mapping(target = "model", expression = "java(ModelType.GPT_4_1.getLabel())")
  @Mapping(target = "maxTokens", source = "maxTokens")
  @Mapping(target = "temperature", source = "temperature")
  @Mapping(target = "messages", source = "prompt", qualifiedByName = "buildAnalyzeTextMessage")
  ChatRequestWrapper toAnalyzeTextRequest(Integer maxTokens, Double temperature, String prompt);

  default ChatRequestWrapper toAnalyzeImageRequest(Integer maxTokens, Double temperature, String prompt, String imageUrl) {
    return ChatRequestWrapper.builder()
        .model(ModelType.GPT_4_1_MINI.getLabel())
        .maxTokens(maxTokens)
        .temperature(temperature)
        .messages(buildAnalyzeImageMessage(prompt, imageUrl))
        .build();
  }

  @Named("buildAnalyzeTextMessage")
  static List<MessageRequest> buildAnalyzeTextMessage(String prompt) {
    return List.of(
        MessageRequest.builder()
            .role(RoleType.USER.getLabel())
            .content(List.of(
                ContentRequest.builder()
                    .type(ContentType.TEXT.getLabel())
                    .text(prompt)
                    .build()
            ))
            .build()
    );
  }

  private List<MessageRequest> buildAnalyzeImageMessage(String prompt, String imageUrl) {
    return List.of(
        MessageRequest.builder()
            .role(RoleType.USER.getLabel())
            .content(List.of(
                ContentRequest.builder()
                    .type(ContentType.TEXT.getLabel())
                    .text(prompt)
                    .build(),
                ContentRequest.builder()
                    .type(ContentType.IMAGE.getLabel())
                    .imageUrl(ImageUrlRequest.builder()
                        .url(imageUrl)
                        .build())
                    .build()
            ))
            .build()
    );
  }
}
