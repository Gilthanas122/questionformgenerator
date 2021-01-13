package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;
  private final AppUserService appUserService;
  private final QuestionConversionService questionConversionService;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository, AppUserService appUserService, QuestionConversionService questionConversionService) {
    this.questionFormRepository = questionFormRepository;
    this.appUserService = appUserService;
    this.questionConversionService = questionConversionService;
  }

  @Override
  public long save(QuestionForm questionForm) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException {
    if (questionForm.getDescription() == null || questionForm.getName() == null || questionForm.getDescription().isEmpty() || questionForm.getName().isEmpty()){
      throw new MissingParamsException("Following field(s) are missing: name, description");
    }else if (questionFormRepository.existsByName(questionForm.getName())){
      throw new QuestionFormNameAlreadyExistsException("There is a question form with the provided name");
    }
    AppUser appUser = appUserService.findCurrentlyLoggedInUser();

    questionForm.setAppUser(appUser);
    questionFormRepository.save(questionForm);
    return questionForm.getId();
  }

  @Override
  public QuestionForm findById(long id) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
    QuestionForm questionForm = questionFormRepository.findById(id).orElseThrow(() ->new QuestionFormNotFoundException("Question form doesn't exist with the provided id"));
    if (questionForm.getAppUser() == null){
      throw new MissingUserException("No user belonging to the question form");
    } else if (questionForm.getAppUser().getId() != appUserService.findCurrentlyLoggedInUser().getId()){
      throw new BelongToAnotherUserException("Given forms belongs to another user");
    }
    return questionForm;
  }

  @Override
  public List<QuestionForm> findAll() throws NoQuestionFormsInDatabaseException {
    long id = appUserService.findCurrentlyLoggedInUser().getId();
    List<QuestionForm> questionForms = questionFormRepository.findAllByAppUserId(id);
    if (questionForms.size() < 1){
      throw new NoQuestionFormsInDatabaseException("No question form belonging to the user.");
    }
    return questionForms;
  }

  @Override
  public void updateQuestionForm(QuestionForm questionForm) throws QuestionFormNotFoundException, BelongToAnotherUserException {
    questionFormRepository.save(questionForm);
  }

  @Override
  public void finishQuestionForm(long id) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NotEnoughQuestionsToCreateFormException {
      QuestionForm questionForm = findById(id);
      if (questionForm.getQuestions().size() < 1){
        throw new NotEnoughQuestionsToCreateFormException("There should be at least one question to submit a form");
      }
      questionForm.setFinished(true);
      questionFormRepository.save(questionForm);
  }

  @Override
  public List<QuestionWithDTypeDTO> findByIdAndAddQuestionType(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = findById(questionFormId);
    List<QuestionWithDTypeDTO> questionWithDTypeDTOS = new ArrayList<>();
    for (Question question: questionForm.getQuestions()) {
      QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);
      questionWithDTypeDTOS.add(questionWithDTypeDTO);
    }
    return questionWithDTypeDTOS;
  }

  @Override
  public Question findQuestionToSwitchPositionWith(QuestionForm questionForm, int currentPosition, String direction) throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    if (direction.equals("up")){
      return questionForm
        .getQuestions()
        .stream()
        .filter(question -> question.getListPosition() < currentPosition)
        .findFirst()
        .orElseThrow(() -> new InvalidQuestionPositionException("Not possible to move element more forward. Is the first element"));
    }else if (direction.equals("down")){
      return questionForm
        .getQuestions()
        .stream()
        .filter(question -> question.getListPosition() > currentPosition)
        .findFirst()
        .orElseThrow(() -> new InvalidQuestionPositionException("Not possible to move element more backwards. Is the last element"));
    }
      throw new InvalidQuestionPositionChangeException("Not valid parameter provided for changing the position");
  }
}
