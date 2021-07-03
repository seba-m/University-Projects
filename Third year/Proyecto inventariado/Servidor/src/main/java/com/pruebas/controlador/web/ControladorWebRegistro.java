package com.pruebas.controlador.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pruebas.modelo.email.EmailService;
import com.pruebas.modelo.usuario.UserRegistrationDto;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
@RequestMapping("/registro")
public class ControladorWebRegistro {

	private UserServiceImpl userService;

	@Autowired
	private EmailService emailService;

	@Value("${app.webServerUrl}")
	private String webServerUrl;

	public ControladorWebRegistro(UserServiceImpl userService) {
		super();
		this.userService = userService;
	}

	@ModelAttribute("Usuario")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping
	public String showRegistrationForm() {
		return "registro";
	}

	@PostMapping
	@Async
	public String registerUserAccount(@ModelAttribute("Usuario") UserRegistrationDto registrationDto) {
		try {

			if (registrationDto == null || registrationDto.hasEmptyField()) {
				return "redirect:/registro?fail";
			}

			try {
				userService.loadUserByUsername(registrationDto.getEmail());
				return "redirect:/registro?fail=UserAlreadyExists";
			} catch (UsernameNotFoundException e) {
				Usuario usuario = userService.save(registrationDto);

				emailService.sendEmail(usuario.getEmail(), "Registration Confirmation",
						"To confirm your e-mail address, please click the link below:\n" + webServerUrl
								+ "/confirm?token=" + usuario.getToken());

				return "redirect:/registro?success";
			}
		} catch (MailException e) {
			e.printStackTrace();
			return "redirect:/registro?fail=FailOnSendEmail";
		} catch (Exception e) {
			return "redirect:/registro?fail";
		}
	}
}