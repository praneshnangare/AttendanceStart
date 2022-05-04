package com.example.demo.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.attoparser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.Customer;
import com.example.demo.entities.Invoice;
import com.example.demo.entities.Item;
import com.example.demo.entities.ItemEntry;
import com.example.demo.entities.User;
import com.example.demo.helper.InvoiceExporter;
import com.example.demo.services.InvoiceService;
@Controller
@RequestMapping("/admin/invoice")
public class InvoicesController {

	@Autowired
	private InvoiceService service;
	@Autowired
	private UserDAO userDAO;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	//private static final Gson gson = new Gson();
	
	@InitBinder
	public void initDateBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal , HttpSession session) {
		
		User user = (User)session.getAttribute("user");
		if(user == null && principal != null) {
			String username = principal.getName();
			user = userDAO.getUserByUsername(username);
			session.setAttribute("user", user);
		}
	}
	
	
	
	@GetMapping("/generateInvoices")
	public String generateInvoices(Model model , HttpServletResponse response) throws ParseException, IOException {
		List<Customer> customerList = service.getAllCustomers();
		List<Item> itemList = service.getAllItems();
		Invoice invoice = new Invoice();
		Integer num = service.getNewInvoiceNumber();
		invoice.setInvNo(num++);
		Date date = new Date();
		model.addAttribute("invNo", num);
		model.addAttribute("custList", customerList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("invoiceFormBean" , invoice);
		model.addAttribute("todaysDate", sdf.format(date));
		//service.export(response);
		return "admin/invoice/generate_invoice";
	}
	
	@PostMapping("/createone1")
	public HttpServletResponse createOne1(@RequestBody Invoice inv , HttpServletResponse response) throws IOException{
		Customer customer = service.getCustomerById(inv.getCustomer().getCustId());
		inv.setCustomer(customer);
		List<ItemEntry> items = new ArrayList<ItemEntry>();
		for(ItemEntry i : inv.getItemList()) {
			Item item = service.getItemById(i.getItem().getItemId());
			i.setItem(item);
			items.add(i);
		}
		inv.setItemList(items);
		service.SaveInvoice(inv , response);
		return response;
	}
	
	@PostMapping("/createone")
//	@ResponseStatus(value=HttpStatus.OK)
//	@ResponseBody
	public ResponseEntity<?> createOne(@RequestBody Invoice inv , HttpServletResponse response) throws IOException{
		Customer customer = service.getCustomerById(inv.getCustomer().getCustId());
		inv.setCustomer(customer);
		List<ItemEntry> items = new ArrayList<ItemEntry>();
		for(ItemEntry i : inv.getItemList()) {
			Item item = service.getItemById(i.getItem().getItemId());
			i.setItem(item);
			items.add(i);
		}
		inv.setItemList(items);
		Integer id = service.SaveInvoice(inv , response);
		return ResponseEntity.ok(id);
	}
	
	@GetMapping("/download-pdf/{id}")
	private void getDownloadPDF(@PathVariable("id") Integer id , Model model , HttpServletResponse response) throws IOException {
		Invoice invoice = service.getInvoiceByID(id);
		//service.ExportToExcel(invoice, response);
		service.ExportToPdf(invoice, response);	
	}
	
	@GetMapping("/download-excel/{id}")
	private void getDownloadExcel(@PathVariable("id") Integer id , Model model , HttpServletResponse response) throws IOException {
		Invoice invoice = service.getInvoiceByID(id);
		service.ExportToExcel(invoice, response);
		//service.ExportToPdf(invoice, response);	
	}
	
	@PostMapping("/save-item")
	public ResponseEntity<?> saveItem(@RequestBody Item item) {
		Item item1 = service.saveItem(item);
		return ResponseEntity.ok(item1);
	}
	
	@PostMapping("/save-customer" )
	public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
		Customer customer1 = service.saveCustomer(customer);
		return ResponseEntity.ok(customer1);
	}
	
	
	
	
}











