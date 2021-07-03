package com.pruebas.modelo.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, UUID> {
	Usuario findByEmail(String email);

	Optional<Usuario> findById(UUID id);

	Usuario findByTokenAccountCreation(String token);

	Optional<Usuario> findByTokenPassword(TokenPassword token);
}