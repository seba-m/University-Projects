package com.pruebas.modelo.configuraciones;

import java.util.Date;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.pruebas.modelo.usuario.UserRepository;
import com.pruebas.modelo.usuario.Usuario;

@Service
public class UserService implements ApplicationListener<AuthenticationSuccessEvent> {

	private UserRepository userRepository;

	UserService(UserRepository repository) {
		this.userRepository = repository;
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		String userName = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
		Usuario user = userRepository.findByEmail(userName);
		user.setLastLogin(new Date());
		userRepository.save(user);
	}
}
