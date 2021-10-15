package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "teacher", roles = {"TEACHER"})
public class QuestionFormRestControllerTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private QuestionFormService questionFormService;
  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getRestQuestionFormCreate_withValidInput_shouldReturnNewQuestionForm() throws Exception {
    mockMvc.perform(get("/rest/question-form/create")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.finished", is(false)));
  }

  @Test
  public void postRestQuestionFormCreate_withValidInput_shouldReturnQuestionFormId() throws Exception {
    mockMvc.perform(post("/rest/question-form/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO("question form test name", "question form test description"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(6)));

    assertEquals("question form test name", questionFormService.findById(6).getName());
  }

  @Test
  public void postRestQuestionFormCreate_withMissingName_shouldMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/question-form/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO(null, "Userpass-13"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Name is required.")));
  }

  @Test
  public void postRestQuestionFormCreate_withDuplicateQuestionFormName_shouldThrowQuestionFormAlreadyExistsException() throws Exception {
    mockMvc.perform(post("/rest/question-form/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO("test questionform", "Userpass-13"))))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message", is("There is a question form with the provided name")));
  }

  @Test
  public void questionFormList_withValidInput_shouldReturnAllTheQuestionForms() throws Exception {
    mockMvc.perform(get("/rest/question-form/list")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  public void questionFormList_NoQuestionFormInDB_shouldReturnAllTheQuestionForms() throws Exception {
    mockMvc.perform(get("/rest/question-form/list")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question form belonging to the user.")));
  }

  @Test
  public void getQuestionFormUpdate_withValidInput_shouldReturnModifiedQuestionForm() throws Exception {
    mockMvc.perform(get("/rest/question-form/update/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description", is("test questionform description")))
            .andExpect(jsonPath("$.name", is("test questionform")));
  }

  @Test
  public void getQuestionFormUpdate_withInValidID_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/question-form/update/666")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void getQuestionFormUpdate_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/question-form/update/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void getQuestionFormUpdate_withQuestionFormWithNoUser_shouldThrowMissingUserException() throws Exception {
    mockMvc.perform(get("/rest/question-form/update/4")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @Test
  public void putUpdateQuestionFormByID_withValidData_shouldReturnCreatedStatuscode() throws Exception {
    mockMvc.perform(put("/rest/question-form/update/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO("modified name", "modified description"))))
            .andExpect(status().isCreated());

    assertEquals("modified name", questionFormService.findById(2).getName());
  }

  @Test
  public void putUpdateQuestionFormByID_withMissingParams_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(put("/rest/question-form/update/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO(null, "modified description"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Name is required.")));
  }

  @Test
  public void putUpdateQuestionFormByID_withInvalidId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(put("/rest/question-form/update/7")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO("modified name", "modified description"))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question form with the given id in the database, can not update it")));
  }

  @Test
  public void putUpdateQuestionFormByID_withQuestionFormBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(put("/rest/question-form/update/3")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new QuestionFormCreateDTO("modified name", "modified description"))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void putFinish_withValidData_shouldReturnQuestionFormId() throws Exception {
    mockMvc.perform(put("/rest/question-form/finish/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void putFinish_withNoQuestionsBelongingToTheQuestionForm_shouldThrowNotEnoughQuestionsToCreateFormException() throws Exception {
    mockMvc.perform(put("/rest/question-form/finish/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("There should be at least one question to submit a form")));
  }

  @Test
  public void putFinish_withMissingAppUser_shouldThrowMissingUserException() throws Exception {
    mockMvc.perform(put("/rest/question-form/finish/4")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @Test
  public void putFinish_withInValidQuestionFormID_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(put("/rest/question-form/finish/66")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void putFinish_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(put("/rest/question-form/finish/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void listQuestions_withValidDate_shouldReturnListQuestionWithDTypeDTO() throws Exception {
    mockMvc.perform(get("/rest/question-form/list-questions/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(4)))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].questionType", is("RadioButtonQuestion")))
            .andExpect(jsonPath("$[1].questionTextPossibilities[0].answerText", is("Radio")))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].questionType", is("CheckBoxQuestion")))
            .andExpect(jsonPath("$[0].questionTextPossibilities[0].answerText", is("Check box")))
            .andExpect(jsonPath("$[2].scale", is(5)));
  }

  @Test
  public void listQuestions_withMissingAppUser_shouldThrowMissingUserException() throws Exception {
    mockMvc.perform(get("/rest/question-form/list-questions/4")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @Test
  public void listQuestions_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/question-form/list-questions/66")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void listQuestions_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/question-form/list-questions/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void delete_withValidData() throws Exception {
    mockMvc.perform(delete("/rest/question-form/delete/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Question Form with id 1 successfully deleted")));

    assertThrows(QuestionFormNotFoundException.class, () -> questionFormService.findById(1));
  }

  @Test
  public void delete_withNonExistingQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(delete("/rest/question-form/delete/66")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void delete_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(delete("/rest/question-form/delete/3")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }
}
