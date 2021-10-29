package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.answers.TextAnswerVote;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "user")
@Import(TestConfigurationBeanFactory.class)
public class AnswerFormRestControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  @Autowired
  AnswerFormRepository answerFormRepository;
  @Autowired
  private BeanFactory beanFactory;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @WithMockUser("teacher")
  public void getAnswerFormCreateRest_withValidData_shouldReturnCreateAnswerFormDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/create/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questionFormId", is(1)))
            .andExpect(jsonPath("$.answerFormId", is(0)))
            .andExpect(jsonPath("$.questions.length()", is(4)));
  }

  @Test
  public void getAnswerFormCreateRest_withQuestionFormAlreadyFilledOutByTheUser_shouldThrowQuestionFormAlreadyFilledOutByCurrentUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/create/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("You have already filled out this question form, please update the one where you filled it out")));
  }

  @Test
  public void getAnswerFormCreateRest_withInValidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/create/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  @WithMockUser("teacher")
  public void postAnswerFormCreateRest_withValidData_shouldReturnAnswerForm() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/1/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(4)));
  }

  @Test
  public void postAnswerFormCreateRest_withNotMatchingAppUserId_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/1/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void postAnswerFormCreateRest_withAnswerFormAlreadyFilledOutByTheUser_shouldThrowAnswerFormAlreadyFilledOutByCurrentUserException() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/2/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("You have already filled out this question form, please update the one where you filled it out")));
  }

  @Test
  public void postAnswerFormCreateRest_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/66/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void postAnswerFormCreateRest_withInvalidUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/1/66")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Couldn't find appuser with the given id")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withValidData_shouldReturnCreateAnswerFormDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answerFormId", is(3)))
            .andExpect(jsonPath("$.questions.length()", is(4)));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withResourceBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher")
  public void getAnswerFormUpdateRest_withAnswerFormNotFilledOutByUser_shouldThrowAnswerFormNotFilledOutException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("You need to fill out the answerform in order to update it")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/66/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void putAnswerFormUpdateRest_withValidData_shouldReturnModifiedAnswerForm() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");
    answerFormModified.getAnswers().get(0).getActualAnswerTexts().get(0).setAnswerText("AnswerText Modified");
    answerFormModified.getAnswers().get(0).getActualAnswerTexts().get(0).setTextAnswerVotes(List.of(new TextAnswerVote(1L, (byte) 4)));

    mockMvc.perform(put("/rest/answer-form/update/3/5")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(3)))
            .andExpect(jsonPath("$.answers[0].actualAnswerTexts[0].answerText", is("AnswerText Modified")))
            .andExpect(jsonPath("$.answers[0].actualAnswerTexts[0].textAnswerVotes[0].value", is(4)));
    String actual = answerFormRepository.findById(3).get().getAnswers().get(0).getActualAnswerTexts().get(0).getAnswerText();
    String expected = answerFormModified.getAnswers().get(0).getActualAnswerTexts().get(0).getAnswerText();
    assertEquals(actual, expected);
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void putAnswerFormUpdateRest_withInValidAnswerFormId_shouldThrowNoSuchAnswerFormByIdException() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");
    mockMvc.perform(put("/rest/answer-form/update/66/5")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Couldn't find answerform belonging to the given id")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void putAnswerFormUpdateRest_withResourceBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");

    mockMvc.perform(put("/rest/answer-form/update/3/66")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher")
  public void putAnswerFormUpdateRest_withNonMatchingUserIdInPath_shouldThrowBelongsToAnotherUserException() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");

    mockMvc.perform(put("/rest/answer-form/update/3/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("This answerForm belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void putAnswerFormUpdateRest_withNonMatchingNumberOfAnswersAndQuestions_shouldThrowAnswerFormNumberOfAnswersShouldMatchException() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");
    answerFormModified.getAnswers().remove(0);

    mockMvc.perform(put("/rest/answer-form/update/3/5")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Updated AnswerForms number of answers and original AnswerForms number of answers should match")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void putAnswerFormUpdateRest_withNonMatchingNumberOfAnswersOfSubmittedAnswerForm_shouldThrowNumberOfQuestionAndAnswersShouldMatchException() throws Exception {
    AnswerForm answerFormModified = (AnswerForm) beanFactory.getBean("answerForm");
    answerFormModified.getAnswers().remove(0);

    mockMvc.perform(put("/rest/answer-form/update/3/5")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(answerFormModified)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Updated AnswerForms number of answers and original AnswerForms number of answers should match")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormBelongingToAnswer_withValidData_shouldReturnDisplayAnswersFromAnAnswerFormDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/get/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questionTypes[0]", is("CheckBoxQuestion")))
            .andExpect(jsonPath("$.questionTexts[0]", is("checkbox question text")))
            .andExpect(jsonPath("$.answers[0].actualAnswerTexts[0].answerText", is("test answer 1 text 1")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormBelongingToAnswer_withInvalidAnswerId_shouldThrowsAnswerNotFoundByIdException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/get/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Couldn't find answer with the provided id")));
  }

  @Test
  @WithMockUser(username = "teacher")
  public void getAnswerFormBelongingToAnswer_withNonMatchingUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/get/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher")
  public void showAllAnswersBelongingToQuestionForm_withValidData_shouldReturnDisplayAllUserAnswersDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/answers/1/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questionTextsAndTypes[0]", is("checkbox question text (Type CheckBoxQuestion )")))
            .andExpect(jsonPath("$.questionTextsAndTypes.length()", is(4)))
            .andExpect(jsonPath("$.answers.length()", is(2)))
            .andExpect(jsonPath("$.answers[1][0]", is("test answer 1 text 1;; test answer 1 text 2")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void showAllAnswersBelongingToQuestionForm_withQuestionFormBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/answers/1/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "admin")
  public void showAllAnswersBelongingToQuestionForm_withNoUserFilledOutQuestionForm_shouldThrowNoUserFilledOutAnswerFormException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/answers/3/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("No user filled out the answer form")));
  }

  @Test
  @WithMockUser(username = "teacher")
  public void showAllAnswersBelongingToQuestionForm_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/answers/66/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  @WithMockUser("teacher2")
  public void seeUsersAnswersBelongingToAQuestionForm_withValidData_shouldReturnDisplayOneUserAnswersDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/list-answers/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answers.length()", is(4)))
            .andExpect(jsonPath("$.questionTexts.length()", is(4)))
            .andExpect(jsonPath("$.answers[1]", is("third actual answertext")))
            .andExpect(jsonPath("$.questionTexts[1]", is("radio question text")));
  }
}
