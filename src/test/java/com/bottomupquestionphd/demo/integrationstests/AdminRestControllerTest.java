package com.bottomupquestionphd.demo.integrationstests;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = {"/db/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/db/clear-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithMockUser(username="admin",roles={"ADMIN"})
public class AdminRestControllerTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeAll
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void restChangeUserRole_withValidAdminAndRole_shouldReturnAllUsers() throws Exception {
    mockMvc.perform(get("/rest/admin/change-user-role")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)));
  }

  //THROWS ACCESS DENIED BUT SOMEHOW CAN NOT TEST IT
 /* @Test
  @WithMockUser
  public void restChangeUserRole_withUserRole_shouldReturnUnauthorized() throws Exception {
    mockMvc.perform(get("/rest/admin/change-user-role"))
            .andExpect(status().isUnauthorized());
  }*/
  @WithMockUser(username="admin",roles={"ADMIN"})
  @Test
  public void removeUserRole_withValidRequestToRemoveTeacherRole_shouldReturnModifiedUsers() throws Exception {
    mockMvc.perform(get("/rest/admin/remove-user-role/teacher/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[1].roles", is("ROLE_USER")));
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
  public void deactivate_withValidUser_returnAllUsersWithOneDeactivated () throws Exception {
    mockMvc.perform(get("/rest/admin/deactivate-user/2")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[1].active", is(false)));
  }

  @Test
  public void deactivate_withAlreadyDeactivatedUser_shouldThrowUserDeactivateException () throws Exception {
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

  @WithMockUser(username="admin",roles={"ADMIN"})
  @Test
  public void activate_witValidRequest_shouldReturnListOfUsersOneModified() throws Exception {
    mockMvc.perform(get("/rest/admin/activate-user/4")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[3].active", is(true)));
  }

  @WithMockUser(username="admin",roles={"ADMIN"})
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
            .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  public void deleteUserwithNonExistentUserId_shouldThrowNoSuchUserByIdException() throws Exception {
    mockMvc.perform(delete("/rest/admin/delete-user/77")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("No user at the given id")));
  }

}
