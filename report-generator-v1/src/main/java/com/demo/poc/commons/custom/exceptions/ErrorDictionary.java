package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
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
  INVALID_FIELD("01.00.01", "Invalid field", BAD_REQUEST, InvalidFieldException.class),
  NO_SUCH_REST_CLIENT("01.00.02", "No such rest client", INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  ERROR_READING_JSON("01.00.03", "Error reading JSON", INTERNAL_SERVER_ERROR, JsonReadException.class),
  INVALID_STREAMING_DATA("01.00.04", "Streaming data is not processable", INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  UNEXPECTED_SSL_EXCEPTION("01.00.05", "Unexpected SSL error for HTTP client", INTERNAL_SERVER_ERROR, UnexpectedSslException.class),
  NO_SUCH_PARAM_MAPPER("10.00.06", "No such param mapper", BAD_REQUEST, NoSuchParamMapperException.class),

  //custom=01
  ERROR_READING_DOCX("01.01.01", "Error reading DOCX", INTERNAL_SERVER_ERROR, DocxReadException.class),
  FORM_IMAGE_IS_REQUIRED("01.01.02", "Form image is required", BAD_REQUEST, FormImageIsRequiredException.class),
  NO_SUCH_AREA_TYPE_REPORTER("01.01.03", "No such area type reporter", BAD_REQUEST, NoSuchAreaTypeReporterException.class),
  NO_SUCH_CONCLUSION_TYPE_REPORTER("01.01.04", "No such conclusion type reporter", BAD_REQUEST, NoSuchConclusionTypeReporterException.class),;

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
