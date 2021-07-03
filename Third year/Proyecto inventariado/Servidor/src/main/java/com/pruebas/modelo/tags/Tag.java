package com.pruebas.modelo.tags;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
public class Tag implements Serializable {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	@Type(type = "uuid-char")
	private UUID id;
	@Column(name = "ownerid", columnDefinition = "BINARY(16)")
	private UUID ownerID;
	private String nombre;

	public Tag() {
	}

	public Tag(String nombre, UUID ownerUuid) {
		this.nombre = nombre;
		this.ownerID = ownerUuid;
	}

	public Tag(String nombre, UUID id, UUID ownerUuid) {
		this.id = id;
		this.nombre = nombre;
		this.ownerID = ownerUuid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(UUID ownerID) {
		this.ownerID = ownerID;
	}
}