package com.smart.controller;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.smart.dao.UserRepository;
import com.smart.entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

		return "add_contact_form";
	}

	@PostMapping("/process-contact")
	public String contactProcessing(@Valid @ModelAttribute("contact") Contact contact, Principal principal, BindingResult result, HttpSession session, Model model, @RequestParam("profileImage")MultipartFile file){
    try{
		//System.out.println("contact data "+ contact.toString());
		if (result.hasErrors()) {
			// Handle validation errors
			session.setAttribute("message", new Message("Invalid phone no format" , "alert-danger"));
			System.out.println("Error by phone: " + result.toString());
			model.addAttribute("contact", contact);
			return "add_contact_form";
		}
		if(!file.isEmpty()){
			contact.setImage(file.getOriginalFilename());
			File uploadedImage = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(uploadedImage.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}
		String userName = principal.getName();
		User user = userRepository.findByEmail(userName);
		contact.setUser(user);
		user.getContacts().add(contact);
		userRepository.save(user);
		session.setAttribute("message", new Message("Contact has been successfully added.", "alert-success"));
	}catch (Exception e){
		e.printStackTrace();
		session.setAttribute("message", new Message("Something went wrong. Please try again. " +e.getMessage() , "alert-danger"));
	}

	return "add_contact_form";
	}

}
