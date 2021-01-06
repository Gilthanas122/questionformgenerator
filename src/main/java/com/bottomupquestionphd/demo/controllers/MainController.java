package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.InputTest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

  @GetMapping("render-input")
  public String renderInput(Model model){
    model.addAttribute("inputTest", new InputTest());
    return "indextry";
  }

  @PostMapping("save-input")
  public String saveTest(@ModelAttribute InputTest inputTest){
    return "redirect:/render-input";
  }
}
