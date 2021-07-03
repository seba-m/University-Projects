package com.pruebas.controlador.rest;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pruebas.modelo.email.EmailService;
import com.pruebas.modelo.tokens.JwtTokenProvider;
import com.pruebas.modelo.usuario.BadRequestException;
import com.pruebas.modelo.usuario.UserRegistrationDto;
import com.pruebas.modelo.usuario.UserServiceImpl;
import com.pruebas.modelo.usuario.Usuario;
import com.pruebas.modelo.usuario.UsuarioDTO;

@CrossOrigin(maxAge = 3600) // https://spring.io/guides/gs/rest-service-cors/
@RestController
@RequestMapping("/api/v1")
public class ControladorAppSesion {

	@Autowired
	private final UserServiceImpl repository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Value("${app.webServerUrl}")
	private String webServerUrl;

	ControladorAppSesion(UserServiceImpl repository) {
		this.repository = repository;
	}

	@PostMapping("/login")
	public UsuarioDTO login(@RequestHeader("Authorization") String userLoginData) {

		if (StringUtils.isBlank(userLoginData)) {
			throw new BadRequestException("InvalidData");
		}

		if (userLoginData.contains("Basic")) {
			userLoginData = userLoginData.replace("Basic", "").trim();
		}

		byte[] decodedBytes = null;

		try {
			decodedBytes = Base64.getDecoder().decode(userLoginData);
		} catch (Exception e) {
			throw new BadRequestException("InvalidData");
		}

		String decodedString = new String(decodedBytes);

		String email = decodedString.substring(0, decodedString.indexOf(":"));
		String password = decodedString.substring(decodedString.indexOf(":") + 1);

		if (repository.isValidAccount(email, password)) {
			Usuario userData = repository.getUserByEmail(email);
			String token = tokenProvider.generateToken(userData);
			String linkFoto = "";

			if (userData.getPhoto() != null && !userData.getPhoto().getNombreImagen().equals("avatardefault.png")) {
				linkFoto = "https://inventarispro.s3.amazonaws.com/user-photos/%1$s/%2$s";

				linkFoto = String.format(linkFoto, userData.getId().toString(), userData.getPhoto().getNombreImagen());
			} else {
				linkFoto = "https://inventarispro.s3.amazonaws.com/user-photos/avatardefault.png";
			}

			UsuarioDTO user = new UsuarioDTO();
			user.setId(userData.getId());
			user.setFirstName(userData.getFirstName());
			user.setLastName(userData.getLastName());
			user.setEmail(null);
			user.setPassword(null);
			user.setPhoto(linkFoto);
			user.setToken(token);
			return user;
		} else {
			throw new BadRequestException("InvalidEmailOrPassword");
		}
	}

	@PostMapping("/signup")
	public void signup(@ModelAttribute UserRegistrationDto usuarioRegistro) {

		if (usuarioRegistro == null || usuarioRegistro.hasEmptyField()) {
			throw new BadRequestException("InvalidRequest");
		}

		try {
			repository.loadUserByUsername(usuarioRegistro.getEmail());
			throw new BadRequestException("AlreadyExists");
		} catch (UsernameNotFoundException e) {
			try {
				Usuario usuario = repository.save(usuarioRegistro);

				emailService.sendEmail(usuario.getEmail(), "Registration Confirmation",
						"To confirm your e-mail address, please click the link below:\n" + webServerUrl
								+ "/confirm?token=" + usuario.getToken());
			} catch (MailException mailException) {
				e.printStackTrace();
				throw new BadRequestException("FailSendingEmail");
			} catch (Exception exception) {
				throw new BadRequestException("UnknownError");
			}
		}

	}

	@PostMapping("/forgot")
	@Async
	public void forgot(@RequestParam("email") String email) {

		try {
			Usuario resetUser = repository.findUserByEmail(email);
			if (resetUser != null && resetUser.getResetToken() == null) {

				resetUser = repository.resetUser(email);

				emailService.sendEmail(resetUser.getEmail(), "Password Reset Request",
						"To reset your password, click the link below:\n" + webServerUrl + "/reset?token="
								+ resetUser.getResetToken().getToken());

			} else if (resetUser != null && resetUser.getResetToken() != null
					&& !resetUser.getResetToken().isValidToken()) {
				repository.removeExpiredToken(resetUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/account/update")
	public Map<String, String> updateAccount(@Nullable @RequestParam("UserJson") String userJson,
			@Nullable @RequestParam("file") MultipartFile foto) {

		Map<String, String> fotoMap = new HashMap<>();
		fotoMap.put("fotoURL", repository.updateUserData(userJson, foto));
		return fotoMap;
	}

	@DeleteMapping("/account/deleteAccount")
	public void eliminarCuenta() {
		if (!repository.deleteAccount()) {
			throw new BadRequestException("CantDeleteAccount");
		}
	}

	@PostMapping("/logout")
	public void logout() {
		SecurityContextHolder.clearContext();
	}
}
