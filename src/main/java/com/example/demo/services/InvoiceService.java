package com.example.demo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.InvoiceDAO;
import com.example.demo.dao.ItemDAO;
import com.example.demo.dao.ItemEntryDAO;
import com.example.demo.entities.Attendance;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Invoice;
import com.example.demo.entities.Item;
import com.example.demo.entities.ItemEntry;
import com.example.demo.entities.User;
import com.example.demo.helper.ExcelExporter;
import com.example.demo.helper.InvoiceExporter;
import com.example.demo.helper.InvoiceExporterPDF;


@Service
public class InvoiceService {

	@Autowired
	private CustomerDAO customerDAO;
	@Autowired
	private ItemDAO itemDAO;
	@Autowired
	private ItemEntryDAO itemEntryDAO;
	@Autowired
	private InvoiceDAO invoiceDAO;

	public Integer getNewInvoiceNumber() {
		Invoice inv =  invoiceDAO.findFirstByOrderByInvNoDesc();
		if (inv == null) {
			return 0;
		}
		return inv.getInvNo();
	}
	public Customer saveCustomer(Customer customer) {
		return this.customerDAO.save(customer);
	}

	public Item saveItem(Item item) {
		return this.itemDAO.save(item);
		
	}
	
	public Integer SaveInvoice(Invoice inv2 , HttpServletResponse response) throws IOException {
		List<ItemEntry> ls = inv2.getItemList();
		Integer rate = 0;
		Integer qty = 0;
		Integer amount = 0;
		for(ItemEntry entry : ls) {
			rate = entry.getRate();
			qty = entry.getQty();
			amount += rate * qty;
		}
		inv2.setTotalAmount(amount);
		Invoice invoice = invoiceDAO.save(inv2);
		List<ItemEntry> ls1 = inv2.getItemList();
		ls1.forEach(a -> a.setInvoice(invoice));		
		itemEntryDAO.saveAll(ls1);
		return invoice.getInvId();
		//ExportToExcel(invoice, response);
	}
	
	public void exportToExcel(HttpServletResponse response) throws IOException {
		Invoice invoice = new Invoice();
		this.ExportToExcel(invoice , response);
	}
	
	public void exportToPdf(HttpServletResponse response) throws IOException {
		Invoice invoice = new Invoice();
		this.ExportToPdf(invoice , response);
	}
	
	public void ExportToExcel(Invoice inv , HttpServletResponse response) throws IOException {
		//response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_emp" + ".xlsx";
		response.setHeader(headerKey, headerValue);
		
		InvoiceExporter excelExporter = new InvoiceExporter(inv);
		excelExporter.export(response);
	}
	
	public void ExportToPdf(Invoice inv , HttpServletResponse response) throws IOException {
		//response.setContentType("application/octet-stream");
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_emp" + ".PDF";
		response.setHeader(headerKey, headerValue);
		
		InvoiceExporterPDF excelExporterPdf = new InvoiceExporterPDF(inv);
		excelExporterPdf.export(response);
	}
	public List<Invoice> getInvoices() {
		List<Invoice> invoices = this.invoiceDAO.findAll();
		return invoices;
	}
	public Invoice getInvoiceByID(Integer id) {
		return this.invoiceDAO.findInvoiceByInvId(id);
	}
	public List<Customer> getAllCustomers(){
		return customerDAO.findAll();
	}
	
	public List<Item> getAllItems(){
		return itemDAO.findAll();
	}
	
	public Customer getCustomerById(Integer id) {
		return customerDAO.findCustomerByCustId(id);
	}
	
	public Item getItemById(Integer id) {
		return itemDAO.findItemByItemId(id);
	}
}









