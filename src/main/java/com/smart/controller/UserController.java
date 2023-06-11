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

	@RequestMapping("/index")
	public String index(Model model, Principal principal){
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
		return "user_dashboard";
	}

}
