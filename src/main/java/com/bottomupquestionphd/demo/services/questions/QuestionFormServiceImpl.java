package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.MyUserDetails;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QuestionFormServiceImpl implements QuestionFormService{

  private final QuestionFormRepository questionFormRepository;
  private final AppUserService appUserService;

  public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository, AppUserService appUserService) {
    this.questionFormRepository = questionFormRepository;
    this.appUserService = appUserService;
  }

  @Override
  public long save(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    AppUser appUser = appUserService.findByUsername(myUserDetails.getUsername());

    QuestionForm questionForm = validateQuestionCreateDTO(questionFormCreateDTO);
    questionForm.setAppUser(appUser);
    questionFormRepository.save(questionForm);
    return questionForm.getId();
  }

  @Override
  public QuestionForm findById(long id) throws QuestionFormNotFoundException {
    return questionFormRepository.findById(id).orElseThrow(() ->new QuestionFormNotFoundException("Question form doesn't exist with the provided id"));
  }

  private QuestionForm validateQuestionCreateDTO(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException {
    if (questionFormCreateDTO.getDescription() == null || questionFormCreateDTO.getDescription().isEmpty()
  || questionFormCreateDTO.getName() == null || questionFormCreateDTO.getName().isEmpty()){
      throw new MissingParamsException("Following parameter(s) are missing: name, description");
    }else if (questionFormRepository.existsByName(questionFormCreateDTO.getName())){
      throw new QuestionFormNameAlreadyExistsException("A questionform with this name" + questionFormCreateDTO.getName() + "already exists.");
    }
    return new QuestionForm(questionFormCreateDTO.getName(), questionFormCreateDTO.getDescription());
  }
}
