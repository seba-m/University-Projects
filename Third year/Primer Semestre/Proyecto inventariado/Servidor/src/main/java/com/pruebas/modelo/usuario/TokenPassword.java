package com.pruebas.modelo.usuario;

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "user_pwd_token")
public class TokenPassword {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	@Type(type = "uuid-char")
	private UUID id;

	@Column(name = "token", columnDefinition = "VARCHAR(255)")
	private String token;

	@Column(name = "date_invalid")
	private Date expireDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tokenPassword")
	private Usuario usuario;

	public TokenPassword() {
	}

	public TokenPassword(String token, Date expDate) {
		this.token = token;
		this.expireDate = expDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isValidToken() {
		Long tiempoActual = new Date().getTime();
		return tiempoActual <= getExpireDate().getTime();
	}

}
