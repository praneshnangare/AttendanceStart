package com.example.demo.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.persistence.metamodel.Metamodel;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String get_index(Principal principal, HttpSession session) {
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

	@GetMapping("/show-employee/{page}")
	public String showemployee(@PathVariable("page") Integer page, Model m) {
		m.addAttribute("title", "Show Employees");
		Pageable pageable = PageRequest.of(page, 7);
		Page<User> users = this.userDAO.findByRole("ROLE_USER", pageable);
		m.addAttribute("users", users);
		m.addAttribute("currentpage", page);
		m.addAttribute("TotalPages", users.getTotalPages());
		return "admin/show_employee";
	}

	@GetMapping("/view-employee/{id}")
	public String viewemployee(@PathVariable("id") Integer id, Model m) {
		Optional<User> userOptional = this.userDAO.findById(id);
		User user = userOptional.get();
		m.addAttribute("employee", user);
		m.addAttribute("title", "View Employee Details");
		return "admin/view_employee";
	}

	@GetMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable("id") Integer id, Model m , HttpSession session) {
		this.userDAO.deleteById(id);
		session.setAttribute("message" , new Message("Employee deleted successfully" , "alert-success"));
		return "redirect:/admin/show-employee/0";
	}
	
	@PostMapping("/update/{id}")
	public String updateEmployee(@PathVariable("id") Integer id , Model m ) {
		m.addAttribute("title" , "Update Employee");
		User user = this.userDAO.findById(id).get();
		System.out.println("ud->  " + user.getId());
		m.addAttribute("employee" , user);
		return "admin/update_employee";
	}
	
	@PostMapping("/process-update")
	public String saveUpdateEmployee(@ModelAttribute("employee") User user , Model m)
	{
		user.setRole("ROLE_USER");
		user.setPassword(this.userDAO.findById(user.getId()).get().getPassword());
		user.setEnabled(true);
		this.userDAO.save(user);
		return "redirect:/admin/view-employee/" + user.getId();
	}
	
}





































