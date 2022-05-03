package com.example.demo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name= "invoice")
public class Invoice {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer invId;
	
	@ManyToOne
	@JoinColumn(name="custId")
	private Customer customer;

	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY , mappedBy = "invoice")
	private List<ItemEntry> itemList = new ArrayList<ItemEntry>();
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private Integer totalAmount;
	
	private Integer invNo;
	
	public Integer getInvNo() {
		return invNo;
	}

	public void setInvNo(Integer invNo) {
		this.invNo = invNo;
	}

	public Invoice(Customer customer2, Date date2) {
		// TODO Auto-generated constructor stub
		this.customer = customer2;
		this.date = date2;
	}
	
	
	public Invoice() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getInvId() {
		return invId;
	}
	public void setInvId(Integer id) {
		this.invId = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<ItemEntry> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemEntry> itemList) {
		this.itemList = itemList;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Integer getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}


	
	
}
