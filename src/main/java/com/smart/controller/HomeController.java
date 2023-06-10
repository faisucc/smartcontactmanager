package com.smart.controller;

import com.smart.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Sign Up - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/doRegister")
	public String handlingUserRegistration(@ModelAttribute("user") User userModel, @RequestParam("confirmPassword") String pwdConfirm, Model model){
		System.out.println("User: " + userModel.toString());
		System.out.println("confirm Pwd" + pwdConfirm);
        return "signup";
	}
}
