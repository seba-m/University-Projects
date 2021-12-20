package com.pruebas.controlador.web;

import java.util.Map;
import java.util.Optional;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pruebas.modelo.email.EmailService;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;

@Controller
public class ControladorWebInicioSesion {

	@Autowired
	private EmailService emailService;

	@Autowired
	private final UserServiceImpl repository;

	@Value("${app.webServerUrl}")
	private String webServerUrl;

	@Value("${app.servermail}")
	private String serverMail;

	ControladorWebInicioSesion(UserServiceImpl repository) {
		this.repository = repository;
	}

	@GetMapping("/login")
	public String loguear(Model model) {

		if (isAuthenticated().getValue0() && isAuthenticated().getValue1().contains("USER")) {
			return "redirect:app";
		} else if (isAuthenticated().getValue0() && isAuthenticated().getValue1().contains("ADMIN")) {
			return "redirect:admin";
		} /*- else if (isAuthenticated().getValue0() && isAuthenticated().getValue1().contains("CANCELED")) {
			return "redirect:/login?banned";
		}*/

		return "login";
	}

	@GetMapping("/forgot")
	public String forgot(Model model) {
		return "forgot";
	}

	@PostMapping("/forgot")
	@Async
	public String forgotPost(@RequestParam("email") String email) {

		try {
			Usuario resetUser = repository.findUserByEmail(email);

			if (resetUser != null && resetUser.getResetToken() == null) {

				resetUser = repository.resetUser(email);

				emailService.sendEmail(resetUser.getEmail(), "Password Reset Request",
						"To reset your password, click the link below:\n" + webServerUrl + "/reset?token="
								+ resetUser.getResetToken().getToken());

				return "redirect:/forgot?success";
			} else if (resetUser != null && resetUser.getResetToken() != null
					&& resetUser.getResetToken().isValidToken()) {
				return "redirect:/forgot?CheckEmail";
			} else if (resetUser != null && resetUser.getResetToken() != null
					&& !resetUser.getResetToken().isValidToken()) {
				repository.removeExpiredToken(resetUser);
				return "redirect:/login";
			} else {
				return "redirect:/login";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/forgot?fail&error=NoAccount";
		}
	}

	@GetMapping("/reset")
	public String reset(@RequestParam("token") String token, Model model) {
		Optional<Usuario> user = repository.findByResetToken(token);
		if (user.isPresent()) {
			if (user.get().getResetToken().isValidToken()) {
				model.addAttribute("token", token);
				return "reset";
			} else {
				repository.removeExpiredToken(user.get());
				return "redirect:/login";
			}
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/reset")
	public String resetPost(@RequestParam Map<String, String> reqParams) {

		Optional<Usuario> resetUser = repository.findByResetToken(reqParams.get("token"));

		if (resetUser.isPresent()) {

			repository.changeUserPassword(resetUser.get(), reqParams.get("password"));

			return "redirect:/login?successChange";
		}

		return "redirect:/login";
	}

	@GetMapping("/confirm")
	public String confirm(@RequestParam("token") String token) {
		try {
			repository.confirmUser(token);
			return "redirect:/login?success";
		} catch (Exception e) {
			return "redirect:/login?fail&" + e.getMessage().trim();
		}
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParams(MissingServletRequestParameterException ex) {
		return "redirect:/login";
	}

	private Pair<Boolean, String> isAuthenticated() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return new Pair<>(false, null);
		}

		return new Pair<>(authentication.isAuthenticated(), authentication.getAuthorities().toString());
	}

}
