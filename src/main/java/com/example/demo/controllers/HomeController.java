package com.example.demo.controllers;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.UserDAO;
import com.example.demo.entities.User;
import com.example.demo.helper.Message;
import com.example.demo.helper.SmsUtility;

@Controller
public class HomeController {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	private String otpVar = "";
	private boolean otpsent = false;
	private boolean otpverified =false;
	private boolean  standalone = false;
	private User temp;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title" ,"Pranesh Enterprises");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title" ,"About-Pranesh Enterprises");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title" ,"Signup-Pranesh Enterprises");
		model.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping("/standalone/signup")
	public String signupstandalone(Model model) {
		model.addAttribute("title" ,"Signup-Pranesh Enterprises");
		model.addAttribute("user",new User());
		standalone = true;
		model.addAttribute("standalone" , standalone);
		return "signup";
	}
	//This handler for registering user
	/**
	 * @param user
	 * @param agreement
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/do_register" , method=RequestMethod.POST)
	public String registeruser(@ModelAttribute("user") User user , 
			@RequestParam(value = "agreement" , defaultValue = "false") boolean agreement , 
			Model model ,HttpSession session) {
		try {
			if(!agreement) {
				throw new Exception("You have not agreed terms and conditions.");
			}
			List<User> user1 = userDAO.findAllByMobile(user.getMobile());
			List<User> user2 = userDAO.findAllByName(user.getName());
			List<User> user3 = userDAO.findAllByAdhar(user.getAdhar());
			if(user1.size() > 1) {
				throw new Exception("User already exists with same Mobile Number!");
			}
			if(user2.size() > 1) {
				throw new Exception("User already exists with same Name!");
			}
			if(user3.size() > 1) {
				throw new Exception("User already exists with same Adhar!");
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setDOJ(new Date());
			User result = this.userDAO.save(user);
			model.addAttribute("user" , new User());
			model.addAttribute("message", new Message("Successfully registered" , "alert-success"));
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user" , user);
			model.addAttribute("message", new Message("Something went wrong !! "+e.getMessage() , "alert-danger"));
		}
		if(standalone) {
			model.addAttribute("standalone" , standalone);
		}
		return "signup";
	}

//handler for custom login
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title","Login Page");
		return "login";
	}
	
	@GetMapping("/forgot-password")
	public String forgotPass(Model model) {
		model.addAttribute("title","Forgot Password");
		otpsent = false;
		otpverified  = false;
		
		model.addAttribute("otpsent" , otpsent);
		model.addAttribute("otpverified" , otpverified);
		model.addAttribute("form_dest" , "/forgot-password/submitvalue");
		return "forgot-password";
	}
	
	@GetMapping("/forgot-password/submitvalue")
	public String forgotPass1(@RequestParam("mobile") String mobile, Model model , HttpSession session) {
		User user = userDAO.findByMobile(mobile);
		if(user != null) {
			temp = user;
			otpVar = generateOTP();
			SmsUtility.sendSms(mobile, "Hello " + user.getName() + 
					", Your OTP for your password reset request is " + otpVar);
			session.setAttribute("message", new Message("OTP sent successfully" , "alert-success"));
			otpsent = true;
			otpverified  = false;
			model.addAttribute("otpsent" , otpsent);
			model.addAttribute("otpverified" , otpverified);
			model.addAttribute("form_dest" , "/forgot-password/submit-otp-reset");
			System.out.println("otp-> " + otpVar);
		}
		else {
			System.out.println("Nope");
			session.setAttribute("message", new Message("User not found" , "alert-danger"));
			otpsent = false;
			otpverified  = false;
			model.addAttribute("otpsent" , otpsent);
			model.addAttribute("otpverified" , otpverified);
			model.addAttribute("form_dest" , "/forgot-password/submitvalue");
		}
		
		return "forgot-password";
	}
	
	@GetMapping("/forgot-password/submit-otp-reset")
	public String forgotOTPSubmit(@RequestParam("otp") String otp ,  Model model , HttpSession session) {
		if(otpVar.equals(otp)) {
			otpsent=true;
			otpverified=true;
			model.addAttribute("otpsent" , otpsent);
			model.addAttribute("otpverified" , otpverified);
			model.addAttribute("form_dest" , "/forgot-password/reset");
			session.setAttribute("message", new Message("OTP Verified successfully" , "alert-success"));
		}
		else {
			session.setAttribute("message", new Message("OTP didn't match" , "alert-danger"));
			otpsent=true;
			otpverified=false;
			model.addAttribute("otpsent" , otpsent);
			model.addAttribute("otpverified" , otpverified);
			model.addAttribute("form_dest" , "/forgot-password/submit-otp-reset");
		}
		
		return "forgot-password";
	}
	
	@GetMapping("/forgot-password/reset")
	public String forgotPassword1(@RequestParam("password") String password , Model model , HttpSession session) {
		System.out.println("otp-> " + otpsent + " " + otpverified);
		if(otpverified && otpsent) {
			System.out.println("password set");
			temp.setPassword(passwordEncoder.encode(password));
			session.setAttribute("message", new Message("Password Reset Successfully!" , "alert-success"));
		}
		else {
			System.out.println("password not set");
			session.setAttribute("message", new Message("Something went wrong, Please try again!" , "alert-danger"));
			
		}
		otpsent = false;
		otpverified  = false;
		
		model.addAttribute("otpsent" , otpsent);
		model.addAttribute("otpverified" , otpverified);
		model.addAttribute("form_dest" , "/forgot-password/submitvalue");
		return "redirect:/signin";
	}
	
	
	private String generateOTP() {
		String numbers = "0123456789";
		int len = 4; //otp length
		char[] otp = new char[len];
		Random rand = new Random();
		for(int i=0; i<len ; i++) {
			otp[i] = numbers.charAt(rand.nextInt(numbers.length()));
		}
		return String.valueOf(otp);
	}
	
}
