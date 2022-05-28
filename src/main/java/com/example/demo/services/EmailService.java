package com.example.demo.services;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
 
@Service 
public class EmailService 
{
    private JavaMailSenderImpl mailSender;
    
    @Autowired
    public EmailService(){
    	  mailSender = new JavaMailSenderImpl();
    	  mailSender.setUsername("praneshnangare12@gmail.com");
    	  mailSender.setPassword("elosmrsiwbtwrfqw");
    	  mailSender.setHost("smtp.gmail.com");
    	  mailSender.setDefaultEncoding("UTF-8");
    	  Properties pros = new Properties();
    	  pros.put("mail.smtp.auth", true);
    	  pros.put("mail.smtp.starttls.enable", true);
    	  pros.put("mail.smtp.timeout", 25000);
    	  pros.put("mail.smtp.port", 587);
    	  pros.put("mail.smtp.socketFactory.port", 587);
    	  pros.put("mail.smtp.socketFactory.fallback", false);
    	  mailSender.setJavaMailProperties(pros);
    	  this.mailSender = mailSender;
    }
    
    /**
     * This method will send compose and send the message 
     * */
    public void sendMail(String to, String subject, String body) 
    {
    	
    	//MimeMessage message = mailSender.createMimeMessage();
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom("praneshnangare12@gmail.com");
        mailSender.send(mailMessage);
        System.out.println("Mail sent successfully without attachment");
    }
    
    public void sendMailWithAttachment(String to, String subject, String body) 
    {
    	MimeMessage message = mailSender.createMimeMessage();
		try{
	 		MimeMessageHelper helper = new MimeMessageHelper(message, true);	
	 		helper.setFrom("praneshnangare12@gmail.com");
	 		helper.setTo(to);
	 		helper.setSubject(subject);
	 		helper.setText(body);
	 			
	 		FileSystemResource file = new FileSystemResource("C:\\Users\\p.subhash.nangare\\Downloads\\QUOT295776.pdf");
	 		helper.addAttachment(file.getFilename(), file);
	 		mailSender.send(message);
	 		System.out.println("Sent a mail with attachment");
 	     }catch (MessagingException e) {
 	    	 throw new MailParseException(e);
 	     }
 	    
    }
    
    public void sendMailWithAttachmentFromStream(ByteArrayResource bytes, String to, String subject, String body) 
    {
    	MimeMessage message = mailSender.createMimeMessage();
		try{
	 		MimeMessageHelper helper = new MimeMessageHelper(message, true);	
	 		helper.setFrom("praneshnangare12@gmail.com");
	 		helper.setTo(to);
	 		helper.setSubject(subject);
	 		helper.setText(body);
	 			
//	 		FileSystemResource file = new FileSystemResource("C:\\Users\\p.subhash.nangare\\Downloads\\QUOT295776.pdf");
	 		helper.addAttachment("hello.pdf", bytes);
	 		mailSender.send(message);
	 		System.out.println("Sent a mail with attachment");
 	     }catch (MessagingException e) {
 	    	 throw new MailParseException(e);
 	     }
 	    
    }
}
  
 

