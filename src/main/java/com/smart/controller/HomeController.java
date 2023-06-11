package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;
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
	public String handlingUserRegistration(@Valid @ModelAttribute("user") User userModel, BindingResult result, @RequestParam("confirmPassword") String pwdConfirm, Model model, HttpSession session) throws Exception{
		try{
			System.out.println("User: " + userModel.toString());
			System.out.println("confirm Pwd: " + pwdConfirm);

			if(!pwdConfirm.equals(userModel.getPassword())){
				System.out.println("Passwords do not match.");
				throw new Exception("Passwords do not match.");
			}

			if(result.hasErrors()){
				System.out.println("ERROR " + result.toString());
				model.addAttribute("user", userModel);
				return "signup";
			}

			userModel.setRole("USER");
			userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));

			User saved = this.userRepository.save(userModel);
			model.addAttribute("user", saved);
			session.setAttribute("message", new Message("You have been successfully registered.", "alert-success"));
		} catch (Exception e){
			e.printStackTrace();
			model.addAttribute("user", userModel);
			session.setAttribute("message", new Message("Something went wrong. Please try again. " +e.getMessage() , "alert-danger"));
		}
		return "signup";
	}

	@RequestMapping("/signin")
	public String customLogin(Model model){
		model.addAttribute("title", "Login - Smart Contact Manager");
		return "login";
	}
}