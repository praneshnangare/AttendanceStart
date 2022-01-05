package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dao.AttendanceDAO;
import com.example.demo.dao.BooksCreationDto;
import com.example.demo.dao.UserDAO;
import com.example.demo.entities.Attendance;
import com.example.demo.entities.AttendanceForm;
import com.example.demo.entities.Book;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;

@Controller
@RequestMapping("/admin")
public class AttendanceController {

	@Autowired 
	private AttendanceDAO attendanceDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@GetMapping("/mark-attendance1")
	public String markAttendance(Model model){
//		List<User> users = this.userDAO.findByRole("ROLE_USER");
//		List<Attendance> list = new ArrayList<Attendance>();
//		for(User user : users) {
//			Attendance attendance = new Attendance();
//			attendance.setName(user.getName());
//			list.add(attendance);
//		}
//		AttendanceForm attendanceForm = new AttendanceForm(list);
////		System.out.println("before " + attendanceForm.getAttendanceList());
//		model.addAttribute("form" , attendanceForm);
//		model.addAttribute("title" , "Mark Attendance");
//		model.addAttribute("ls" , users);
//		return "admin/mark_attendance";
		
		AttendanceForm booksForm = new AttendanceForm();
	    for (int i = 1; i <= 3; i++) {
	        booksForm.addItem(new Attendance());
	    }
	    
	    model.addAttribute("form", booksForm);
	    return "admin/mark_attendance";
	}
	
	@PostMapping("/save1")
	public String saveAttendance(@ModelAttribute("attendanceForm") AttendanceForm attendanceForm, Model model
			, HttpSession session) {
		if(attendanceForm != null) {
			if(attendanceForm.getAttendanceList() != null) {
				System.out.println("everythings ok");
			}
			else {
				System.out.println("attendance list is null");
			}
		}
		else {
			System.out.println("attendance form  null");
		}
		System.out.println(attendanceForm);
		session.setAttribute("message", new Message("Attendance has been saved", "alert-success"));

		return "admin/mark_attendance";
	}
	
	
	@GetMapping("/mark-attendance")
	public String showCreateForm(Model model) {
//	    BooksCreationDto booksForm = new BooksCreationDto();
//	    for (int i = 1; i <= 3; i++) {
//	        booksForm.addBook(new Book());
//	    }
//	    
//	    model.addAttribute("form", booksForm);
		List<User> users = this.userDAO.findByRole("ROLE_USER");
		AttendanceForm attendanceForm = new AttendanceForm();
	    for (User user: users) {
	    	Attendance attendance = new Attendance();
	    	attendance.setName(user.getName());
	        attendanceForm.addItem(attendance);
	    }
	    model.addAttribute("form", attendanceForm);
	    return "admin/mark_attendance";
	}
	@PostMapping("/save")
	public String saveRecords(@ModelAttribute("form") AttendanceForm b) {
		System.out.println(b.getAttendanceList().size());
//		b.getAttendanceList().forEach((c) -> System.out.println(c.getName()));
		attendanceDAO.saveAll(b.getAttendanceList());
		return "admin/mark_attendance";
	}
	

	
	
	@PostMapping("/save2")
	public String saveRecords(@ModelAttribute("form") BooksCreationDto b) {
		System.out.println(b.getBooks().size());
		b.getBooks().forEach((c) -> System.out.println(c.getAuthor()));
		return "admin/createBooksForm";
	}
}





