package com.pruebas.modelo.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.servermail}")
	private String serverEmail;

	@Value("${app.webServerUrl}")
	private String webServerUrl;

	@Async
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setFrom(serverEmail);
		registrationEmail.setTo(to);
		registrationEmail.setSubject(subject);
		registrationEmail.setText(text);

		mailSender.send(registrationEmail);
	}
}
