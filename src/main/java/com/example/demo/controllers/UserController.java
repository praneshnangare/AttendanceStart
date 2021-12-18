package com.example.demo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDAO userDAO;
	
	@ModelAttribute
	public void add_common_data(Model model, Principal principal) {
		String username = principal.getName();
		User user = userDAO.getUserByUsername(username);
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		return "normal/user_dashboard";
	}
	
	
	
	
}
