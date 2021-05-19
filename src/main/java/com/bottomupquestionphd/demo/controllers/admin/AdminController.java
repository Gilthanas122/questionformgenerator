package com.bottomupquestionphd.demo.controllers.admin;

import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoUsersInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.appuser.RoleMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.UserDeactivateException;
import com.bottomupquestionphd.demo.services.appuser.AdminAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("admin")
public class AdminController {
  private static final Logger log = LoggerFactory.getLogger(AdminController.class);
  private final AdminAppUserService adminAppUserService;

  public AdminController(AdminAppUserService adminAppUserService) {
    this.adminAppUserService = adminAppUserService;
  }


  @GetMapping("change-user-role")
  public String renderChangeUserRoleHTML(Model model) {
    log.info("GET change-user-role started");
    try {
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      log.info("GET change-user-role finished");
      return "admin/change-user-role";
    }catch (NoUsersInDatabaseException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
    model.addAttribute("error", e.getMessage());
  }
    return "admin/change-user-role";
  }


  @GetMapping("/add-user-role/{role}/{id}")
  public String addUserRole(Model model, @PathVariable String role, @PathVariable long id){
    log.info("GET add-user-role/" + role + "/" + id + "started");
      try{
        adminAppUserService.addNewRole(role, id);
        model.addAttribute("allUsers", adminAppUserService.findAllUsers());
        log.info("GET add-user-role/" + role + "/" + id + "finished");
        return "redirect:/admin/change-user-role";
      }catch (NoSuchUserByIdException e){
        log.error(e.getMessage());
        model.addAttribute("error", e.getMessage());
      }catch (Exception e){
        log.error(e.getMessage());
        model.addAttribute("error", e.getMessage());
      }
      return "admin/change-user-role";
  }


  @GetMapping("/remove-user-role/{role}/{id}")
  public String removeUserRole(Model model, @PathVariable String role, @PathVariable long id){
    log.info("GET /remove-user-role/" + role + "/" + id + " started");
    try{
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      adminAppUserService.removeRole(role, id);
      log.info("GET /remove-user-role/" + role + "/" + id + " finished");
      return "redirect:/admin/change-user-role";
    }catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (RoleMissMatchException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "admin/change-user-role";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("deactivate-user/{id}")
  public String deactivateUser(@PathVariable long id, Model model) {
    log.info("/deactivate-user/" + id + " started");
    try{
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      adminAppUserService.deactivateUser(id);
      log.info("/deactivate-user/" + id + " finished");
      return "redirect:/admin/change-user-role";
    }catch (UserDeactivateException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (NoUsersInDatabaseException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch(Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "admin/change-user-role";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("activate-user/{id}")
  public String activateUser(@PathVariable long id, Model model) {
    log.info("/activate-user/" + id + " started");
    try{
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      adminAppUserService.activateUser(id);
      log.info("/activate-user/" + id + " finished");
      return "redirect:/admin/change-user-role";
    }catch (UserDeactivateException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (NoUsersInDatabaseException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "admin/change-user-role";
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("delete-user/{id}")
  public String deleteUser(@PathVariable long id, Model model){
    log.info("/delete-user/" + id + " started");
    try{
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      adminAppUserService.deleteUser(id);
      log.info("/delete-user/" + id + " finished");
      return "redirect:/admin/change-user-role";
    }catch (NoUsersInDatabaseException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "admin/change-user-role";
  }

}
