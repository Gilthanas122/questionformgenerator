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
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionFormServiceImpl implements QuestionFormService {

    private final QuestionFormRepository questionFormRepository;
    private final AppUserService appUserService;
    private final QuestionConversionService questionConversionService;
    private final QueryService queryService;


    public QuestionFormServiceImpl(QuestionFormRepository questionFormRepository, AppUserService appUserService, QuestionConversionService questionConversionService, QueryService queryService) {
        this.questionFormRepository = questionFormRepository;
        this.appUserService = appUserService;
        this.questionConversionService = questionConversionService;
        this.queryService = queryService;
    }

    @Override
    public long save(QuestionForm questionForm) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException {
        if (questionForm.getDescription() == null || questionForm.getName() == null || questionForm.getDescription().isEmpty() || questionForm.getName().isEmpty()) {
            throw new MissingParamsException("Following field(s) are missing: name, description");
        } else if (questionFormRepository.existsByName(questionForm.getName())) {
            throw new QuestionFormNameAlreadyExistsException("There is a question form with the provided name");
        }
        AppUser appUser = appUserService.findCurrentlyLoggedInUser();

        questionForm.setAppUser(appUser);
        questionFormRepository.save(questionForm);
        return questionForm.getId();
    }

    @Override
    public QuestionForm findById(long id) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException {
        checkIfQuestionFormExists(id);
        QuestionForm questionForm = questionFormRepository.findById(id);
        if (questionForm.getAppUser() == null) {
            throw new MissingUserException("No user belonging to the question form");
        }
        appUserService.checkIfCurrentUserMatchesUserIdInPath(questionForm.getAppUser().getId());
        return questionForm;
    }

    private void checkIfQuestionFormExists(long questionFormid) throws QuestionFormNotFoundException {
        if (!questionFormRepository.existsById(questionFormid)) {
            throw new QuestionFormNotFoundException("Question form doesn't exist with the provided id");
        }
    }

    @Override
    public List<QuestionForm> findAll() throws NoQuestionFormsInDatabaseException {
        long id = appUserService.findCurrentlyLoggedInUser().getId();
        List<QuestionForm> questionForms = questionFormRepository.findAllByAppUserId(id);
        if (questionForms.size() < 1) {
            throw new NoQuestionFormsInDatabaseException("No question form belonging to the user.");
        }
        return questionForms;
    }

    @Override
    public void finishQuestionForm(long id) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NotEnoughQuestionsToCreateFormException {
        QuestionForm questionForm = findById(id);
        if (questionForm.getQuestions().size() < 1) {
            throw new NotEnoughQuestionsToCreateFormException("There should be at least one question to submit a form");
        }
        questionForm.setFinished(true);
        questionFormRepository.save(questionForm);
    }

    @Override
    public List<QuestionWithDTypeDTO> findByIdAndAddQuestionType(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
        QuestionForm questionForm = findById(questionFormId);
        List<QuestionWithDTypeDTO> questionWithDTypeDTOS = new ArrayList<>();
        for (Question question : questionForm.getQuestions()) {
            QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);
            questionWithDTypeDTOS.add(questionWithDTypeDTO);
        }
        return questionWithDTypeDTOS;
    }

    @Override
    public Question findQuestionToSwitchPositionWith(QuestionForm questionForm, int currentPosition, String direction) throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
        if (direction.equals("up")) {
            return questionForm
                    .getQuestions()
                    .stream()
                    .filter(question -> question.getListPosition() == currentPosition - 1)
                    .findFirst()
                    .orElseThrow(() -> new InvalidQuestionPositionException("Not possible to move element more forward. Is the first element"));
        } else if (direction.equals("down")) {
            return questionForm
                    .getQuestions()
                    .stream()
                    .filter(question -> question.getListPosition() == currentPosition + 1)
                    .findFirst()
                    .orElseThrow(() -> new InvalidQuestionPositionException("Not possible to move element more backwards. Is the last element"));
        }
        throw new InvalidQuestionPositionChangeException("Not valid parameter provided for changing the position");
    }

    @Override
    public void updateQuestionListPositionAfterDeletingQuestion(QuestionForm questionForm) {
        for (int i = 0; i < questionForm.getQuestions().size() - 1; i++) {
            if (questionForm.getQuestions().get(i).getListPosition() + 1 < questionForm.getQuestions().get(i + 1).getListPosition()) {
                questionForm.getQuestions().get(i + 1).setListPosition(questionForm.getQuestions().get(i).getListPosition() + 1);
            }
            questionFormRepository.save(questionForm);
        }

    }

    @Override
    public QuestionForm findByIdForAnswerForm(long questionFormId) throws QuestionFormNotFoundException {
        QuestionForm questionForm = questionFormRepository.findById(questionFormId);
        if (questionForm == null){
            throw new QuestionFormNotFoundException("Couldn't find questionform with the given id");
        }
        return questionForm;
    }

    @Override
    public void deleteQuestionForm(long questionFormId) throws QuestionFormNotFoundException {
        checkIfQuestionFormExists(questionFormId);
        questionFormRepository.deleteQuestionFormById(questionFormId);
        queryService.deleteQuestionsBelongingToQuestionForm(questionFormId);
    }

    @Override
    public List<Long> getAllTextQuestionIdsFromQuestionForm(QuestionForm questionForm){
        List<Long> textQuestionIds = questionForm
                .getQuestions()
                .stream()
                .filter(q -> q.getDiscriminatorValue().equals("TextQuestion"))
                .map(Question::getId)
                .collect(Collectors.toList());
        return textQuestionIds;
    }

}
