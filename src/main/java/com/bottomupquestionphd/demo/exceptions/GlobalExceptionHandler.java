package com.bottomupquestionphd.demo.exceptions;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormAlreadyFilledOutByCurrentUserException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFilledOutException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.answerform.NoUserFilledOutAnswerFormException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidInputFormatException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleUsernameNotFoundException(final Exception exception) {
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

  @ExceptionHandler(InvalidChangePasswordException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleInvalidChangePasswordException(final Exception exception) {
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

  // Question Controller
  @ExceptionHandler(InvalidInputFormatException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleInvalidInputFormatException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(QuestionNotFoundByIdException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleQuestionNotFoundByIdException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(TypeMismatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleTypeMismatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(InvalidQuestionPositionChangeException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleInvalidQuestionPositionChangeException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(InvalidQuestionPositionException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleInvalidQuestionPositionException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //AnswerForm Controller

  @ExceptionHandler(AnswerFormAlreadyFilledOutByCurrentUserException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleAnswerFormAlreadyFilledOutByCurrentUserException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AnswerFormNotFilledOutException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleAnswerFormNotFilledOutException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoSuchAnswerformById.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoSuchAnswerformById(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(PassWordMissMachException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handlePassWordMissMachException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoUserFilledOutAnswerFormException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleNoUserFilledOutAnswerFormException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }


  @ExceptionHandler(QuestionTypesAndQuestionTextsSizeMissMatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleQuestionTypesAndQuestionTextsSizeMissMatchExceptionException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

}
