package com.example.demo.entities;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class AttendanceForm {
	private List<Attendance> attendanceList;
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private Date date;


	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void addItem(Attendance attendance) {
		this.attendanceList.add(attendance);
	}
	public AttendanceForm(List<Attendance> attendanceList , Date date) {
		super();
		this.attendanceList = attendanceList;
		this.date = date;
	}
	public AttendanceForm() {
		super();
		this.attendanceList = new ArrayList<Attendance>();
		this.date = new Date();
	}

	public List<Attendance> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<Attendance> attendanceList) {
		this.attendanceList = attendanceList;
	}
	
}
