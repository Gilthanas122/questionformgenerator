package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AnswerFormFilterRestControllerTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void renderSearchFormForQuestions_withValidRequest() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.answerForms[1].answers", hasSize(4)))
            .andExpect(jsonPath("$.answerForms[1].answers[0].actualAnswerTexts[0].answerText", is("test answer 1 text 1")));
  }

  @Test
  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  public void renderSearchFormForQuestions_withAnswerFormBelongingToAnotherUser_shouldThrowsBelongsToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @Test
  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  public void renderSearchFormForQuestions_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/search/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void renderSearchFormForQuestions_withNoUserProvidedByQuestionForm_shouldThrowMissingUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/search/4")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  public void renderSearchFormForQuestions_withNoQuestionByQuestionForm_shouldThrowQuestionFormHasNotQuestionsException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/search/5")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Can not search a question form without questions")));
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void postFilterAnswers_withValidData_returnsAnswerSearchTermResultDTO() throws Exception {
    List<String> searchTerms = List.of("1", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS", hasSize(3)))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[0]", is("test answer 1 text 1")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[1]", is("test answer 1 text 2")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[1].actualAnswers[0]", is("third actual answertext")));
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void postFilterAnswers_withValidDataAndSearchTermContainingAND_returnsAnswerSearchTermResultDTO() throws Exception {
    List<String> searchTerms = List.of("1 AND 2", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS", hasSize(3)))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[0]", is("test answer 1 text 2")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[1].actualAnswers[0]", is("third actual answertext")));
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void postFilterAnswers_withValidDataAndSearchTermContainingOR_returnsAnswerSearchTermResultDTO() throws Exception {
    List<String> searchTerms = List.of("1 OR 3", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS", hasSize(3)))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[0]", is("test answer 1 text 1")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[1]", is("test answer 1 text 2")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[1].actualAnswers[0]", is("third actual answertext")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[2].actualAnswers[0]", is("5")));
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void postFilterAnswers_withEmptySearchTerm_returnsAnswerSearchTermResultDTO() throws Exception {
    List<String> searchTerms = List.of("1 OR 3", "", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS", hasSize(2)))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[0]", is("test answer 1 text 1")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[1]", is("test answer 1 text 2")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[1].actualAnswers[0]", is("5")));
  }

  @Test
  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  public void postFilterAnswers_withASearchTermOfNull_returnsAnswerSearchTermResultDTO() throws Exception {
    List<String> searchTerms = new ArrayList<>();
    searchTerms.add("1 OR 3");
    searchTerms.add(null);
    searchTerms.add("5");
    searchTerms.add("kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS", hasSize(2)))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[0]", is("test answer 1 text 1")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[0].actualAnswers[1]", is("test answer 1 text 2")))
            .andExpect(jsonPath("$.actualAnswerTextSearchTermResultDTOS[1].actualAnswers[0]", is("5")));
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void postFilterAnswers_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    List<String> searchTerms = List.of("4", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/66")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void postFilterAnswers_withNoUserAtQuestionForm_shouldThrowMissingUserException() throws Exception {
    List<String> searchTerms = List.of("4", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/4")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }

  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  @Test
  public void postFilterAnswers_withQuestionFormBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    List<String> searchTerms = List.of("4", "third", "5", "kaki");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void postFilterAnswers_withInvalidAmountOfSearchTerms_shouldThrowQuestionFormFilteringException() throws Exception {
    List<String> searchTerms = List.of("4", "third", "5");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);
    mockMvc.perform(post("/rest/answer-form-filter/search/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(searchTermsForFilteringDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Invalid amount of search terms submitted")));
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void returnAllAnswers_withValidData() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/download-all-answers/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void returnAllAnswers_withValidData_returnsXlxsSheet() throws Exception {
    MvcResult result = mockMvc.perform(get("/rest/answer-form-filter/download-all-answers/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

    InputStream inputStream = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
    Workbook workbook = WorkbookFactory.create(inputStream);

    assertEquals(200, result.getResponse().getStatus());
    assertEquals("application/octet-stream", result.getResponse().getContentType());

    assertEquals("checkbox question text", workbook.sheetIterator().next().getRow(0).getCell(0).getStringCellValue());
    assertEquals("radio question text", workbook.sheetIterator().next().getRow(0).getCell(1).getStringCellValue());
    assertEquals("no answer provided", workbook.sheetIterator().next().getRow(1).getCell(0).getStringCellValue());
    assertEquals("no answer provided", workbook.sheetIterator().next().getRow(1).getCell(1).getStringCellValue());
    assertEquals("no answer provided", workbook.sheetIterator().next().getRow(1).getCell(3).getStringCellValue());
    assertEquals("no answer provided", workbook.sheetIterator().next().getRow(1).getCell(3).getStringCellValue());
    assertEquals("test answer 1 text 1;;test answer 1 text 2", workbook.sheetIterator().next().getRow(2).getCell(0).getStringCellValue());
    assertEquals("third actual answertext", workbook.sheetIterator().next().getRow(2).getCell(1).getStringCellValue());
    assertEquals("4", workbook.sheetIterator().next().getRow(2).getCell(2).getStringCellValue());
    assertEquals("5", workbook.sheetIterator().next().getRow(2).getCell(3).getStringCellValue());
  }

  @WithMockUser(username = "teacher", roles = {"TEACHER"})
  @Test
  public void returnAllAnswers_withInvalidQuestionFormId_shouldThrowQuestionFormNotFoundException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/download-all-answers/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Question form doesn't exist with the provided id")));

  }

  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  @Test
  public void returnAllAnswers_withQuestionFormBelongingToAnotherUser_shouldThrowBelongToAnotherUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/download-all-answers/1")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Current data belongs to another user")));
  }

  @WithMockUser(username = "teacher2", roles = {"TEACHER"})
  @Test
  public void returnAllAnswers_withNoUserAtQuestionForm_shouldThrowMissingUserException() throws Exception {
    mockMvc.perform(get("/rest/answer-form-filter/download-all-answers/4")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user belonging to the question form")));
  }
}
