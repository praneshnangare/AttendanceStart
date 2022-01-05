package com.example.demo.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.User;

public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email = :email")
	public User getUserByUsername(@Param("email") String email);

	//@Query("from User as u where u.role = 'ROLE_USER')
	public Page<User> findByRole(String role ,  Pageable pePageable);
	
	public List<User> findByRole(String role);
	
}
