package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.NoQuestionFormsInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;
  private final AppUserService appUserService;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository, AppUserService appUserService) {
    this.questionFormRepository = questionFormRepository;
    this.appUserService = appUserService;
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
}
