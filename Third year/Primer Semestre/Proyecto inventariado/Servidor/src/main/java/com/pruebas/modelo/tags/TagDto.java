package com.pruebas.modelo.tags;

import java.util.UUID;

public class TagDto {
	private UUID id;
	private String nombre;

	public TagDto(UUID id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public UUID getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}
}
