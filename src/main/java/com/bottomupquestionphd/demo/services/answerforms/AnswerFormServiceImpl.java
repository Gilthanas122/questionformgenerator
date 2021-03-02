package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.error.ErrorService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;


@Service
public class AnswerFormServiceImpl implements AnswerFormService {
    private final AnswerFormRepository answerFormRepository;
    private final QuestionFormService questionFormService;
    private final AppUserService appUserService;

    public AnswerFormServiceImpl(AnswerFormRepository answerFormRepository, QuestionFormService questionFormService, AppUserService appUserService) {
        this.answerFormRepository = answerFormRepository;
        this.questionFormService = questionFormService;
        this.appUserService = appUserService;
    }

    @Override
    public CreateAnswerFormDTO createFirstAnswerForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, NoSuchUserByIdException, BelongToAnotherUserException {
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        AppUser currentUser = appUserService.findCurrentlyLoggedInUser();
        boolean hasTheUserAlreadyFilledOutThisQuestionForm = checkIfUserHasFilledOutAnswerForm(questionFormId, currentUser);
        if (!hasTheUserAlreadyFilledOutThisQuestionForm) {
            return new CreateAnswerFormDTO(0, questionFormId, currentUser.getId(), questionForm.getQuestions());
        }
        AnswerForm answerForm = currentUser.getAnswerForm();
        return new CreateAnswerFormDTO(answerForm.getId(), questionFormId, currentUser.getId(), questionForm.getQuestions(), answerForm.getAnswers());
    }


    @Override
    public void saveAnswerForm(AnswerForm answerForm, long answerFormId, long questionFormId, long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException {
        AppUser appUser = appUserService.findById(appUserId);
        QuestionForm questionForm = questionFormService.findById(questionFormId);
        answerForm.setAppUser(appUser);
        answerForm.setQuestionForm(questionForm);
        checkForEmptyAnswerText(answerForm);
        answerFormRepository.save(answerForm);

    }

    private void checkForEmptyAnswerText(AnswerForm answerForm) throws MissingParamsException {
        for (Answer answer : answerForm.getAnswers()) {
            for (ActualAnswerText actualAnswerText : answer.getActualAnswerTexts()) {
                if (actualAnswerText.getAnswerText().isEmpty() || actualAnswerText.getAnswerText() == null) {
                    throw new MissingParamsException("All the fields should be filled out!");
                }
            }
        }
    }

    public AnswerForm findAnswerFormById(long answerFormId) throws NoSuchAnswerformById {
        return answerFormRepository.findById(answerFormId).orElseThrow(() -> new NoSuchAnswerformById("Couldn't find answerform belonging to the given id"));
    }

    @Override
    public CreateAnswerFormDTO convertAnswerFormToCreateAnswerFormDTO(long questionFormId, AnswerForm answerForm) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
        QuestionForm questionForm = questionFormService.findById(questionFormId);
        AppUser appUser = appUserService.findCurrentlyLoggedInUser();
        return new CreateAnswerFormDTO(answerForm.getId(), questionFormId, appUser.getId(), questionForm.getQuestions(), answerForm.getAnswers());
    }

    @Override
    public boolean checkIfUserHasFilledOutAnswerForm(long questionFormId, AppUser currentUser) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
        QuestionForm questionForm = questionFormService.findById(questionFormId);
        return
                questionForm
                        .getAnswerForms()
                        .stream()
                        .filter(form -> form.getAppUser() != null)
                        .anyMatch(form -> form.getAppUser().getId() == currentUser.getId());
    }


}
