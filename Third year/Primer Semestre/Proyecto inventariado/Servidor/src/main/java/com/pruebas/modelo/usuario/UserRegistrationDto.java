package com.pruebas.modelo.usuario;

import org.apache.commons.lang3.StringUtils;

public class UserRegistrationDto {
	private String firstName;
	private String lastName;
	private String email;
	private String password;

	public UserRegistrationDto() {
		// esta vacio, dado a que se le asigna mediante getters y setters los valores.
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean hasEmptyField() {
		return StringUtils.isBlank(getEmail()) || StringUtils.isBlank(getFirstName())
				|| StringUtils.isBlank(getLastName()) || StringUtils.isBlank(getPassword());
	}
}