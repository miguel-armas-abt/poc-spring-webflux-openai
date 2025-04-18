package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class DocxReadException extends GenericException {

  public DocxReadException(String message) {
    super(message, ErrorDictionary.parse(DocxReadException.class));
  }
}
