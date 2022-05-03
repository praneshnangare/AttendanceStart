package com.example.demo.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer custId;
	private String name;
	private String address;
	private String gstNumber;
	private String vendorCode;
	
	public Customer(String string, String string2, String string3, String string4) {
		// TODO Auto-generated constructor stub
		this.name = string;
		this.address = string2;
		this.gstNumber = string3;
		this.vendorCode = string4;
	}
	
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public List<Invoice> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY , mappedBy = "customer")
	private List<Invoice> invoices;
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer id) {
		this.custId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	
	
}
