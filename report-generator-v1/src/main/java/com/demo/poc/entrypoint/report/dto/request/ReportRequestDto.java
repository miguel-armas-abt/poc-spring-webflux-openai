package com.demo.poc.entrypoint.report.dto.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReportRequestDto implements Serializable {

  @NotEmpty
  private String imageUrl;
}