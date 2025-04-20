package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import lombok.Getter;

@Getter
public class FormImageIsRequiredException extends GenericException {

  public FormImageIsRequiredException() {
    super(ErrorDictionary.FORM_IMAGE_IS_REQUIRED.getMessage(), ErrorDictionary.parse(FormImageIsRequiredException.class));
  }
}
