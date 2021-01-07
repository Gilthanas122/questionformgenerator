package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoUsersInDatabaseException;
import com.bottomupquestionphd.demo.services.appuser.AdminAppUserService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
  private AdminAppUserService adminAppUserService;

  public AdminController(AdminAppUserService adminAppUserService) {
    this.adminAppUserService = adminAppUserService;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("change-user-role")
  public String renderChangeUserRoleHTML(Model model) {
    try {
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      return "admin/change-user-role";
    }catch (NoUsersInDatabaseException e){
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
    model.addAttribute("error", e.getMessage());
  }
    return "admin/change-user-role";
  }

  @GetMapping("/add-user-role/{role}/{id}")
  public String addUserRole(Model model, @PathVariable String role, @PathVariable long id){
      try{
        model.addAttribute("allUsers", adminAppUserService.findAllUsers());
        adminAppUserService.addNewRole(role, id);
        return "redirect:/admin/change-user-role";
      }catch (NoSuchUserByIdException e){
        model.addAttribute("error", e.getMessage());
      }catch (Exception e){
        model.addAttribute("error", e.getMessage());
      }
      return "admin/change-user-role";
  }

  @GetMapping("/remove-user-role/{role}/{id}")
  public String removeUserRole(Model model, @PathVariable String role, @PathVariable long id){
    try{
      model.addAttribute("allUsers", adminAppUserService.findAllUsers());
      adminAppUserService.removeRole(role, id);
      return "redirect:/admin/change-user-role";
    }catch (NoSuchUserByIdException e){
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      model.addAttribute("error", e.getMessage());
    }
    return "admin/change-user-role";
  }


}