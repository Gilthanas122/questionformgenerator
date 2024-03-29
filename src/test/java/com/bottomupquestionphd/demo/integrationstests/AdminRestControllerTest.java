package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username = "admin", roles = {"ADMIN"})
public class AdminRestControllerTest {
  @Autowired
  private AppUserService appUserService;
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void findAllUsers_withValidRequest() throws Exception {
    mockMvc.perform(get("/rest/admin/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)));
  }

  @Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Test
  public void findAllUsers_withNoUsersInDB_shouldThrowNoUserInDBException() throws Exception {
    mockMvc.perform(get("/rest/admin/")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No users in database")));
  }

  //THROWS ACCESS DENIED BUT SOMEHOW CAN NOT TEST IT
/*  @Test
  @WithMockUser(username = "user")
  public void restChangeUserRole_withUserRole_shouldReturnUnauthorized() throws Exception {
    mockMvc.perform(get("/rest/admin/"))
            .andExpect(status().isUnauthorized());
  }*/
  @Test
  public void addUserRole_withValidData_shouldReturnListModifiedUsers() throws Exception {
    mockMvc.perform(get("/rest/admin/add-user-role/admin/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].roles", is("ROLE_USER,ROLE_TEACHER,ROLE_ADMIN")));

    assertEquals(appUserService.findById(2).getRoles(), "ROLE_USER,ROLE_TEACHER,ROLE_ADMIN");
  }

  @Test
  public void addUserRole_withInvalidRoleSuffix_shouldThrowRoleMissMatchException() throws Exception {
    mockMvc.perform(get("/rest/admin/add-user-role/kaki/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Invalid role")));
  }

  @Test
  public void addUserRole_withInvalidId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(get("/rest/admin/add-user-role/admin/66")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));
  }

  @Test
  public void addUserRole_withUserThatHasAlreadyThatRole_shouldThrowRoleMissMatchException() throws Exception {
    mockMvc.perform(get("/rest/admin/add-user-role/teacher/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User already has the given role")));
  }


  @Test
  public void removeUserRole_withValidRequestToRemoveTeacherRole_shouldReturnListModifiedUsers() throws Exception {
    mockMvc.perform(get("/rest/admin/remove-user-role/teacher/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[1].roles", is("ROLE_USER")));

    assertEquals(appUserService.findById(2).getRoles(), "ROLE_USER");
  }

  @Test
  public void removeUserRole_withInvalidRoleRemoveRequest_shouldThrowRoleMissMatchException() throws Exception {
    mockMvc.perform(get("/rest/admin/remove-user-role/admin/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User doesn't have the given role")));
  }

  @Test
  public void removeUserRole_withInvalidRoleSuffix_shouldThrowRoleMissMatchException() throws Exception {
    mockMvc.perform(get("/rest/admin/remove-user-role/kaki/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("Invalid role")));

  }

  @Test
  public void removeUserRole_withNonExistentUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(get("/rest/admin/remove-user-role/teacher/77")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));

  }

  @Test
  public void deactivate_withValidUser_returnAllUsersWithOneDeactivated() throws Exception {
    mockMvc.perform(get("/rest/admin/deactivate-user/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(5)))
            .andExpect(jsonPath("$[0].active", is(false)));

    assertEquals(appUserService.findById(2).isActive(), false);

  }

  @Test
  public void deactivate_withAlreadyDeactivatedUser_shouldThrowUserDeactivateException() throws Exception {
    mockMvc.perform(get("/rest/admin/deactivate-user/4")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User is already inactive")));
  }


  @Test
  public void deactivate_withNonExistentUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(get("/rest/admin/deactivate-user/77")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));
  }

  @WithMockUser(username = "admin", roles = {"ADMIN"})
  @Test
  public void activate_witValidRequest_shouldReturnListOfUsersOneModified() throws Exception {
    mockMvc.perform(get("/rest/admin/activate-user/4")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[3].active", is(true)));

    assertEquals(appUserService.findById(4).isActive(), true);

  }

  @WithMockUser(username = "admin", roles = {"ADMIN"})
  @Test
  public void activate_withAlreadyActiveUser_shouldThrowDeactivateUserException() throws Exception {
    mockMvc.perform(get("/rest/admin/activate-user/3")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User is already active")));
  }

  @Test
  public void activate_withNonExistentUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(get("/rest/admin/activate-user/77")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));
  }

  @Test
  public void deleteUser_withValidRequest_shouldReturnListOfAllUsersButTheDeleted() throws Exception {
    mockMvc.perform(delete("/rest/admin/delete-user/2")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)));

    assertThrows(NoSuchUserByIdException.class, () -> appUserService.findById(2));
  }

  @Test
  public void deleteUserwithNonExistentUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(delete("/rest/admin/delete-user/77")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));
  }
}
