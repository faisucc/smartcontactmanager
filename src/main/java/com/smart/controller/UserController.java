package com.smart.controller;
import com.smart.dao.ContactRepository;
import com.smart.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
	public String contactProcessing(@ModelAttribute("contact") Contact contact, Principal principal, BindingResult result, @RequestParam("profileImage")MultipartFile file, Model model, HttpSession session) throws Exception{
    try{
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
	public String showContacts(@PathVariable("page")Integer page, Model model, Principal principal){
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
				contact.setUser(null);
				this.contactRepository.delete(contact);
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

}
