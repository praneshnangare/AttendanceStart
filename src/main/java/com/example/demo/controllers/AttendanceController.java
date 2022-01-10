package com.example.demo.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.AttendanceDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entities.Attendance;
import com.example.demo.entities.AttendanceForm;
import com.example.demo.entities.User;
import com.example.demo.helper.ExcelExporter;

@Controller
@RequestMapping("/admin")
public class AttendanceController {

	@Autowired 
	private AttendanceDAO attendanceDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	 @InitBinder
	 public void initDateBinder(final WebDataBinder binder) {
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	 }
	 
	@GetMapping("/mark-attendance")
	public String showCreateForm(Model model) throws ParseException {
		List<User> users = this.userDAO.findByRole("ROLE_USER");
		AttendanceForm attendanceForm = new AttendanceForm();
	    for (User user: users) {
	    	Attendance attendance = new Attendance();
	    	attendance.setName(user.getName());
	    	attendance.setAttendanceDate(new Date());
	        attendanceForm.addItem(attendance);
	    }
	    
//	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	    Date date = new Date();//formatter.parse(formatter.format(new Date()));
	    attendanceForm.setDate(date);
	    model.addAttribute("form", attendanceForm);
	    return "admin/mark_attendance";
	}
	
	@PostMapping("/save")
	public String saveRecords(@ModelAttribute("form") AttendanceForm b) {	
		attendanceDAO.saveAll(b.getAttendanceList());
		return "admin/mark_attendance";
	}
	
	@GetMapping("/fetchRecords")
	public String getAttendanceRecords(Model model) {
		
		Date fromdate = new Date();
		Date todate = new Date();
		String employee = "";
		return "redirect:fetchRecords/?from="+sdf.format(fromdate) + "&to=" + sdf.format(todate) + "&emp="+ employee;
	}
	
	@GetMapping("/fetchRecords/")
	public String getAttendance(@RequestParam("from") Date from, @RequestParam("to") Date to, 
			@RequestParam("emp") String employee , Model model) {
		List<User> users = this.userDAO.findByRole("ROLE_USER");
		List<Attendance> records = null;
		if("".equals(employee)) {
			records = attendanceDAO.findByAttendanceDateBetween(from , to);
		}
		else {
			records = attendanceDAO.findByNameAndAttendanceDateBetween(employee, from , to);
		}
		model.addAttribute("selectedEmp" , employee);
		model.addAttribute("records", records);
		model.addAttribute("users", users);
		model.addAttribute("fromDate", sdf.format(from));
		model.addAttribute("toDate", sdf.format(to));
		model.addAttribute("emp", employee);
		model.addAttribute("title" , "Attendance Report");
		return "admin/AttendanceReport";
	}
	
	@GetMapping("/downloadRecords")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_emp" + ".xlsx";
		response.setHeader(headerKey, headerValue);

//		List<User> users = this.userDAO.findByRoleOrderByNameAsc("ROLE_USER");
		List<Attendance> records = this.attendanceDAO.findAllOrderByNameAsc();
		ExcelExporter excelExporter = new ExcelExporter(records);
		excelExporter.export(response);
	}
}





