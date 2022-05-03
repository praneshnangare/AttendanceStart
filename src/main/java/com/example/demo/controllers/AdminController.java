package com.example.demo.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.metamodel.Metamodel;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.PaymentInfo;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
@RequestMapping("/admin/")
@SessionAttributes("admin")
public class AdminController {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal , HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if(user == null && principal != null) {
			String username = principal.getName();
			user = userDAO.getUserByUsername(username);
			session.setAttribute("user", user);
		}
	}

	@GetMapping("index")
	public String get_index(Principal principal, Model model) {
		model.addAttribute("title", "Admin Dashboard");
		return "admin/admin_dashboard";
	}

	@GetMapping("add-user")
	public String addUser(Model model) {
		model.addAttribute("title" , "Add Employee");
		model.addAttribute("employee", new User());
		return "admin/add_employee";
	}

	@PostMapping("save-employee")
	public String saveUser(Model model, @ModelAttribute User user,@RequestParam("isAdmin") Boolean isAdmin, HttpSession session) {
		
		try {
			
			List<User> user1 = userDAO.findAllByMobile(user.getMobile());
			List<User> user2 = userDAO.findAllByName(user.getName());
			List<User> user3 = userDAO.findAllByAdhar(user.getAdhar());
			List<User> user4 = userDAO.findAllByEmail(user.getEmail());
			if(user1.size() > 0) {
				throw new Exception("User already exists with same Mobile Number!");
			}
			if(user2.size() > 0) {
				throw new Exception("User already exists with same Name!");
			}
			if(user3.size() > 0) {
				throw new Exception("User already exists with same Adhar!");
			}
			if(user4.size() > 0) {
				throw new Exception("User already exists with same Email!");
			}
			if(isAdmin) {user.setRole("ROLE_ADMIN");}
			else {user.setRole("ROLE_USER");}
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode("welcome"));
			user.setDOJ(new Date());
			User result = this.userDAO.save(user);
			model.addAttribute("employee", new User());
			session.setAttribute("message", new Message("Employee has been added successfully", "alert-success"));
		}catch (Exception e) {
			//e.printStackTrace();
			model.addAttribute("employee", user);
			session.setAttribute("message", new Message("Something went wrong !! "+e.getMessage() , "alert-danger"));
		}
		return "admin/add_employee";
		
	}

	@GetMapping("/show-employee/{page}")
	public String showemployee(@PathVariable("page") Integer page, Model m) {
		int itemsPerPage = 10;
		Pageable pageable = PageRequest.of(page, itemsPerPage);
		Page<User> users = this.userDAO.findByRoleOrderByNameAsc("ROLE_USER", pageable);
		
		m.addAttribute("title", "Show Employees");
		m.addAttribute("users", users);
		m.addAttribute("currentpage", page);
		m.addAttribute("startIndex" , page*itemsPerPage);
		m.addAttribute("TotalPages", users.getTotalPages());
		return "admin/show_employee";
	}

	@GetMapping("/view-employee/{id}")
	public String viewemployee(@PathVariable("id") Integer id, Model m) {
		Optional<User> userOptional = this.userDAO.findById(id);
		User user = userOptional.get();
		m.addAttribute("title" , "View Employee");
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
	
	@GetMapping("/update/{id}")
	public String updateEmployee(@PathVariable("id") Integer id , Model m ) {
		m.addAttribute("title" , "Update Employee");
		User user = this.userDAO.findById(id).get();
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
	
	@GetMapping("/profiles")
	public String profileUpdate() {
		return "d";
		}
	
}





































