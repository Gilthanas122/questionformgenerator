package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.admin.AdminController;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoUsersInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.appuser.RoleMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.UserDeactivateException;
import com.bottomupquestionphd.demo.services.appuser.AdminAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RESTAdminController {
  private static final Logger log = LoggerFactory.getLogger(AdminController.class);
  private final AdminAppUserService adminAppUserService;

  public RESTAdminController(AdminAppUserService adminAppUserService) {
    this.adminAppUserService = adminAppUserService;
  }

  @GetMapping("change-user-role")
  public ResponseEntity<?> renderChangeUserRoleHTMLRest() throws NoUsersInDatabaseException {
    log.info("REST GET admin/change-user-role started");
    log.info("REST GET admin/change-user-role finished");

    return new ResponseEntity<>(adminAppUserService.findAllUsers(), HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/remove-user-role/{role}/{id}")
  public ResponseEntity<?> removeUserRoleRest(@PathVariable String role, @PathVariable long id) throws NoSuchUserByIdException, RoleMissMatchException, NoUsersInDatabaseException {
    log.info("REST GET admin/remove-user-role/" + role + "/" + id + " started");
    adminAppUserService.removeRole(role, id);
    log.info("REST GET admin/remove-user-role/" + role + "/" + id + " finished");

    return new ResponseEntity<>(adminAppUserService.findAllUsers(), HttpStatus.OK);
  }

  @GetMapping("deactivate-user/{id}")
  public ResponseEntity<?> deactivateUserRest(@PathVariable long id) throws UserDeactivateException, NoSuchUserByIdException, NoUsersInDatabaseException {
    log.info("REST GET admin/deactivate-user/" + id + " started");
    adminAppUserService.deactivateUser(id);
    log.info("REST GET admin/deactivate-user/" + id + " finished");

    return new ResponseEntity<>(adminAppUserService.findAllUsers(), HttpStatus.OK);
  }

  @GetMapping("activate-user/{id}")
  public ResponseEntity<?> activateUser_rest(@PathVariable long id) throws UserDeactivateException, NoSuchUserByIdException, NoUsersInDatabaseException {
    log.info("REST GET admin/activate-user/" + id + " started");
    adminAppUserService.activateUser(id);
    log.info("REST GET admin/activate-user/" + id + " finished");

    return new ResponseEntity<>(adminAppUserService.findAllUsers(), HttpStatus.OK);
  }

  @DeleteMapping("delete-user/{id}")
  public ResponseEntity<?> deleteUserREST(@PathVariable long id) throws NoSuchUserByIdException, NoUsersInDatabaseException {
    log.info("REST DELETE admin/delete-user/" + id + " started");
    adminAppUserService.deleteUser(id);
    log.info("REST DELETE admin/delete-user/" + id + " finished");

    return new ResponseEntity<>(adminAppUserService.findAllUsers(), HttpStatus.OK);
  }
}
