package com.example.demo.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.AttendanceDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entities.Attendance;
import com.example.demo.entities.User;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AttendanceDAO attendanceDAO;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@InitBinder
	public void initDateBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	@ModelAttribute
	public void add_common_data(Model model, Principal principal , HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if(user == null && principal != null) {
			String username = principal.getName();
			user = userDAO.getUserByUsername(username);
			session.setAttribute("user", user);
		}
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal , HttpSession session) {
		String username = principal.getName();
		User user = userDAO.getUserByUsername(username);
		session.setAttribute("user", user);
		model.addAttribute("title" , "Dashboard");
		return "normal/user_dashboard";
	}
	
	@GetMapping("/view")
	public String view1 (Model model, Principal principal , HttpSession session) {
		Date fromdate = new Date();
		Date todate = new Date();
		
		return "redirect:view/?from="+sdf.format(fromdate)+"&to="+sdf.format(todate);
	}
	@GetMapping("/view/")
	public String view(@RequestParam(value = "from") Date from, @RequestParam(value = "to") Date to,
			Model model, Principal principal , HttpSession session) {
		String username = principal.getName();
		User user = userDAO.getUserByUsername(username);
		
		List<Attendance> ls= attendanceDAO.findByNameAndAttendanceDateBetweenOrderByAttendanceDateAsc(user.getName(),from , to);
		
		List<String> datels = ls.stream().map(x->sdf.format(x.getAttendanceDate())).collect(Collectors.toList());
		Integer total = 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("present" , 0);
		map.put("absent" , 0);
		map.put("halfDay" , 0);
		map.put("total" , ls.size());
		
		for(Attendance x : ls) {
			datels.add(sdf.format(x.getAttendanceDate()));
			map.put(x.getStatus() , map.get(x.getStatus())+1);
		}
		model.addAttribute("maps", map);
		model.addAttribute("records" , ls);
		model.addAttribute("datels" , datels);
		model.addAttribute("title" , "View Attendance");
		model.addAttribute("fromDate" , sdf.format(from));
		model.addAttribute("toDate" , sdf.format(to));
		return "normal/viewAttendance";
	}
}















