package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionType;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "teacher", roles = {"TEACHER"})
public class QuestionRestControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void getQuestionCreateRest_withValidData_shouldReturnQuestionFormId() throws Exception {
    mockMvc.perform(get("/rest/question/create/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void postQuestionCreateRest_withValidTextQuestion_shouldReturnStatusOk() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.TEXTQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("test question text"))))
            .andExpect(status().isOk());
  }

  @Test
  public void postQuestionCreateRest_withValidRadioButtonQuestion_shouldReturnStatusOk() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.RADIOBUTTONQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("test question text", List.of("radio answer text", "radio answer text 2", "radio answer text 3")))))
            .andExpect(status().isOk());
  }

  @Test
  public void postQuestionCreateRest_withValidCheckBoxQuestion_shouldReturnStatusOk() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.CHECKBOXQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("test question text", List.of("checkbox answer text", "checkbox answer text 2", "checkbox answer text 3")))))
            .andExpect(status().isOk());
  }

  @Test
  public void postQuestionCreateRest_withValidScaleQuestion_shouldReturnStatusOk() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.SCALEQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("test question text scale", List.of("5")))))
            .andExpect(status().isOk());
  }

  @Test
  public void postQuestionCreateRest_withInValidQuestionType_shouldThrowInvalidInputFormatException() throws Exception {
    mockMvc.perform(post("/rest/question/create/invalid/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("test question text scale", List.of("5")))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Invalid Question Type")));
  }

  @Test
  public void postQuestionCreateRest_withMissingFields_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/question/create/text/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO(""))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("QuestionText is required.")));
  }

  @Test
  public void postQuestionCreateRest_withNoScaleValueByScaleQuestion_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.SCALEQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("scale question text"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Should have a scale value for scale question")));
  }

  @Test
  public void postQuestionCreateRest_withNotEnoughAnswerByRadioButtonQuestion_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.RADIOBUTTONQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("radio button question text"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("The provided number of answer possibilities is less than 2")));
  }

  @Test
  public void postQuestionCreateRest_withNotEnoughAnswerByCheckboxQuestion_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/question/create/" + QuestionType.CHECKBOXQUESTION.toString() + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("check box question text"))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("The provided number of answer possibilities is less than 2")));
  }

  @Test
  public void postQuestionCreateRest_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(post("/rest/question/create/text/66")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("text question text"))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  public void postQuestionCreateRest_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(post("/rest/question/create/text/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("text question text"))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void postQuestionCreateRest_withNoAppUserByTheGivenQuestionForm_shouldThrowMissingAppUserException() throws Exception {
    mockMvc.perform(post("/rest/question/create/text/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionCreateDTO("text question text"))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void getUpdateRest_withValidData_shouldReturnQuestionWithDTypeDTO() throws Exception {
    mockMvc.perform(get("/rest/question/update/8")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(8)))
            .andExpect(jsonPath("$.questionType", is(QuestionType.CHECKBOXQUESTION.toString())))
            .andExpect(jsonPath("$.questionText", is("check box question no answers")));
  }

  @Test
  public void getUpdateRest_withInvalidQuestionId_shouldThrowQuestionNotFoundByIdException() throws Exception {
    mockMvc.perform(get("/rest/question/update/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question with the given id")));
  }

  @Test
  public void getUpdateRest_withQuestionIdThatBelongsToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/question/update/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void getUpdateRest_withQuestionThatHasAlreadyBeenAnswered_shouldThrowQuestionHasBeenAnsweredException() throws Exception {
    mockMvc.perform(get("/rest/question/update/6")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("You can not modify a question where answers has been provided, only delete it")));
  }

  @Test
  public void putUpdateRest_withValidCheckBoxQuestion_shouldReturnQuestion() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(1, 1, QuestionType.CHECKBOXQUESTION.toString(), "modified check box question text", 0))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.listPosition", is(0)))
            .andExpect(jsonPath("$.questionText", is("modified check box question text")));
  }

  @Test
  public void putUpdateRest_withValidRadioButtonQuestion_shouldReturnQuestion() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(2, 1, QuestionType.RADIOBUTTONQUESTION.toString(), "modified radio button question text", 0))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.listPosition", is(1)))
            .andExpect(jsonPath("$.questionText", is("modified radio button question text")));
  }

  @Test
  public void putUpdateRest_withValidScaleQuestion_shouldReturnQuestion() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(3, 1, QuestionType.SCALEQUESTION.toString(), "modified scale question text", 0))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(3)))
            .andExpect(jsonPath("$.listPosition", is(2)))
            .andExpect(jsonPath("$.questionText", is("modified scale question text")));
  }

  @Test
  public void putUpdateRest_withValidTextQuestion_shouldReturnQuestion() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(4, 1, QuestionType.TEXTQUESTION.toString(), "modified text question text", 0))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(4)))
            .andExpect(jsonPath("$.listPosition", is(3)))
            .andExpect(jsonPath("$.questionText", is("modified text question text")));
  }

  @Test
  public void putUpdateRest_withInvalidQuestionId_shouldThrowQuestionNotFoundByIdException() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(66, 1, "Check box", "modified check box question text", 0))))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question with the given id")));
  }

  @Test
  public void putUpdateRest_withNotMatchinQuestionType_shouldThrowTypeMissMatchException() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(2, 1, QuestionType.CHECKBOXQUESTION.toString(), "modified check box question text", 0))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("The questionstypes do not match")));
  }

  @Test
  public void putUpdateRest_withQuestionBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(5, 3, "Scale", "modified check box question text", 0))))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void putUpdateRest_withMissingParams_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(put("/rest/question/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(new QuestionWithDTypeDTO(5, 3, "", "modified check box question text", 0))))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("QuestionType is required.")));
  }

  @Test
  public void putUpdatePosition_withValidData_shouldReturnQuestion() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/up/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.listPosition", is(0)));
  }

  @Test
  public void putUpdatePosition_withInvalidQuestionID_shouldQuestionNotFoundByIdException() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/up/77")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question with the given id")));
  }

  @Test
  public void putUpdatePosition_withQuestionBelongingToAnotherUSer_shouldBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/up/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  public void putUpdatePosition_withInvalidChangeRequest_shouldThrowInvalidQuestionPositionChangeException() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/kaki/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Not valid parameter provided for changing the position")));
  }

  @Test
  public void putUpdatePosition_withTryingToMoveTheFirstElementForward_shouldThrowInvalidQuestionPositionException() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/up/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Not possible to move element more forward. It's the first element")));
  }

  @Test
  public void putUpdatePosition_withTryingToMoveTheLastElementBackward_shouldThrowInvalidQuestionPositionException() throws Exception {
    mockMvc.perform(put("/rest/question/update-position/down/4")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Not possible to move element more backward, it's the last element")));
  }

  @Test
  public void questionDelete_withValidData_shouldReturnQuestionFormId() throws Exception {
    mockMvc.perform(delete("/rest/question/delete/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(1)));
  }

  @Test
  public void questionDelete_withInvalidQuestionId_shouldThrowQuestionNotFoundException() throws Exception {
    mockMvc.perform(delete("/rest/question/delete/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No question with the given id")));
  }

  @Test
  public void questionDelete_withQuestionBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(delete("/rest/question/delete/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }
}
