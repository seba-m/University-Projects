package com.pruebas.modelo.usuario;

import org.apache.commons.lang3.StringUtils;

public class UserUpdatingDto {
	private String firstName;
	private String lastName;
	private String email;
	private String passwordOriginal;
	private String passwordChanged;
	private String passwordChangedRepeated;

	public UserUpdatingDto() {
		// esta vacio, dado a que se le asigna mediante getters y setters los valores.
	}

	public boolean isNewPasswordMatches() {
		return passwordChanged.equals(passwordChangedRepeated);
	}

	public String getPasswordChanged() {
		return passwordChanged;
	}

	public void setPasswordChanged(String passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	public String getPasswordChangedRepeated() {
		return passwordChangedRepeated;
	}

	public void setPasswordChangedRepeated(String passwordChangedRepeated) {
		this.passwordChangedRepeated = passwordChangedRepeated;
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

	public String getPasswordOriginal() {
		return passwordOriginal;
	}

	public void setPasswordOriginal(String passwordOriginal) {
		this.passwordOriginal = passwordOriginal;
	}

	boolean isValidEmail(String email) {
		return !getEmail().equals(email);
	}

	public boolean isEmptyEmail() {
		return StringUtils.isBlank(getEmail());
	}

	boolean isValidFirstName(String firstName) {
		return !isEmptyFirstName() && !getFirstName().equals(firstName);
	}

	public boolean isEmptyFirstName() {
		return StringUtils.isBlank(getFirstName());
	}

	boolean isValidLastName(String lastName) {
		return !isEmptyLastName() && !getLastName().equals(lastName);
	}

	public boolean isEmptyLastName() {
		return StringUtils.isBlank(getLastName());
	}

	public boolean isValidNewPassword() {
		return getPasswordChanged().equals(getPasswordChangedRepeated());
	}

	public boolean isEmptyPasswords() {
		return StringUtils.isBlank(getPasswordOriginal()) || StringUtils.isBlank(getPasswordChanged())
				|| StringUtils.isBlank(getPasswordChangedRepeated());
	}

	@Override
	public String toString() {
		return "LoginResponse{" + " Email='" + getEmail() + '\'' + ", FirstName='" + getFirstName() + '\''
				+ ", LastName='" + getLastName() + '\'' + ", Password='" + getPasswordOriginal() + '\''
				+ ", Password changed='" + getPasswordChanged() + '\'' + ", Password changed repeated='"
				+ getPasswordChangedRepeated() + '\'' + '}';
	}
}