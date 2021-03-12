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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
        return answerFormRepository.findById(answerFormId).orElseThrow(() -> new NoSuchAnswerformById("Couldn't find answerform belonging to the given id"));
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
    public CreateAnswerFormDTO createAnswerFormToUpdate(long questionFormId, long answerFormId, long appUserId) throws BelongToAnotherUserException, QuestionFormNotFoundException, MissingUserException, AnswerFormNotFilledOutException {
        appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
        AnswerForm answerForm = questionForm.getAnswerForms()
                .stream()
                .filter(a -> a.getId() == answerFormId)
                .findFirst().orElse(null);
        if (answerForm == null) {
            throw new AnswerFormNotFilledOutException("You need to fill out the answerform in order to update it");
        }
       List<Long> textQuestionIds =  questionFormService.getAllTextQuestionIdsFromQuestionForm(questionFormId);

        List<Answer> filteredAnswerRemovedOwn = answerService.removeOwnAATextsFromAATToBeVoted(appUserId, getAllActualAnswerTextsBelongingToAQuestion(textQuestionIds));
        CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(answerFormId, questionFormId, appUserId, questionForm.getQuestions(), answerForm.getAnswers(), filteredAnswerRemovedOwn);
        return createAnswerFormDTO;
    }

    @Override
    public void saveUpdatedAnswerForm(long answerFormId, long appUserId, AnswerForm answerForm) throws NoSuchAnswerformById, BelongToAnotherUserException, NoSuchUserByIdException {
        AnswerForm originalAnswerForm1 = findAnswerFormById(answerFormId);
        if (originalAnswerForm1.getAppUser().getId() != appUserId){
            throw new BelongToAnotherUserException("This answerForm belongs to another user");
        }
        answerForm.setAppUser(originalAnswerForm1.getAppUser());
        answerForm.setQuestionForm(originalAnswerForm1.getQuestionForm());
        answerForm.setId(originalAnswerForm1.getId());
        answerForm.setAnswers(removeNullAnswersFromAnswerForm(answerForm.getAnswers(), answerForm, originalAnswerForm1.getAnswers()));
        answerFormRepository.save(answerForm);
    }

    private List<Answer> removeNullAnswersFromAnswerForm(List<Answer> answers, AnswerForm answerForm, List<Answer> originalFormsAnswers) {
        for (Answer answer: answers) {
            Predicate<ActualAnswerText> isNotEmptyOrNull = item -> item.actualAnswerTextIsNullOrEmpty();
            List<ActualAnswerText> answerTextOfAnswer = answer.getActualAnswerTexts();
            List<ActualAnswerText> actualAnswerTextsFiltered = new ArrayList<>();
                    answer
                    .getActualAnswerTexts()
                    .stream()
                    .filter(isNotEmptyOrNull)
                    .forEach(item ->{
                        actualAnswerTextsFiltered.add(item);
                    });
                 answerTextOfAnswer.removeAll(actualAnswerTextsFiltered);
            answer.setActualAnswerTexts(answerTextOfAnswer);

        }
        answers = answerService.setAnswersToAnswerForm(answerForm, answers, originalFormsAnswers);;
        return answers;
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

    public List<Answer> getAllActualAnswerTextsBelongingToAQuestion(List<Long> questionIds) {
        List<Answer> answersBelongingToAQuestion = answerService.findAllAnswerTextsBelongingToAQuestion(questionIds);
        return answersBelongingToAQuestion;
    }
}
