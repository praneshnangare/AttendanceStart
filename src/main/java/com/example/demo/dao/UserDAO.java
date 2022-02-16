package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.User;

public interface UserDAO extends JpaRepository<User, Integer>{

	@Query("select u from User u where u.email = :email or u.name= :email or u.mobile=:email")
	public User getUserByUsername(@Param("email") String email);

	//@Query("from User as u where u.role = 'ROLE_USER')
	public Page<User> findByRole(String role ,  Pageable pePageable);
	
	public List<User> findByRole(String role);

	public List<User> findByRoleOrderByNameAsc(String string);
	
	public Optional<User> findById(Integer id);

//	public List<User> findOne(String employee);

	public List<User> findByName(String employee);

	public User findByMobile(String mobile);

	public List<User> findAllByMobile(String mobile);

	public List<User> findAllByAdhar(String adhar);

	public List<User> findAllByName(String name);

	public List<User> findAllByEmail(String email);
}
