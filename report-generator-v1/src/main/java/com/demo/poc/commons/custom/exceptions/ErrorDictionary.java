package com.demo.poc.commons.custom.exceptions;

import com.demo.poc.commons.core.errors.dto.ErrorType;
import com.demo.poc.commons.core.errors.exceptions.GenericException;
import com.demo.poc.commons.core.errors.exceptions.InvalidFieldException;
import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientErrorExtractorException;
import com.demo.poc.commons.core.errors.exceptions.NoSuchRestClientException;
import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static com.demo.poc.commons.core.errors.dto.ErrorType.BUSINESS;
import static com.demo.poc.commons.core.errors.dto.ErrorType.SYSTEM;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum ErrorDictionary {

  //system: 01.00.xx
  UNEXPECTED_SSL_EXCEPTION("01.00.01", "Unexpected SSL error for HTTP client", SYSTEM, INTERNAL_SERVER_ERROR, UnexpectedSslException.class),
  INVALID_STREAMING_DATA("01.00.02", "Streaming data is not processable", SYSTEM, INTERNAL_SERVER_ERROR, InvalidStreamingData.class),
  ERROR_READING_JSON("01.00.03", "Error reading JSON", SYSTEM, INTERNAL_SERVER_ERROR, JsonReadException.class),

  //no such properties and components: 01.01.xx
  NO_SUCH_REST_CLIENT("01.01.01", "No such rest client", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientException.class),
  NO_SUCH_REST_CLIENT_ERROR_EXTRACTOR("01.01.02", "No such rest client error extractor",SYSTEM, INTERNAL_SERVER_ERROR, NoSuchRestClientErrorExtractorException.class),
  NO_SUCH_PARAM_MAPPER("01.01.03", "No such param mapper", SYSTEM, INTERNAL_SERVER_ERROR, NoSuchParamMapperException.class),
  NO_SUCH_AREA_TYPE_REPORTER("01.01.04", "No such area type reporter", SYSTEM, BAD_REQUEST, NoSuchAreaTypeReporterException.class),
  NO_SUCH_CONCLUSION_TYPE_REPORTER("01.01.05", "No such conclusion type reporter", SYSTEM, BAD_REQUEST, NoSuchConclusionTypeReporterException.class),

  //business and bad requests: 01.02.xx
  INVALID_FIELD("01.02.01", "Invalid field", BUSINESS, BAD_REQUEST, InvalidFieldException.class),
  ERROR_READING_DOCX("01.01.01", "Error reading DOCX", SYSTEM, INTERNAL_SERVER_ERROR, DocxReadException.class),
  FORM_IMAGE_IS_REQUIRED("01.01.02", "Form image is required", BUSINESS, BAD_REQUEST, FormImageIsRequiredException.class),;

  private final String code;
  private final String message;
  private final ErrorType type;
  private final HttpStatus httpStatus;
  private final Class<? extends GenericException> exceptionClass;

  public static ErrorDictionary parse(Class<? extends GenericException> exceptionClass) {
    return Arrays.stream(ErrorDictionary.values())
        .filter(errorDetail -> errorDetail.getExceptionClass().isAssignableFrom(exceptionClass))
        .findFirst()
        .orElseThrow(() -> new GenericException("No such exception"));
  }
}
