package com.pruebas.modelo.usuario;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<TokenPassword, UUID> {

	Optional<TokenPassword> findByToken(String token);

}