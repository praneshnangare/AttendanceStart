package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "PAYMENT_INFO")
public class PaymentInfo {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "emp_id")
	private User emp;
	
	private Integer january;
	private Integer february;
	private Integer march;
	private Integer april;
	private Integer may;
	private Integer june;
	private Integer july;
	private Integer august;
	private Integer september;
	private Integer october;
	private Integer november;
	private Integer december;
	
	
	
	public PaymentInfo() {
		super();
	}
	public PaymentInfo(User emp) {
		super();
		this.emp = emp;
	}
	public User getEmp() {
		return emp;
	}
	public void setEmp(User emp) {
		this.emp = emp;
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJanuary() {
		return january;
	}
	public void setJanuary(Integer january) {
		this.january = january;
	}
	public Integer getFebruary() {
		return february;
	}
	public void setFebruary(Integer february) {
		this.february = february;
	}
	public Integer getMarch() {
		return march;
	}
	public void setMarch(Integer march) {
		this.march = march;
	}
	public Integer getApril() {
		return april;
	}
	public void setApril(Integer april) {
		this.april = april;
	}
	public Integer getMay() {
		return may;
	}
	public void setMay(Integer may) {
		this.may = may;
	}
	public Integer getJune() {
		return june;
	}
	public void setJune(Integer june) {
		this.june = june;
	}
	public Integer getJuly() {
		return july;
	}
	public void setJuly(Integer july) {
		this.july = july;
	}
	public Integer getAugust() {
		return august;
	}
	public void setAugust(Integer august) {
		this.august = august;
	}
	public Integer getSeptember() {
		return september;
	}
	public void setSeptember(Integer september) {
		this.september = september;
	}
	public Integer getOctober() {
		return october;
	}
	public void setOctober(Integer october) {
		this.october = october;
	}
	public Integer getNovember() {
		return november;
	}
	public void setNovember(Integer november) {
		this.november = november;
	}
	public Integer getDecember() {
		return december;
	}
	public void setDecember(Integer december) {
		this.december = december;
	}
}









