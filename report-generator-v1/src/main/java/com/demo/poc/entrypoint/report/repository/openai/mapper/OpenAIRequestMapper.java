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

@Mapper(componentModel = "spring")
public interface OpenAIRequestMapper {

  default ChatRequestWrapper toGpt4VisionPreviewRequest(Integer maxTokens, Double temperature, String prompt, String imageInBase64) {
    return ChatRequestWrapper.builder()
        .model(ModelType.GPT_4_1_MINI.getLabel())
        .maxTokens(maxTokens)
        .temperature(temperature)
        .messages(buildGpt4VisionPreviewMessage(prompt, imageInBase64))
        .build();
  }

  private List<MessageRequest> buildGpt4VisionPreviewMessage(String prompt, String imageInBase64) {
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
                        .url("data:image/jpeg;base64," + imageInBase64)
                        .build())
                    .build()
            ))
            .build()
    );
  }
}
