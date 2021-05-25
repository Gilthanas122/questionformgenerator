package com.bottomupquestionphd.demo.exceptions;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
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

  //User Controller
  @ExceptionHandler(EmailAlreadyUsedException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public @ResponseBody
  ErrorMessageDTO handleEmailAlreadyUsedException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(InvalidRegexParameterException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public @ResponseBody ErrorMessageDTO handleInvalidRegexParameterException(final Exception exception){
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(MissingParamsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody ErrorMessageDTO handleMissingParamsException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(UsernameAlreadyTakenException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public @ResponseBody
  ErrorMessageDTO handleUsernameAlreadyTakenException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AppUserNotActivatedException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleAppUserNotActivatedException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoSuchUserNameException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoSuchUserNameException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(InvalidLoginException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleInvalidLoginException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AppUserPasswordMissMatchException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleAppUserPasswordMissMatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(ConfirmationTokenDoesNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleConfirmationTokenDoesNotExistException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoSuchUserByEmailException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoSuchUserByEmailException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //Admin Controller
  @ExceptionHandler(NoUsersInDatabaseException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoUsersInDatabaseException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AppUserIsAlreadyActivatedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleAppUserIsAlreadyActivatedException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(RoleMissMatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleRoleMissMatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoSuchUserByIdException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoSuchUserByIdException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(UserDeactivateException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleUserDeactivateException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //Question Form Controller

  @ExceptionHandler(QuestionFormNameAlreadyExistsException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public @ResponseBody
  ErrorMessageDTO handleQuestionFormNameAlreadyExistsException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoQuestionFormsInDatabaseException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoQuestionFormsInDatabaseException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(QuestionFormNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleQuestionFormNotFoundException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(BelongToAnotherUserException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleBelongToAnotherUserException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(MissingUserException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleMissingUserException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NotEnoughQuestionsToCreateFormException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleNotEnoughQuestionsToCreateFormException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }
}
