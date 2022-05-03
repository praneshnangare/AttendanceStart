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
@Table(name="item")
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer itemId;
	private String itemName;
	private String hsnCode;
	
	@OneToMany(cascade = CascadeType.ALL, fetch =  FetchType.LAZY , mappedBy = "item")
	private List<ItemEntry> itemEntries;
	
	public List<ItemEntry> getItemEntries() {
		return itemEntries;
	}

	public void setItemEntries(List<ItemEntry> itemEntries) {
		this.itemEntries = itemEntries;
	}

	public Item(String string, String string2) {
		// TODO Auto-generated constructor stub
		this.itemName = string;
		this.hsnCode = string2;
		
	}
	
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer id) {
		this.itemId = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	
}
