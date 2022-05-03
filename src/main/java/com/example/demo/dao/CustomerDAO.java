package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Customer;

public interface CustomerDAO extends JpaRepository<Customer, Integer>{

	public Customer findCustomerByCustId(Integer id);

}
