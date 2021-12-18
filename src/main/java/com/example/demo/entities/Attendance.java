package com.example.demo.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;
import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "ATTENDANCE")
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer atdId;
	private String name;
	private String status;
	private Integer extraHours;
	private String remarks;
	@DateTimeFormat(pattern = "dd-MMM-yyyy")
	private Date attendanceDate;
	
	@ManyToOne
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Integer getAtdId() {
		return atdId;
	}
	public void setAtdId(Integer atdId) {
		this.atdId = atdId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getExtraHours() {
		return extraHours;
	}
	public void setExtraHours(Integer extraHours) {
		this.extraHours = extraHours;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Date getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	
	
}
