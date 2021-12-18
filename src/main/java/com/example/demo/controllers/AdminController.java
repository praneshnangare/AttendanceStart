package com.example.demo.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
@RequestMapping("/admin/")
public class AdminController {

	@Autowired
	private UserDAO userDAO;
	
	@ModelAttribute
	public void add_common_data(Model model, Principal principal) {
		String username = principal.getName();
		User admin = userDAO.getUserByUsername(username);
		model.addAttribute("user", admin);
	}
	
	@GetMapping("index")
	public String get_index() {
		return "admin/admin_dashboard";
	}
	
	@GetMapping("add-user")
	public String addUser(Model model) {
		model.addAttribute("employee" , new User());
		return "admin/add_employee";
	}
	
	@PostMapping("save-employee")
	public String saveUser(Model model, @ModelAttribute User user ,HttpSession session) {
		if(user != null) {
			userDAO.save(user);
		}
		else {
			System.out.println("null user submitted");
		}
		session.setAttribute("message", new Message("Employee has been added successfully" , "alert-success"));
		return "admin/add_employee";
	}
}
