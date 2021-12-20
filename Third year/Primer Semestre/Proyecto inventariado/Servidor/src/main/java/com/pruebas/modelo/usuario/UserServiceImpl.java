
package com.pruebas.modelo.usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.modelo.fotos.Foto;
import com.pruebas.modelo.fotos.FotoServiceImpl;
import com.pruebas.modelo.producto.ProductoException;
import com.pruebas.modelo.tokens.JwtTokenProvider;

@Service
@EnableJpaAuditing
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private ResetTokenRepository tokenRepository;

	private FotoServiceImpl fotoRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, ResetTokenRepository tokenRepository,
			FotoServiceImpl fotoRepository) {
		super();
		this.fotoRepository = fotoRepository;
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Usuario save(UserRegistrationDto usuario) throws BadRequestException {

		if (StringUtils.isBlank(usuario.getPassword())) {
			throw new BadRequestException("Invalid password.");
		}

		if (StringUtils.isBlank(usuario.getFirstName())) {
			throw new BadRequestException("First name is required.");
		}

		if (StringUtils.isBlank(usuario.getLastName())) {
			throw new BadRequestException("Last name is required.");
		}

		if (StringUtils.isBlank(usuario.getEmail())) {
			throw new BadRequestException("Email is required.");
		}

		try {
			loadUserByUsername(usuario.getEmail());
			throw new BadRequestException("There is already user with this email!");
		} catch (Exception e) {
			Usuario usuarioCreado = new Usuario(usuario.getFirstName(), usuario.getLastName(), usuario.getEmail(),
					passwordEncoder.encode(usuario.getPassword()),
					fotoRepository.saveAndFlush(new Foto("avatardefault", "avatardefault.png", "user-photos", 0L)),
					false, Arrays.asList(new Rol("ROLE_USER")));

			usuarioCreado.setToken(UUID.randomUUID().toString());
			usuarioCreado.setCreatedOn(new Date());

			return userRepository.save(usuarioCreado);
		}
	}

	public Usuario save(Usuario usuario) {
		return userRepository.save(usuario);
	}

	private UserUpdatingDto jsonToUserUpdatingDto(String producto) {
		UserUpdatingDto productoDto = new UserUpdatingDto();

		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			productoDto = om.readValue(producto, UserUpdatingDto.class);
		} catch (Exception e) {
			System.err.println("\n\n\nerror " + e.getMessage() + "\n\n\n");
			throw new ProductoException("InvalidProduct");
		}
		return productoDto;
	}

	public String updateUserData(String updateDataJSON, MultipartFile image) {

		UserUpdatingDto updateData = (!StringUtils.isBlank(updateDataJSON)) ? jsonToUserUpdatingDto(updateDataJSON)
				: null;

		Usuario usuarioActual;
		boolean changeOcurred = false;

		try {
			usuarioActual = getCurrentUser();
		} catch (Exception e) {
			throw new BadRequestException("UnknownAccount");
		}

		if (updateData != null) {

			if (!updateData.isEmptyPasswords()) {

				if (!updateData.isValidNewPassword()) {
					throw new BadRequestException("PasswordsWontMatch");
				}

				if (!isValidAccount(usuarioActual.getEmail(), updateData.getPasswordOriginal())) {
					throw new BadRequestException("WrongPassword");
				}

				if (isValidAccount(usuarioActual.getEmail(), updateData.getPasswordOriginal())
						&& updateData.isValidNewPassword()) {
					usuarioActual.setPassword(passwordEncoder.encode(updateData.getPasswordChanged()));
					changeOcurred = true;
				}
			}

			if (!updateData.isEmptyEmail()) {
				if (updateData.isValidEmail(usuarioActual.getEmail())
						&& EmailValidator.getInstance().isValid(updateData.getEmail())
						&& userRepository.findByEmail(updateData.getEmail()) == null
						&& !updateData.getEmail().equalsIgnoreCase(usuarioActual.getEmail())) {
					usuarioActual.setEmail(updateData.getEmail());
					changeOcurred = true;
				}

				if (!EmailValidator.getInstance().isValid(updateData.getEmail())) {
					throw new BadRequestException("WrongEmail");
				}

				if (userRepository.findByEmail(updateData.getEmail()) != null
						&& !updateData.getEmail().equalsIgnoreCase(usuarioActual.getEmail())) {
					throw new BadRequestException("EmailAlreadyExists");
				}

			}

			if (updateData.isValidFirstName(usuarioActual.getFirstName())) {
				usuarioActual.setFirstName(updateData.getFirstName());
				changeOcurred = true;
			}

			if (updateData.isValidLastName(usuarioActual.getLastName())) {
				usuarioActual.setLastName(updateData.getLastName());
				changeOcurred = true;
			}
		}

		if (image != null && !StringUtils.isBlank(image.getOriginalFilename())) {
			try {
				usuarioActual.setPhoto(fotoRepository.saveUserImage(usuarioActual.getId(), image));
				changeOcurred = true;
			} catch (IOException e) {
				usuarioActual.setPhoto(new Foto("avatardefault", "avatardefault.png", "user-photos", 0L));
				throw new BadRequestException("CantSaveImage");
			}
		}

		if (!changeOcurred && usuarioActual.getPhoto().getNombreImagen().equals("avatardefault.png")) {
			return "https://inventarispro.s3.amazonaws.com/user-photos/avatardefault.png";
		} else if (!changeOcurred) {
			String linkFoto = "https://inventarispro.s3.amazonaws.com/user-photos/%1$s/%2$s";

			return String.format(linkFoto, usuarioActual.getId().toString(),
					usuarioActual.getPhoto().getNombreImagen());
		}

		boolean result = saveUpdatedUser(usuarioActual);

		if (!result) {
			throw new BadRequestException("FailSavingData");
		} else {
			String linkFoto = "https://inventarispro.s3.amazonaws.com/user-photos/%1$s/%2$s";

			return String.format(linkFoto, usuarioActual.getId().toString(),
					usuarioActual.getPhoto().getNombreImagen());
		}
	}

	public String updateUserData(UserUpdatingDto updateData, MultipartFile image) {

		Usuario usuarioActual;

		try {
			usuarioActual = getCurrentUser();
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/account?error=unknow_account";
		}
		List<String> errors = new ArrayList<>();

		boolean errorOcurred = false;
		boolean editOcurred = false;

		if (updateData != null) {

			if (!updateData.isEmptyPasswords()) {

				if (!updateData.isValidNewPassword()) {
					errors.add("password_match");
					errorOcurred = true;
				}

				if (!isValidAccount(usuarioActual.getEmail(), updateData.getPasswordOriginal())) {
					errors.add("wrong_password");
					errorOcurred = true;
				}

				if (isValidAccount(usuarioActual.getEmail(), updateData.getPasswordOriginal())
						&& updateData.isValidNewPassword()) {
					usuarioActual.setPassword(passwordEncoder.encode(updateData.getPasswordChanged()));
					editOcurred = true;
				}
			}

			if (!updateData.isEmptyEmail()) {
				if (updateData.isValidEmail(usuarioActual.getEmail())
						&& EmailValidator.getInstance().isValid(updateData.getEmail())
						&& userRepository.findByEmail(updateData.getEmail()) == null
						&& !updateData.getEmail().equalsIgnoreCase(usuarioActual.getEmail())) {
					usuarioActual.setEmail(updateData.getEmail());
					editOcurred = true;
				}

				if (!EmailValidator.getInstance().isValid(updateData.getEmail())) {
					errors.add("wrong_mail");
					errorOcurred = true;
				}

				if (userRepository.findByEmail(updateData.getEmail()) != null) {
					errors.add("already_exist");
					errorOcurred = true;
				}

			}

			if (updateData.isValidFirstName(usuarioActual.getFirstName())) {
				usuarioActual.setFirstName(updateData.getFirstName());
				editOcurred = true;
			}

			if (updateData.isValidLastName(usuarioActual.getLastName())) {
				usuarioActual.setLastName(updateData.getLastName());
				editOcurred = true;
			}
		}

		if (image != null && !StringUtils.isBlank(image.getOriginalFilename())) {
			try {
				usuarioActual.setPhoto(fotoRepository.saveUserImage(usuarioActual.getId(), image));
				editOcurred = true;
			} catch (IOException e) {
				usuarioActual.setPhoto(new Foto("avatardefault", "avatardefault.png", "user-photos", 0L));
				errors.add("save_trouble");
				errorOcurred = true;
			}
		}
		if (!errorOcurred) {
			if (editOcurred) {
				return (saveUpdatedUser(usuarioActual)) ? "redirect:/app/account?success"
						: "redirect:/app/account?error=failed_save";
			} else {
				return "redirect:/app/account";
			}
		} else {
			return "redirect:/app/account?error=" + errors.stream().map(n -> n).collect(Collectors.joining("&error="));
		}

	}

	private boolean saveUpdatedUser(Usuario usuarioActual) {
		try {
			userRepository.save(usuarioActual);

			if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(usuarioActual.getEmail())) {

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				Authentication newAuth = new UsernamePasswordAuthenticationToken(usuarioActual.getEmail(),
						usuarioActual.getPassword(), new ArrayList<>(auth.getAuthorities()));

				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Usuario resetUser(String email) {

		Usuario userExists = userRepository.findByEmail(email);

		if (userExists == null) {
			throw new BadRequestException(email + " is not registered.");
		}

		if (userExists.getEmail().isEmpty()) {
			throw new BadRequestException(email + " does not have a valid email address.");
		}

		userExists.setResetToken(
				new TokenPassword(UUID.randomUUID().toString(), new Date(new Date().getTime() + 900000)));

		userRepository.save(userExists);

		return userExists;
	}

	public void changeUserPassword(Usuario user, String password) {

		user.setPassword(passwordEncoder.encode(password));
		user.setResetToken(null);
		userRepository.save(user);

	}

	public void confirmUser(String token) {
		if (StringUtils.isEmpty(token)) {
			throw new BadRequestException("Invalid token");
		}

		Usuario user = userRepository.findByTokenAccountCreation(token);

		if (user == null) {
			throw new BadRequestException("Invalid token");
		}

		user.setEnabled(true);
		user.setToken("");

		userRepository.save(user);
	}

	public Usuario getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!(authentication == null
				|| AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass()))) {
			return userRepository.findByEmail(authentication.getName());
		} else {
			throw new UsernameNotFoundException("No user data found.");
		}
	}

	public Usuario getUserByEmail(String email) {

		Usuario user = userRepository.findByEmail(email);

		if (user == null || !user.isEnabled()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return user;
	}

	public boolean deleteAccount() {
		try {
			// obtengo el usuario actual
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			// elimino las fotos del usuario
			fotoRepository.deleteUserImage(getCurrentUser());

			// lo elimino del repositorio
			userRepository.delete(userRepository.findByEmail(authentication.getName()));

			// y lo deslogueo
			SecurityContextHolder.clearContext();

			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean isValidAccount(String email, String password) {
		try {
			return passwordEncoder.matches(password, loadUserByUsername(email).getPassword());
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario user = userRepository.findByEmail(username);
		if (user == null || !user.isEnabled()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	public UserDetails findByUserId(UUID id) throws UsernameNotFoundException {

		Optional<Usuario> user = userRepository.findById(id);
		if (!user.isPresent() || !user.get().isEnabled()) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new User(user.get().getEmail(), user.get().getPassword(), mapRolesToAuthorities(user.get().getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Rol> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	public void removeExpiredToken(Usuario user) {

		if (user != null && user.getResetToken() != null) {
			UUID tokenID = user.getResetToken().getId();
			tokenRepository.deleteById(tokenID);
			user.setResetToken(null);
			userRepository.save(user);
		}
	}

	@Override
	public Optional<Usuario> findByResetToken(String token) {

		Optional<TokenPassword> userTokenPassword = tokenRepository.findByToken(token);

		if (userTokenPassword.isPresent()) {
			return userRepository.findByTokenPassword(userTokenPassword.get());
		} else {
			return Optional.empty();
		}

	}

	@Override
	public Usuario findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}