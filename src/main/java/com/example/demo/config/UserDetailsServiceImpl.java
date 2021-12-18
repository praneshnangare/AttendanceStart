package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;

import org.springframework.security.core.userdetails.UserDetailsService;
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user  = userDAO.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user!!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}

}
