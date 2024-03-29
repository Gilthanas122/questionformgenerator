package com.bottomupquestionphd.demo.exceptions;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormHasNotQuestionsException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.exceptions.question.*;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.exceptions.textanswervote.NoActualAnswerTextsToVoteForException;
import com.bottomupquestionphd.demo.exceptions.textanswervote.TextAnswerVotesMissMatchException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.NestedServletException;

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
  public @ResponseBody
  ErrorMessageDTO handleInvalidRegexParameterException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(MissingParamsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleMissingParamsException(final Exception exception) {
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

  @ExceptionHandler(UserAlreadyDisabledException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleAlreadyDisabledException(final Exception exception) {
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

  @ExceptionHandler(QuestionFormHasNotQuestionsException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleQuestionFormHasNotQuestionsException(final Exception exception) {
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

  @ExceptionHandler(QuestionHasBeenAnsweredException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleQuestionHasBeenAnsweredException(final Exception exception) {
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

  @ExceptionHandler(NumberOfQuestionAndAnswersShouldMatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleNumberOfQuestionAndAnswersShouldMatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AnswerFormNumberOfAnswersShouldMatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleAnswerFormNumberOfAnswersShouldMatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(AnswerNotFoundByIdException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleAnswerNotFoundByIdException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //TextAnswerVotes
  @ExceptionHandler(TextAnswerVotesMissMatchException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleTextAnswerVotesMissMatchException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NoActualAnswerTextsToVoteForException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessageDTO handleNoActualAnswerTextsToVoteForException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //Answer Form Filtering Exceptions
  @ExceptionHandler(QuestionFormFilteringException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public @ResponseBody
  ErrorMessageDTO handleQuestionFormFilteringException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  //Random Exception

  @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleUnAuthorizedException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(NestedServletException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public @ResponseBody
  ErrorMessageDTO handleAccessDeniedException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(HttpClientErrorException.Forbidden.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public @ResponseBody
  ErrorMessageDTO handleForbiddenException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody
  ErrorMessageDTO handleRandomException(final Exception exception) {
    log.error(exception.getMessage());
    return ErrorServiceImpl.defaultExceptionResponse(exception);
  }
}
