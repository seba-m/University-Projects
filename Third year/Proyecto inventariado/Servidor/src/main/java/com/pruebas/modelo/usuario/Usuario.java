package com.pruebas.modelo.usuario;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import com.pruebas.modelo.fotos.Foto;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)", nullable = false, updatable = false)
	@Type(type = "uuid-char")
	private UUID id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "token_account_creation")
	private String tokenAccountCreation;

	@Column(name = "is_enabled")
	private boolean isEnabled;

	@Column(name = "created_on")
	@CreatedDate
	private Date createdOn;

	@Column(name = "last_login")
	private Date lastLogin;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reset_id", nullable = true)
	private TokenPassword tokenPassword;

	@OneToOne
	private Foto photo;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Rol> roles;

	public Usuario() {

	}

	Usuario(String firstName, String lastName, String email, String password, Foto photo, boolean isEnabled,
			Collection<Rol> roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.photo = photo;
		this.isEnabled = isEnabled;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return tokenAccountCreation;
	}

	public void setToken(String token) {
		this.tokenAccountCreation = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
	}

	public Foto getPhoto() {
		return photo;
	}

	public void setPhoto(Foto photo) {
		this.photo = photo;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public TokenPassword getResetToken() {
		return tokenPassword;
	}

	public void setResetToken(TokenPassword resetToken) {
		this.tokenPassword = resetToken;
	}

	public boolean isEmptyLastName() {
		return StringUtils.isBlank(getLastName());
	}

	public boolean isEmptyFirstName() {
		return StringUtils.isBlank(getLastName());
	}

	public boolean isEmptyEmail() {
		return StringUtils.isBlank(getEmail());
	}

	public boolean isEmptyPasswords() {
		return StringUtils.isBlank(getPassword());
	}

	public boolean isEmptyPhoto() {
		return getPhoto() == null;
	}

	public boolean isDefault() {
		return getPhoto().getTitulo().equals("avatardefault.png")
				|| getPhoto().getNombreImagen().equals("avatardefault.png");
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.firstName, this.lastName, this.email, this.password, this.photo);
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;

		if (!(o instanceof Usuario))
			return false;

		Usuario usuario = (Usuario) o;
		return Objects.equals(this.id, usuario.getId()) && Objects.equals(this.firstName, usuario.firstName)
				&& Objects.equals(this.lastName, usuario.lastName) && Objects.equals(this.email, usuario.email)
				&& Objects.equals(this.password, usuario.password) && Objects.equals(this.photo, usuario.photo);
	}
}
