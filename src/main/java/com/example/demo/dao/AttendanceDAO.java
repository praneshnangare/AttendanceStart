package com.example.demo.dao;

import java.util.Date;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Attendance;

public interface AttendanceDAO extends JpaRepository<Attendance, Integer> {
	public List<Attendance> findByAttendanceDate(Date attendanceDate);

	public List<Attendance> findByAttendanceDateBetween(Date from, Date to);

	public List<Attendance> findByNameAndAttendanceDateBetween(String employee, Date from, Date to);

	public List<Attendance> findAllByOrderByNameAsc();

	//public List<Attendance> findByNameAndAttendanceDateBetweenSorted(String name, Date from, Date to);

	public List<Attendance> findByNameAndAttendanceDateBetweenOrderByAttendanceDateAsc(String name, Date from, Date to);

	public List<Attendance> findByAttendanceDateBetweenOrderByAttendanceDateAsc(Date from, Date to);

}
