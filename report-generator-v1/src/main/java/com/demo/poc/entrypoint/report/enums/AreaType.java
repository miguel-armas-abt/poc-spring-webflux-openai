package com.demo.poc.entrypoint.report.enums;

import com.demo.poc.commons.custom.exceptions.AreaTypeImageIsRequiredException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.Part;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum AreaType {

  COGNITIVE_AREA("cognitive-area"),
  CONDUCT_AREA("conduct-area"),
  EMOTIONAL_AREA("emotional-area"),
  SOCIAL_AREA("social-area"),;

  private final String label;

  public static void validate(Map<String, Part> images) {
    Arrays.stream(AreaType.values())
        .forEach(areaType -> Optional.ofNullable(images.get(areaType.getLabel()))
            .orElseThrow(() -> new AreaTypeImageIsRequiredException(areaType.getLabel() + " is required")));
  }
}
