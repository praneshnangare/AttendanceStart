package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Attendance;


public interface AttendanceDAO extends JpaRepository<Attendance, Integer>{

}
