package com.pruebas.modelo.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
	Usuario save(UserRegistrationDto registrationDto);

	UserDetails findByUserId(UUID ID);

	Usuario findUserByEmail(String email);

	Optional<Usuario> findByResetToken(String token);
}