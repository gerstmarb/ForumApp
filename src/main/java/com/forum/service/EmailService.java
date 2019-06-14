package com.forum.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;
	
	@Value("${spring.webservices.path}")
	private String ACTIVATION_URL;
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void sendMessage(String email, String username, String code) {
		SimpleMailMessage message = null;
		
		try {
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Registration successful");
			message.setText("Dear " + username + "! \n \n Thank you for your sign up! \\n "
					+ "Your activation link: " + ACTIVATION_URL + code);
			javaMailSender.send(message);
			
		} catch (Exception e) {
			log.error("something wrong with email sending to: " + email + e);
		}
	}
}
