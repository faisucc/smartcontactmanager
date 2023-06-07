package com.smart.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.smart.dao.UserRepository;
import com.smart.entities.*;
@Controller
public class UserController {
    
	@Autowired
	private UserRepository userRepository;
	@GetMapping("/test")
	@ResponseBody
	public String checkDatabase() {
		User user =new User();
		user.setName("ammu");
		user.setEmail("amritha1@gmail.com");
		userRepository.save(user);
		return "Working";
	}
}
