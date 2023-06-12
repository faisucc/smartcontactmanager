package com.smart.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.smart.dao.UserRepository;
import com.smart.entities.*;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserRepository userRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal){
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String index(Model model, Principal principal){
		model.addAttribute("title", "User Dashboard - Smart Contact Manager");
		return "user_dashboard";
	}

	@GetMapping("/add_contact")
	public String addContactForm(Model model){
		model.addAttribute("title", "Add Contact - Smart Contact Manager");
		model.addAttribute("contact", new Contact());

		return "user/add_contact_form";
	}

}
