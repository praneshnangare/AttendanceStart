package com.example.demo.controllers;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
@RequestMapping("/admin/")
@SessionAttributes("admin")
public class AdminController {

	@Autowired
	private UserDAO userDAO;

//	@ModelAttribute
//	public void add_common_data(Model model, Principal principal) {
//		String username = principal.getName();
//		User admin = userDAO.getUserByUsername(username);
//		model.addAttribute("admin", admin);
//	}

	@GetMapping("index")
	public String get_index(Principal principal , HttpSession session) {
		String username = principal.getName();
		User admin = userDAO.getUserByUsername(username);
		session.setAttribute("admin", admin);
		return "admin/admin_dashboard";
	}

	@GetMapping("add-user")
	public String addUser(Model model) {
		model.addAttribute("employee", new User());
		return "admin/add_employee";
	}

	@PostMapping("save-employee")
	public String saveUser(Model model, @ModelAttribute User user, HttpSession session) {
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setDOJ(new Date());
		User result = this.userDAO.save(user);
		model.addAttribute("employee", new User());
		session.setAttribute("message", new Message("Employee has been added successfully", "alert-success"));
		return "admin/add_employee";
	}
}
