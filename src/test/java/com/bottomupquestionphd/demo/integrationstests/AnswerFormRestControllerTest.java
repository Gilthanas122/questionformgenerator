package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.answers.TextAnswerVote;
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
  private BeanFactory beanFactory;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getAnswerFormCreateRest_withValidData_returnCreateAnswerFormDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/create/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.questionFormId", is(1)))
            .andExpect(jsonPath("$.answerFormId", is(0)))
            .andExpect(jsonPath("$.questions.length()", is(4)));
  }

  @Test
  public void getAnswerFormCreateRest_withQuestionFormAlreadyFilledoutByTheUser_shouldThrowQuestionFormAlreadyFilledOutByCurrentUserException() throws Exception {
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
  public void postAnswerFormCreateRest_withValidData_shouldReturnAnswerForm() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/1/3")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AnswerForm())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(4)));
  }

  @Test
  @WithMockUser(username = "test")
  public void postAnswerFormCreateRest_withNotMachingAppUserId_shouldThrowNoSuchUserByUsername() throws Exception {
    mockMvc.perform(post("/rest/answer-form/create/1/66")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(beanFactory.getBean("answerForm"))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Couldn't find user with the given username")));
  }

  @Test
  public void postAnswerFormCreateRest_withAnswerFormBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
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
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withValidData_shouldReturnCreateAnswerFormDTO() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/1/5")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answerFormId", is(1)))
            .andExpect(jsonPath("$.questions.length()", is(4)));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withResourceBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/1/66")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withAnswerFormNotFilledOutByUser_shouldThrowAnswerFormNotFilledOutException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/1/2/5")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("You need to fill out the answerform in order to update it")));
  }

  @Test
  @WithMockUser(username = "teacher2")
  public void getAnswerFormUpdateRest_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form/update/66/1/5")
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
            .andExpect(jsonPath("$.message", is("This answerForm belongs to another user")));
  }


}
