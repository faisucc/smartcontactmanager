package com.smart.controller;
import com.smart.dao.ContactRepository;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.smart.dao.UserRepository;
import com.smart.entities.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.*;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository userRepository;
    @Autowired
	private ContactRepository contactRepository;
	@ModelAttribute
	public void addCommonData(Model model, Principal principal){
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		model.addAttribute("user", user);
	}

	@RequestMapping("/index")
	public String index(Model model, Principal principal, HttpSession session){
		if(null != session.getAttribute("message")){
			session.removeAttribute("message");
		}
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
	public String contactProcessing(@ModelAttribute("contact") Contact contact, Principal principal, BindingResult result, @RequestParam("profileImage")MultipartFile file, Model model, HttpSession session) throws Exception{
    try{
		if(null != session.getAttribute("message")){
			session.removeAttribute("message");
		}
		System.out.println("contact data "+ contact.toString());
//		if (result.hasErrors()) {
//			// Handle validation errors
//			session.setAttribute("message", new Message("Invalid phone no format" , "alert-danger"));
//			System.out.println("Error by phone: " + result.toString());
//			model.addAttribute("contact", contact);
//			return "add_contact_form";
//		}

		String numericRegex = "^[0-9]+$";
		if(contact.getPhone().length() != 10){
//			session.setAttribute("message", new Message("Phone number has to be 10 digits." , "alert-danger"));
			throw new Exception("Phone number has to be 10 digits.");
		}

		if (!Pattern.matches(numericRegex, contact.getPhone())){
//			session.setAttribute("message", new Message("Phone number must contain only numbers." , "alert-danger"));
			throw new Exception("Phone number must contain only numbers.");
		}

		if(!file.isEmpty()){
			contact.setImage(file.getOriginalFilename());
			File uploadedImage = new ClassPathResource("static/img").getFile();
			Path path = Paths.get(uploadedImage.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}
		else{
			String emptyFileName = "empty-pic.png";
			contact.setImage(emptyFileName);
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

	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page")Integer page, Model model, Principal principal, HttpSession session){
		if(null != session.getAttribute("message")){
			session.removeAttribute("message");
		}
	  model.addAttribute("title","Show contacts");
	  String email = principal.getName();
	  User user = userRepository.findByEmail(email);
//	  List<Contact> contacts = user.getContacts();
	  Page<Contact> contacts = contactRepository.findByUser(user, PageRequest.of(page,5));
	  model.addAttribute("contacts", contacts);
	  model.addAttribute("currentPage", page);
	  model.addAttribute("totalPages", contacts.getTotalPages());
      return "showContacts";
	}

	@RequestMapping("/contact/{cid}")
	public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session) throws Exception{
		try {
			if(null != session.getAttribute("message"))
				session.removeAttribute("message");
			if(!contactRepository.findById(cid).isPresent()){
				throw new Exception("This is an invalid operation");
			}
			Optional<Contact> contactOptional = this.contactRepository.findById(cid);
			Contact contact = contactOptional.get();
			System.out.println(contactOptional+"wtf"+contact.toString());
			String email = principal.getName();
			User user = userRepository.findByEmail(email);
			if (user.getId() == contact.getUser().getId()) {
				model.addAttribute("title","Contact - " + contact.getName() );
				model.addAttribute("contact", contact);
			}else {

				throw new Exception("This person is not present in your contact list.");
			}
		}catch (Exception e){
			e.printStackTrace();
			session.setAttribute("message", new Message( e.getMessage() , "alert-danger"));
		}
		return "contact_details";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) throws Exception{

		try{
			if(null != session.getAttribute("message")){
				session.removeAttribute("message");
			}
			if(!contactRepository.findById(cid).isPresent()){
				throw new Exception("This is an invalid operation");
			}
			Optional<Contact> contactOptional= this.contactRepository.findById(cid);
			Contact contact = contactOptional.get();
			String email = principal.getName();
			User user = userRepository.findByEmail(email);
			if (user.getId() == contact.getUser().getId()){
				user.getContacts().remove(contact);
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Contact deleted successfully", "success"));

			}else{
				throw new Exception("This person is not present in your contact list.");
			}
		}catch (Exception e){
			e.printStackTrace();
			session.setAttribute("message", new Message( e.getMessage() , "alert-danger"));
			return "showContacts";
		}
		return "redirect:/user/show-contacts/0";
	}

	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model, HttpSession session){
		if(null != session.getAttribute("message")){
			session.removeAttribute("message");
		}
		model.addAttribute("title", "Update Contact");
		Optional<Contact> contactOptional= this.contactRepository.findById(cid);
		Contact contact = contactOptional.get();
		model.addAttribute("contact", contact);
		return "update_form";
	}

	@PostMapping("/process-updated-contact")
	public String processTheUpdatedContact(@ModelAttribute Contact contact, Model model, @RequestParam("profileImage")MultipartFile file, HttpSession session, Principal principal){
        try{
			if(null != session.getAttribute("message")){
				session.removeAttribute("message");
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
			System.out.println(contact+"wtf");
            this.contactRepository.save(contact);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "redirect:/user/contact/"+contact.getCid();
	}

	@GetMapping("/profile")
	public String profile(Model m){
		m.addAttribute("title","Your Profile");

		return "profile";
	}

	@GetMapping("/settings")
	public String openSettings(){
		return "settings";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) throws Exception{
		try{
			String email = principal.getName();
			User currentUser = this.userRepository.findByEmail(email);
			String passwordRegExp ="^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
			if(this.bCryptPasswordEncoder.matches(oldPassword,currentUser.getPassword())){
				if(newPassword.length() < 8){
					throw new Exception("Password must be of at least 8 characters long.");
				}
				if(!Pattern.matches(passwordRegExp,newPassword)){
					throw new Exception("Your password must contain one lowercase letter, one uppercase letter, one number and one special character.");
				}
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				this.userRepository.save(currentUser);
				session.setAttribute("message", new Message("Password successfully updated.","alert-success"));
			}else{
				throw new Exception("Incorrect old password.");
			}
		} catch(Exception e){
			session.setAttribute("message", new Message( e.getMessage() , "alert-danger"));
			return "redirect:/user/settings";
		}

		return "redirect:/user/index";
	}
}
