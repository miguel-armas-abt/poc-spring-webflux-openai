package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class NoSuchConclusionTypeReporterException extends GenericException {

  public NoSuchConclusionTypeReporterException() {
    super(ErrorDictionary.NO_SUCH_AREA_TYPE_REPORTER.getMessage(), ErrorDictionary.parse(NoSuchConclusionTypeReporterException.class));
  }
}
