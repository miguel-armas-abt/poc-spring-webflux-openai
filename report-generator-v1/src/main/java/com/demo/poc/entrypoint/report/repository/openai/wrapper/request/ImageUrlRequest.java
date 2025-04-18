package com.demo.poc.entrypoint.report.repository.openai.wrapper.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageUrlRequest implements Serializable {

  private String url;
}
