package com.example.demo.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.example.demo.dao.PaymentInfoDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entities.Attendance;
import com.example.demo.entities.AttendanceForm;
import com.example.demo.entities.PaymentInfo;
import com.example.demo.entities.User;
import com.example.demo.helper.ExcelExporter;
import com.example.demo.helper.Message;
import com.example.demo.helper.SmsUtility;

@Controller
@RequestMapping("/admin")
public class AttendanceController {

	@Autowired
	private AttendanceDAO attendanceDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PaymentInfoDAO paymentInfoDAO;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@InitBinder
	public void initDateBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	@GetMapping("/mark-attendance")
	public String showCreateForm(Model model) throws ParseException {
		Date date = new Date();
		return "redirect:mark-attendance/?date=" + sdf.format(date);
	}

	@GetMapping("/mark-attendance/")
	public String markattendance(@RequestParam("date") Date date, Model model) {

		List<User> users = this.userDAO.findByRole("ROLE_USER");
		List<Attendance> records = this.attendanceDAO.findByAttendanceDate(date);
		AttendanceForm attendanceForm = new AttendanceForm();
		if (records.size() != users.size()) {
			for (User user : users) {
				boolean flg = false;
				Attendance rec = null;
				for (Attendance recordItem : records) {
					if (user.getName().equals(recordItem.getName())) {
						flg = true;
						rec = recordItem;
						break;
					} else {
						flg = false;
					}
				}
				if (!flg) {
					Attendance attendance = new Attendance();
					attendance.setUser(user);
					attendance.setName(user.getName());
					attendance.setAttendanceDate(new Date());
					attendanceForm.addItem(attendance);
				} else {
					attendanceForm.addItem(rec);
				}
			}
		} else {
			attendanceForm.setAttendanceList(records);
		}

		attendanceForm.setDate(date);
		model.addAttribute("form", attendanceForm);
		return "admin/Mark_Attendance";
	}

	@PostMapping("/save")
	public String saveRecords(@ModelAttribute("form") AttendanceForm b, Model model , HttpSession session) {
		List<Attendance> ls = b.getAttendanceList();
		List<String> s = Arrays.asList("present", "halfDay", "absent");
		for(Attendance a : ls) {
			if(s.contains(a.getStatus())) {
				a.setAttendanceDate(b.getDate());
			}
			else {
				session.setAttribute("message" , new Message("Attendance Could not be saved. Please save it again!" , "alert-danger"));
				return "admin/Mark_Attendance";
			}
		}
		attendanceDAO.saveAll(ls);
		b.getAttendanceList().stream().forEach(x -> System.out.println(x.getUser().getName()));
		if ((boolean) b.getNotify()) {
			String message = "You have been marked : ";

			b.getAttendanceList().stream()
					.forEach(x -> SmsUtility.sendSms(x.getUser().getMobile(), message + x.getStatus()));
		}
		session.setAttribute("message", new Message("Attendance has been saved successfully", "alert-success"));
		return "admin/Mark_Attendance";
	}

	@GetMapping("/fetchRecords")
	public String getAttendanceRecords(Model model) {
		Date fromdate = new Date();
		Date todate = new Date();
		String employee = "";
		return "redirect:fetchRecords/?from=" + sdf.format(fromdate) + "&to=" + sdf.format(todate) + "&emp=" + employee;
	}

	@GetMapping("/fetchRecords/")
	public String getAttendance(@RequestParam("from") Date from, @RequestParam("to") Date to,
			@RequestParam("emp") String employee, Model model) {
		List<User> users = this.userDAO.findByRole("ROLE_USER");
		List<Attendance> records = null;
		if ("".equals(employee)) {
			records = attendanceDAO.findByAttendanceDateBetweenOrderByAttendanceDateAsc(from, to);
		} else {
			records = attendanceDAO.findByNameAndAttendanceDateBetweenOrderByAttendanceDateAsc(employee, from, to);
		}
		List<String> datels = records.stream().map(x->sdf.format(x.getAttendanceDate())).collect(Collectors.toList());
		model.addAttribute("datels" , datels);
		model.addAttribute("selectedEmp", employee);
		model.addAttribute("records", records);
		model.addAttribute("users", users);
		model.addAttribute("fromDate", sdf.format(from));
		model.addAttribute("toDate", sdf.format(to));
		model.addAttribute("emp", employee);
		model.addAttribute("title", "Attendance Report");
		return "admin/AttendanceReport";
	}

	@GetMapping("/downloadRecords")
	public void exportToExcel(HttpServletResponse response, @RequestParam("from") Date from,
			@RequestParam("to") Date to, @RequestParam("emp") String employee) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_emp" + ".xlsx";
		response.setHeader(headerKey, headerValue);
		// List<Attendance> records = this.attendanceDAO.findAllByOrderByNameAsc();
		// List<User> users = this.userDAO.findByRoleOrderByNameAsc("ROLE_USER");
		List<User> users = this.userDAO.findByRole("ROLE_USER");
		List<Attendance> records = null;
		if ("".equals(employee)) {
			records = attendanceDAO.findByAttendanceDateBetween(from, to);
			users = this.userDAO.findByRole("ROLE_USER");
		} else {
			records = attendanceDAO.findByNameAndAttendanceDateBetween(employee, from, to);
			users = this.userDAO.findByName(employee);
		}
		Map<User, List<Attendance>> map = createAttendanceMap(users, records);
		ExcelExporter excelExporter = new ExcelExporter(map);
		excelExporter.export(response);
	}

	public Map<User, List<Attendance>> createAttendanceMap(List<User> users, List<Attendance> records) {
		Map<User, List<Attendance>> map = new HashMap<User, List<Attendance>>();
		for (User user : users) {

			List<Attendance> ls = records.stream().filter(record -> record.getName().equals(user.getName()))
					.collect(Collectors.toList());
			map.put(user, ls);

		}
		return map;
	}

}
