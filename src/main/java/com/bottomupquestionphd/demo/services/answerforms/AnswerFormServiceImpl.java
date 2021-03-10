package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormAlreadyFilledOutByCurrentUserException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFilledOutException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFoundException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerFormServiceImpl implements AnswerFormService {
    private final AnswerFormRepository answerFormRepository;
    private final QuestionFormService questionFormService;
    private final AppUserService appUserService;
    private final AnswerService answerService;

    public AnswerFormServiceImpl(AnswerFormRepository answerFormRepository, QuestionFormService questionFormService, AppUserService appUserService, AnswerService answerService) {
        this.answerFormRepository = answerFormRepository;
        this.questionFormService = questionFormService;
        this.appUserService = appUserService;
        this.answerService = answerService;
    }

    @Override
    public CreateAnswerFormDTO createAnswerForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        AppUser currentUser = appUserService.findCurrentlyLoggedInUser();
        checkIfUserHasFilledOutAnswerForm(questionFormId, currentUser.getId());

        return new CreateAnswerFormDTO(0, questionFormId, currentUser.getId(), questionForm.getQuestions());
    }

    @Override
    public void saveAnswerForm(AnswerForm answerForm, long answerFormId, long questionFormId, long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, AnswerFormAlreadyFilledOutByCurrentUserException {
        appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
        AppUser appUser = appUserService.findById(appUserId);

        checkIfUserHasFilledOutAnswerForm(questionFormId, appUserId);
        appUser.addOneAnswerForm(answerForm);
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        answerForm.setAppUser(appUser);
        answerForm.setQuestionForm(questionForm);
        checkForEmptyAnswerText(answerForm);

        answerService.connectAnswersToActualAnswers(answerForm.getAnswers());
        answerService.connectQuestionsToAnswers(answerForm.getAnswers(), questionForm.getId());
        connectAnswerFormToAnswers(answerForm);
        answerFormRepository.save(answerForm);
    }

    private void connectAnswerFormToAnswers(AnswerForm answerForm) {
        for (Answer answer : answerForm.getAnswers()) {
            answer.setAnswerForm(answerForm);
        }
    }

    private void checkForEmptyAnswerText(AnswerForm answerForm) throws MissingParamsException {
        for (Answer answer : answerForm.getAnswers()) {
            for (ActualAnswerText actualAnswerText : answer.getActualAnswerTexts()) {
                if (actualAnswerText.getAnswerText() == null || actualAnswerText.getAnswerText().isEmpty()) {
                    throw new MissingParamsException("All the fields should be filled out!");
                }
            }
        }
    }

    public AnswerForm findAnswerFormById(long answerFormId) throws NoSuchAnswerformById {
        return (AnswerForm) answerFormRepository.findById(answerFormId).orElseThrow(() -> new NoSuchAnswerformById("Couldn't find answerform belonging to the given id"));
    }

    @Override
    public List<AppUsersQuestionFormsDTO> findQuestionFormsFilledOutByAppUserId(long appUserId) throws BelongToAnotherUserException {
        appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
        return answerFormRepository.findAllQuestionFormsFilledOutByUser(appUserId);
    }

    @Override
    public List<Long> findQuestionFormIdsFilledOutByUser(long appUserId) {
        return answerFormRepository.findAllQuestionFormIdsFilledOutByAppUser(appUserId);
    }

    @Override
    public CreateAnswerFormDTO updateAnswerForm(long questionFormId, long answerFormId, long appUserId) throws BelongToAnotherUserException, QuestionFormNotFoundException, MissingUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
        appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        checkIfUserHasFilledOutAnswerForm(questionFormId, appUserId);
        AnswerForm answerForm = questionForm.getAnswerForms()
                .stream()
                .filter(a -> a.getId() == answerFormId)
                .findFirst().orElse(null);

        CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(questionFormId, appUserId, answerFormId, questionForm.getQuestions(), answerForm.getAnswers());
        return createAnswerFormDTO;
    }

    @Override
    public boolean checkIfUserHasFilledOutAnswerForm(long questionFormId, long appUserId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        if (questionForm == null) {
            throw new QuestionFormNotFoundException("Couldn't find questionform");
        }
        boolean hasUserFilledOutGivenAnswerForm =
                questionForm
                        .getAnswerForms()
                        .stream()
                        .filter(form -> form.getAppUser() != null)
                        .anyMatch(form -> form.getAppUser().getId() == appUserId);
        if (hasUserFilledOutGivenAnswerForm) {
            throw new AnswerFormAlreadyFilledOutByCurrentUserException("You have already filled out this question form, please update the existing one");
        }
        return hasUserFilledOutGivenAnswerForm;
    }

    private boolean findAnswerFormBelongingToQuestionFormById(long answerFormId, QuestionForm questionForm) {
        for (AnswerForm answerform : questionForm.getAnswerForms()) {
            if (answerform.getId() == answerFormId) {
                return true;
            }
        }
        return false;
    }

    private AnswerForm findAnswerFormBelongingToUserByAGivenQuestionForm(QuestionForm questionForm, AppUser currentUser) throws AnswerFormNotFoundException {
        return currentUser
                .getAnswerForms()
                .stream()
                .filter(form -> findAnswerFormBelongingToQuestionFormById(form.getId(), questionForm))
                .findFirst()
                .orElseThrow(() -> new AnswerFormNotFoundException("Couldn't find answerform with the given id"));
    }
}
