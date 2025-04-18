package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class AreaTypeImageIsRequiredException extends GenericException {

  public AreaTypeImageIsRequiredException(String message) {
    super(message, ErrorDictionary.parse(AreaTypeImageIsRequiredException.class));
  }
}
