package com.pruebas.modelo.fotos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioFotos extends JpaRepository<Foto, Long> {
	Foto findByTitulo(String titulo);
}
