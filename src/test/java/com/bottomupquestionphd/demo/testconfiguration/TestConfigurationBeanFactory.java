package com.bottomupquestionphd.demo.testconfiguration;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
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
    AppUser getPlayerWithValidCredentials() {
        AppUser fakePlayer = new AppUser("validUser", "Geeks@portal20", "ROLE_USER");
        return fakePlayer;
    }

    @Bean(name = "validLoginDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    LoginDTO getValidLoginDTO() {
        LoginDTO loginDTO = new LoginDTO("validUser", "Geeks@portal20");
        return loginDTO;
    }

    @Bean(name = "validQuestion")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Question getValidQuestion(){
        Question question = new Question(1, "fakeQuestionText", 0);
        question.setQuestionForm(getQuestionForm());
        return question;
    }

    @Bean(name = "scaleQuestion")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Question getScaleQuestion(){
        ScaleQuestion question = new ScaleQuestion(1, "fakeQuestionText", 6);
        question.setQuestionForm(getQuestionForm());
        return question;
    }

    @Bean(name = "textQuestion")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    TextQuestion getValidTextQuestion(){
        TextQuestion question = new TextQuestion(1, "fakeTextQuestionText");
        question.setQuestionForm(getQuestionForm());
        return question;
    }

    @Bean(name = "checkboxQuestion")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CheckBoxQuestion getValidCheckBoxQuestion(){
        CheckBoxQuestion question = new CheckBoxQuestion(1, "fakeQuestionText", List.of(getQuestionTextPossibility(), getQuestionTextPossibility(), getQuestionTextPossibility()));
        question.setQuestionForm(getQuestionForm());
        return question;
    }

    @Bean(name = "radioButtonQuestion")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    RadioButtonQuestion getValidRadioButtonQuestion(){
        RadioButtonQuestion question = new RadioButtonQuestion(1, "fakeQuestionText", List.of(getQuestionTextPossibility(), getQuestionTextPossibility(), getQuestionTextPossibility()));
        question.setQuestionForm(getQuestionForm());
        return question;
    }

    @Bean("questionTextPossibility")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionTextPossibility getQuestionTextPossibility(){
        QuestionTextPossibility questionTextPossibility = new QuestionTextPossibility("questionTextPossibility");
        return questionTextPossibility;
    }

    @Bean(name = "textQuestionWithDTypeDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionWithDTypeDTO getTextQuestionWithDTypeDTO(){
        QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "TextQuestion", "TextQuestionText", null);
        return questionWithDTypeDTO;
    }

    @Bean(name = "scaleQuestionWithDTypeDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionWithDTypeDTO getScaleQuestionWithDTypeDTO(){
        QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Scale", "ScaleQuestionText", 5);
        return questionWithDTypeDTO;
    }

    @Bean(name = "checkboxQuestionWithDTypeDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionWithDTypeDTO getCheckBoxQuestionWithDTypeDTO(){
        QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Check box", "CheckboxQuestionText", 5);
        questionWithDTypeDTO.setQuestionTextPossibilities(getQuestionTextPossibilitiesWithCheckBoxQuestion());
        return questionWithDTypeDTO;
    }

    @Bean(name = "radioQuestionWithDTypeDTO")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionWithDTypeDTO getRadioQuestionWithDTypeDTO(){
        QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(1, 1, "Radio button", "RadioQuestionText", 5);
        questionWithDTypeDTO.setQuestionTextPossibilities(getQuestionTextPossibilitiesWithRadioButtonQuestion());
        return questionWithDTypeDTO;
    }

    @Bean(name = "questionForm")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    QuestionForm getQuestionForm(){
        QuestionForm questionForm = new QuestionForm("question form name", "question form description");
        return questionForm;
    }

    @Bean(name = "answerForm")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    AnswerForm getAnswerForm(){
        AnswerForm answerForm = new AnswerForm(0, getQuestionForm(), getPlayerWithValidCredentials());
        return answerForm;
    }


    @Bean(name = "checkBoxAnswer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Answer getCheckBoxAnswer(){
        Answer answer = new Answer(0, getActualAnswerTexts(), getAnswerForm(), getValidCheckBoxQuestion());
        return answer;
    }

    @Bean(name = "checkBoxAnswer")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    Answer getRadioButtonAnswer(){
        Answer answer = new Answer(0, getActualAnswerTexts(), getAnswerForm(), getValidRadioButtonQuestion());
        return answer;
    }

    @Bean(name = "actualAnswerTexts")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    List<ActualAnswerText> getActualAnswerTexts(){
        List<ActualAnswerText> actualAnswerTexts = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            actualAnswerTexts.add(new ActualAnswerText(i, "actualanswertext"+i));
        }
        return actualAnswerTexts;
    }

    @Bean(name = "questionTextPossibilitiesForCheckBox")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<QuestionTextPossibility> getQuestionTextPossibilitiesWithCheckBoxQuestion(){
        List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            questionTextPossibilities.add(new QuestionTextPossibility(i, "questionTextPossibility" + i, getValidCheckBoxQuestion()));
        }
        return questionTextPossibilities;
    }

    @Bean(name = "questionTextPossibilitiesForRadioButton")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public List<QuestionTextPossibility> getQuestionTextPossibilitiesWithRadioButtonQuestion(){
        List<QuestionTextPossibility> questionTextPossibilities = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            questionTextPossibilities.add(new QuestionTextPossibility(i, "questionTextPossibility" + i, getValidRadioButtonQuestion()));
        }
        return questionTextPossibilities;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
