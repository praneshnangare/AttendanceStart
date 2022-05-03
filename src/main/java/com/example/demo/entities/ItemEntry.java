package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="item_entry")
public class ItemEntry {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="itemId")
	private Item item;
	
	private Integer qty;
	
	private Integer rate;
	
	@ManyToOne
	@JoinColumn(name="invId")
	private Invoice invoice;
	
	
	public ItemEntry(Item item2, int i) {
		// TODO Auto-generated constructor stub
		this.item = item2;
		this.qty = i;
	}
	
	public ItemEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Integer getAmount() {
		// TODO Auto-generated method stub
		return this.qty*this.rate;
	}
	
}
