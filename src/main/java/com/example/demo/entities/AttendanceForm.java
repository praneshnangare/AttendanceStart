package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

public class AttendanceForm {
	private List<Attendance> attendanceList;

	public void addItem(Attendance attendance) {
		this.attendanceList.add(attendance);
	}
	public AttendanceForm(List<Attendance> attendanceList) {
		super();
		this.attendanceList = attendanceList;
	}
	public AttendanceForm() {
		super();
		this.attendanceList = new ArrayList<Attendance>();
	}

	public List<Attendance> getAttendanceList() {
		return attendanceList;
	}

	public void setAttendanceList(List<Attendance> attendanceList) {
		this.attendanceList = attendanceList;
	}
	
}
