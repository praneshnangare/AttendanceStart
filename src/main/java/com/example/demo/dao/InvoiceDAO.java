package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Invoice;

public interface InvoiceDAO extends JpaRepository<Invoice, Integer>{

	Invoice findInvoiceByInvId(Integer id);
	Invoice findFirstByOrderByInvNoDesc();
}
