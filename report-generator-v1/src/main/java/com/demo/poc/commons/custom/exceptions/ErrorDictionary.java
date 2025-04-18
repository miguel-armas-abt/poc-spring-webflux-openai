package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchCacheConfigException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamAssignmentException;
import com.demo.poc.commons.core.errors.exceptions.ReflectiveParamMappingException;
import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system=00
  INVALID_FIELD("10.00.01", "Invalid field", BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT("10.00.02", "No such rest client", INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("10.00.03", "Error reading JSON", INTERNAL_SERVER_ERROR, JsonReadException.class),
  ERROR_MAPPING_REFLECTIVE_PARAMS("10.00.04", "Error mapping reflective params", INTERNAL_SERVER_ERROR, ReflectiveParamMappingException.class),
  ERROR_ASSIGN_REFLECTIVE_PARAMS("10.00.05", "Error assign reflective params", INTERNAL_SERVER_ERROR, ReflectiveParamAssignmentException.class),
  INVALID_STREAMING_DATA("10.00.06", "Streaming data is not processable", INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  UNEXPECTED_SSL_EXCEPTION("10.00.07", "Unexpected SSL error for HTTP client", INTERNAL_SERVER_ERROR, UnexpectedSslException.class),;

  //custom=01

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
  private final Class<? extends GenericException> exceptionClass;

  public static ErrorDictionary parse(Class<? extends GenericException> exceptionClass) {
    return Arrays.stream(ErrorDictionary.values())
            .filter(errorDetail -> errorDetail.getExceptionClass().isAssignableFrom(exceptionClass))
            .findFirst()
            .orElseThrow(() -> new GenericException("No such exception"));
  }
}
