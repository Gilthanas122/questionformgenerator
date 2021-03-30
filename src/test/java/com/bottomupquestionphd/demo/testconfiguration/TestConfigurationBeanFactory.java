package com.bottomupquestionphd.demo.testconfiguration;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestConfigurationBeanFactory {

  @Bean(name = {"validUser"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AppUser getAppUser() {
    AppUser fakePlayer = new AppUser
            .Builder()
            .username("validUser")
            .password("Geeks@portal20")
            .emailId("user@domain.com")
            .active(true)
            .build();
    return fakePlayer;
  }

  @Bean(name = {"validAdmin"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AppUser getAdmin() {
    AppUser fakeAdmin = new AppUser
            .Builder()
            .username("validAdmin")
            .password("Geeks@portal20")
            .emailId("user@domain.com")
            .roles("ROLE_ADMIN")
            .build();
    return fakeAdmin;
  }

  @Bean(name = {"validUsers"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<AppUser> getAppUsers() {
    List<AppUser> appUsers = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      AppUser appUser;
      if (i == 0) {
        appUser = getAdmin();
      } else {
        appUser = getAppUser();
      }
      appUsers.add(appUser);
    }
    return appUsers;
  }

  @Bean(name = {"appUsersQuestionFormsDTOs"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<AppUsersQuestionFormsDTO> getAppUserQuestionFormsDTOs(){
    List<AppUsersQuestionFormsDTO> appUsersQuestionFormsDTOS = new ArrayList<>();
    for (int i = 0; i <4 ; i++) {
      AppUsersQuestionFormsDTO appUsersQuestionFormsDTO = getAppUsersQuestionFormsDTO();
      appUsersQuestionFormsDTOS.add(appUsersQuestionFormsDTO);
    }
    return appUsersQuestionFormsDTOS;
  }

  @Bean(name = {"questionFormNotFilledOutByUserDTOs"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<QuestionFormNotFilledOutByUserDTO> getQuestionFormNotFilledOutByUserDTOs(){
   List<QuestionFormNotFilledOutByUserDTO> questionFormNotFilledOutByUserDTOs = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      QuestionFormNotFilledOutByUserDTO questionFormNotFilledOutByUserDTO = new QuestionFormNotFilledOutByUserDTO(i, "name" +i, i * (int) (Math.random()*3+1));
      questionFormNotFilledOutByUserDTOs.add(questionFormNotFilledOutByUserDTO);
    }
    return questionFormNotFilledOutByUserDTOs;
  }

  @Bean(name = {"appUsersQuestionFormsDTO"})
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public AppUsersQuestionFormsDTO getAppUsersQuestionFormsDTO() {
    return new AppUsersQuestionFormsDTO() {
      @Override
      public long getQuestionFormId() {
        return 1;
      }

      @Override
      public String getName() {
        return "name";
      }

      @Override
      public long getAnswerFormId() {
        return 1;
      }

      @Override
      public long getAppUserId() {
        return 1;
      }
    };
  }

  @Bean(name = "validLoginDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AppUserLoginDTO getValidLoginDTO() {
    AppUserLoginDTO loginDTO = new AppUserLoginDTO("validUser", "Geeks@portal20");
    return loginDTO;
  }

  @Bean(name = "validQuestion")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  Question getValidQuestion() {
    Question question = new Question(1, "fakeQuestionText", 0);
    question.setQuestionForm(getQuestionForm());
    question.getQuestionForm().setAppUser(getAppUser());
    return question;
  }

  @Bean(name = "scaleQuestion")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  Question getScaleQuestion() {
    ScaleQuestion question = new ScaleQuestion(1, "fakeQuestionText", 6);
    question.setQuestionForm(getQuestionForm());
    return question;
  }

  @Bean(name = "textQuestion")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  TextQuestion getValidTextQuestion() {
    TextQuestion question = new TextQuestion(1, "fakeTextQuestionText");
    question.setQuestionForm(getQuestionForm());
    return question;
  }

  @Bean(name = "checkboxQuestion")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  CheckBoxQuestion getValidCheckBoxQuestion() {
    CheckBoxQuestion question = new CheckBoxQuestion(1, "fakeQuestionText", List.of(getQuestionTextPossibility(), getQuestionTextPossibility(), getQuestionTextPossibility()));
    question.setQuestionForm(getQuestionForm());
    return question;
  }

  @Bean(name = "radioButtonQuestion")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  RadioButtonQuestion getValidRadioButtonQuestion() {
    RadioButtonQuestion question = new RadioButtonQuestion(1, "fakeQuestionText", List.of(getQuestionTextPossibility(), getQuestionTextPossibility(), getQuestionTextPossibility()));
    question.setQuestionForm(getQuestionForm());
    return question;
  }

  @Bean("questionTextPossibility")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionTextPossibility getQuestionTextPossibility() {
    QuestionTextPossibility questionTextPossibility = new QuestionTextPossibility("questionTextPossibility");
    return questionTextPossibility;
  }

  @Bean(name = "textQuestionWithDTypeDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionWithDTypeDTO getTextQuestionWithDTypeDTO() {
    QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "TextQuestion", "TextQuestionText", null);
    return questionWithDTypeDTO;
  }

  @Bean(name = "scaleQuestionWithDTypeDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionWithDTypeDTO getScaleQuestionWithDTypeDTO() {
    QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Scale", "ScaleQuestionText", 5);
    return questionWithDTypeDTO;
  }

  @Bean(name = "checkboxQuestionWithDTypeDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionWithDTypeDTO getCheckBoxQuestionWithDTypeDTO() {
    QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Check box", "CheckboxQuestionText", 5);
    questionWithDTypeDTO.setQuestionTextPossibilities(getQuestionTextPossibilitiesWithCheckBoxQuestion());
    return questionWithDTypeDTO;
  }

  @Bean(name = "radioQuestionWithDTypeDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionWithDTypeDTO getRadioQuestionWithDTypeDTO() {
    QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Radio button", "RadioQuestionText", 5);
    questionWithDTypeDTO.setQuestionTextPossibilities(getQuestionTextPossibilitiesWithRadioButtonQuestion());
    return questionWithDTypeDTO;
  }

  @Bean(name = "questionForm")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionForm getQuestionForm() {
    QuestionForm questionForm = new QuestionForm("question form name", "question form description");
    questionForm.setQuestions(getListQuestions());
    return questionForm;
  }

  @Bean(name = "questionFormWithAnswerForm")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  QuestionForm getQuestionFormWithAnswerForm() {
    QuestionForm questionForm = new QuestionForm("question form name", "question form description");
    questionForm.setQuestions(getListQuestions());
    questionForm.setAnswerForms(getAnswerFormsWithoutSettingQuestionForm());
    questionForm.setAppUser(getAppUser());
    return questionForm;
  }

  @Bean(name = "questionForms")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<QuestionForm> getQuestionForms() {
    List<QuestionForm> questionForms = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      QuestionForm questionForm = new QuestionForm("question form name" + i, "question form description" + i);
      questionForm.setId(i + 1);
      questionForms.add(questionForm);
      questionForm.setAnswerForms(getAnswerFormsWithoutSettingQuestionForm());
    }
    return questionForms;
  }

  @Bean(name = "answerForm")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  AnswerForm getAnswerForm() {
    AnswerForm answerForm = new AnswerForm(0, getQuestionForm(), getAppUser());
    answerForm.setAnswers(getAnotherListAnswers());
    return answerForm;
  }

  @Bean(name = "answerFormsWithoutSettingQuestionForm")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<AnswerForm> getAnswerFormsWithoutSettingQuestionForm() {
    List<AnswerForm> answerForms = new ArrayList<>();
    for (int i = 0; i <4 ; i++) {
      AnswerForm answerForm = new AnswerForm(0, getQuestionForm(), getAppUser());
      answerForm.setAnswers(getAnotherListAnswers());
      answerForms.add(answerForm);
    }
    return answerForms;
  }

  @Bean(name = "checkBoxAnswer")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  Answer getCheckBoxAnswer() {
    Answer answer = new Answer(0, getActualAnswerTexts(), getAnswerForm(), getValidCheckBoxQuestion());
    return answer;
  }

  @Bean(name = "radioButtonAnswer")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  Answer getRadioButtonAnswer() {
    Answer answer = new Answer(0, getActualAnswerTexts(), getAnswerForm(), getValidRadioButtonQuestion());
    return answer;
  }

  @Bean(name = "actualAnswerTexts")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  List<ActualAnswerText> getActualAnswerTexts() {
    List<ActualAnswerText> actualAnswerTexts = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      actualAnswerTexts.add(new ActualAnswerText(i, "actualanswertext" + i));
    }
    return actualAnswerTexts;
  }

  @Bean(name = "actualAnswerText")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  ActualAnswerText getActualAnswerText() {
    return new ActualAnswerText(1l, "AnswerTextTest");
  }

  @Bean(name = "questionTextPossibilitiesForCheckBox")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public List<QuestionTextPossibility> getQuestionTextPossibilitiesWithCheckBoxQuestion() {
    List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      questionTextPossibilities.add(new QuestionTextPossibility(i, "questionTextPossibility" + i, getValidCheckBoxQuestion()));
    }
    return questionTextPossibilities;
  }

  @Bean(name = "questionTextPossibilitiesForRadioButton")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public List<QuestionTextPossibility> getQuestionTextPossibilitiesWithRadioButtonQuestion() {
    List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      questionTextPossibilities.add(new QuestionTextPossibility(i, "questionTextPossibility" + i, getValidRadioButtonQuestion()));
    }
    return questionTextPossibilities;
  }

  @Bean(name = "questionCreateDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public QuestionCreateDTO getQuestionCreateDTO() {
    return new QuestionCreateDTO("What is your name?");
  }

  @Bean(name = "getListQuestions")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public List<Question> getListQuestions() {
    List<Question> questions = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Question question = new Question("helloka" + i);
      question.setListPosition(i);
      questions.add(question);
    }
    return questions;
  }

  @Bean(name = "getAnswers")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public List<Answer> getListAnswers() {
    List<Answer> answers = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Answer answer = new Answer(i, getActualAnswerTexts(), getAnswerForm(), getScaleQuestion());
      answers.add(answer);
    }
    return answers;
  }

  @Bean(name = "getAnotherAnswers")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public List<Answer> getAnotherListAnswers() {
    List<Answer> answers = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Answer answer = new Answer(i+5, getActualAnswerTexts(), new AnswerForm(), getScaleQuestion());
      answers.add(answer);
    }
    return answers;
  }

  @Bean(name = "createAnswerFormDTO")
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CreateAnswerFormDTO getCreateAnswerFormDTO(){
    CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(1l, 1l, 1l, getListQuestions(), getListAnswers(), getAnotherListAnswers());
    return createAnswerFormDTO;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
