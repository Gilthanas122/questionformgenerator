package com.bottomupquestionphd.demo.exceptions;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


  @ExceptionHandler(MissingParamsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleMissingParamsException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }
}
