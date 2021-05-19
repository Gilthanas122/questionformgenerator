package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserRegisterDTO;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(TestConfigurationBeanFactory.class)
public class PublicRestControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void registerUser_getAppUser() throws Exception {
    mockMvc.perform(get("/rest/register")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username", is(nullValue())))
            .andExpect(jsonPath("$.password", is(nullValue())))
            .andExpect(jsonPath("$.emailId", is(nullValue())));
  }

  // PROBLEM WITH SENDING EMAILS
  @Test
  public void registerUser_withValidUser_shouldReturnSuccessMessage() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO("user2", "Userpass-13", "user23@user.com"))))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status", is("created")))
            .andExpect(jsonPath("$.message", is("App user successfully registered")));
  }

  @Test
  public void registerUser_InValidPassword_shouldThrowInvalidRegexParameterException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO("user2", "Userpass", "user@user.com"))))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Password must be at least 8 and max 20 characters long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=")));
  }

  @Test
  public void registerUser_InValidEmailFormat_shouldThrowInvalidRegexParameterException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO("user2", "Userpass-13", "user@usercom"))))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Not valid email format")));
  }

  @Test
  public void registerUser_withExistingUsername_shouldThrowUserNameAlreadyTakenException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO("user", "userpasS-13", "user@user.com"))))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("The username is already taken")));
  }

  @Test
  public void registerUser_withExistingEmail_shouldThrowEmailAlreadyUsedException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO("user2", "userpasS-13", "user@user.com"))))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("There is a registration with this email already")));
  }

  @Test
  public void registerUser_withoutUserName_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO(null, "userpasS-13", "user2@user.com"))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  public void registerUser_withoutUserNameAndPassword_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserRegisterDTO(null, "", "user2@user.com"))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Username, password is required.")));
  }

  @Test
  public void renderLogin_shouldReturnAppUserLoginDTO() throws Exception {
    mockMvc.perform(get("/rest/login")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username", is(nullValue())));
  }

  @Test
  public void login_withValidCredentials_shouldReturnSUccessMessageDTO() throws Exception {
    mockMvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserLoginDTO("user", "Userpass-13"))))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status", is("ok")))
            .andExpect(jsonPath("$.message", is("Successful login")));
  }

  @Test
  public void login_withNotMatchingPassword_shouldThrowAppUserPasswordMissMatchException() throws Exception {
    mockMvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserLoginDTO("user", "Userpas-13"))))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Username didn't match the password")));
  }

  @Test
  public void login_withNoSuchUserNameInDatabase_shouldThrowAppUserNoSuchUserNameException() throws Exception {
    mockMvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserLoginDTO("user333", "Userpass-13"))))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("No such username in the database")));
  }

  @Test
  public void login_withNullValueAsUsername_shouldThrowMissingParamsException() throws Exception {
    mockMvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserLoginDTO(null, "Userpass-13"))))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Username is required.")));
  }

  @Test
  public void login_withNotActivatedUser_shouldThrowAppUserNotActivatedException() throws Exception {
    mockMvc.perform(post("/rest/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(new AppUserLoginDTO("usernotactivated", "Userpass-13"))))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Your account is not activated, please check your email box for the activation email")));
  }

  @Test
  public void verifyaccount_withValidUser_shouldReturnSuccessMessage() throws Exception {
    mockMvc.perform(get("/rest/verify-account")
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "user-not-activated-token"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status", is("ok")))
            .andExpect(jsonPath("$.message", is("You have successfully registered with the email usernotactivated@user.com")));
  }

  @Test
  public void verifyaccount_withAlreadyActivatedUser_shouldThrowUserIsAlreadyActivatedException() throws Exception {
    mockMvc.perform(get("/rest/verify-account")
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "user-token"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Appuser has been already activated")));
  }

  @Test
  public void verifyaccount_withInvalidToken_shouldThrowNoSuchUserByEmailException() throws Exception {
    mockMvc.perform(get("/rest/verify-account")
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "user-token-not-existing"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("No user with the given email")));
  }
}
