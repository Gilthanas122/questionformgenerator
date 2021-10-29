package com.bottomupquestionphd.demo.services.answers.textanswervotes;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.TextAnswerVote;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.textanswervote.ReceiveTextAnswerVotesDTO;
import com.bottomupquestionphd.demo.domains.dtos.textanswervote.TextAnswerVoteForVotingDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.exceptions.textanswervote.NoActualAnswerTextsToVoteForException;
import com.bottomupquestionphd.demo.exceptions.textanswervote.TextAnswerVotesMissMatchException;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import com.bottomupquestionphd.demo.repositories.TextAnswerVoteRepository;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TextAnswerVoteServiceImpl implements TextAnswerVoteService {
  private final TextAnswerVoteRepository textAnswerVoteRepository;
  private final AppUserService appUserService;
  private final QuestionFormService questionFormService;
  private final AnswerService answerService;
  private final QueryService queryService;
  private final ActualAnswerTextRepository actualAnswerTextRepository;

  public TextAnswerVoteServiceImpl(TextAnswerVoteRepository textAnswerVoteRepository, AppUserService appUserService, QuestionFormService questionFormService, AnswerService answerService, QueryService queryService, ActualAnswerTextRepository actualAnswerTextRepository) {
    this.textAnswerVoteRepository = textAnswerVoteRepository;
    this.appUserService = appUserService;
    this.questionFormService = questionFormService;
    this.answerService = answerService;
    this.queryService = queryService;
    this.actualAnswerTextRepository = actualAnswerTextRepository;
  }

  //NOT TESTED
  @Override
  public TextAnswerVoteForVotingDTO returnAnswersNotVotedByCurrentUser(long appUserid, long questionFormId, long answerFormId) throws BelongToAnotherUserException, MissingUserException, QuestionFormNotFoundException, MissingParamsException, NoActualAnswerTextsToVoteForException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserid);
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    List<Question> textQuestions = questionForm.getQuestions()
            .stream()
            .filter(q -> q.getDiscriminatorValue()
                    .equals("TextQuestion"))
            .filter(q -> questionHasAnswersFilledOutByOtherUser(q, appUserid))
            .collect(Collectors.toList());
    if (textQuestions.size() < 1) {
      throw new NoActualAnswerTextsToVoteForException("No other user filled out this questionform so far or you voted for all the available answers");
    }
    List<Question> questionsRandomlyChoosen = chooseQuestionsRandomly(textQuestions);
    questionForm.setQuestions(questionsRandomlyChoosen);
    List<Long> textQuestionIds = questionFormService.getAllTextQuestionIdsFromQuestionForm(questionForm);
    List<Answer> filteredAnswerRemovedOwn = answerService.removeOwnAATextsFromAATToBeVoted(appUserid,
            getAllAnswersBelongingToAQuestion(textQuestionIds));
    return new TextAnswerVoteForVotingDTO(answerFormId, questionFormId, appUserid, textQuestions, filteredAnswerRemovedOwn);
  }

  // NOT TESTED
  private List<Question> chooseQuestionsRandomly(List<Question> textQuestions) {
    Random rand = new Random();
    int randomNumberOfQuestions = Math.min(textQuestions.size(), 5);
    if (randomNumberOfQuestions == 0) {
      List<Question> questionsEmpty = new ArrayList<>();
      return questionsEmpty;
    }
    return rand
            .ints(randomNumberOfQuestions, 0, textQuestions.size())
            .mapToObj(textQuestions::get)
            .collect(Collectors.toList());
  }

  //NOT TESTED
  @Override
  @Transactional
  public void saveVotes(ReceiveTextAnswerVotesDTO receiveTextAnswerVotesDTO, long appUserid, long questionFormId, long answerFormId) throws BelongToAnotherUserException, TextAnswerVotesMissMatchException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserid);
    List<ActualAnswerText> actualAnswerTexts = queryService.findActualAnswerTexts(receiveTextAnswerVotesDTO.getActualAnswerTextIds());
    List<TextAnswerVote> textAnswerVotes = createTextAnswerVotesFromVotes(receiveTextAnswerVotesDTO.getTextAnswerVotes());
    if (actualAnswerTexts.size() != textAnswerVotes.size()) {
      throw new TextAnswerVotesMissMatchException("The number of actualanswertexts and the belonging textsanswervotes should be equal");
    }
    for (int i = 0; i < actualAnswerTexts.size(); i++) {
      if (textAnswerVotes.get(i).getValue() != 0) {
        TextAnswerVote actualTextAnswerVote = textAnswerVotes.get(i);
        ActualAnswerText actualAnswerText = actualAnswerTexts.get(i);
        actualAnswerText.addOneTextAnswerVote(actualTextAnswerVote);
        actualTextAnswerVote.setActualAnswerText(actualAnswerText);
      }
    }
    actualAnswerTextRepository.saveAll(actualAnswerTexts);
  }

  private List<TextAnswerVote> createTextAnswerVotesFromVotes(List<List<Byte>> textAnswerVotesBytes) {
    List<TextAnswerVote> textAnswerVotes = new ArrayList<>();
    for (List<Byte> textanswerVotesList : textAnswerVotesBytes) {
      for (Byte textAnswerVote : textanswerVotesList) {
        TextAnswerVote answerVote = new TextAnswerVote(textAnswerVote);
        textAnswerVotes.add(answerVote);
      }
    }
    return textAnswerVotes;
  }

  private List<Answer> getAllAnswersBelongingToAQuestion(List<Long> questionIds) throws MissingParamsException {
    return answerService.findAllAnswerTextsBelongingToAQuestion(questionIds);
  }

  private boolean questionHasAnswersFilledOutByOtherUser(Question question, long appUserId) {
    return question
            .getAnswers()
            .stream()
            .anyMatch(answer -> {
              try {
                return answer.getAnswerForm().getAppUser().getId() != appUserId && answerService.removeOwnAATextsFromAATToBeVoted(appUserId, question.getAnswers()).size() > 0;
              } catch (MissingParamsException e) {
                e.printStackTrace();
              }
              return false;
            });
  }
}
