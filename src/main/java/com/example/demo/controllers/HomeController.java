package com.example.demo.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
public class HomeController {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title" ,"Pranesh Enterprises");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title" ,"About-Pranesh Enterprises");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title" ,"Signup-Pranesh Enterprises");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	//This handler for registering user
	@RequestMapping(value = "/do_register" , method=RequestMethod.POST)
	public String registeruser(@ModelAttribute("user") User user , 
			@RequestParam(value = "agreement" , defaultValue = "false") boolean agreement , 
			Model model ,HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("not agreed");
				throw new Exception("You have not agreed terms and conditions.");
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("Agreement  " + agreement);
			System.out.println(user);
			User result = this.userDAO.save(user);
			model.addAttribute("user" , new User());
			session.setAttribute("message", new Message("Successfully registered" , "alert-success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user" , user);
			session.setAttribute("message", new Message("Something went wrong !! "+e.getMessage() , "alert-danger"));
		}
		return "signup";
	}

//handler for custom login
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title","Login Page");
		return "login";
	}
}
