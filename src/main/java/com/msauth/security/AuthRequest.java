package com.msauth.security;

import java.io.Serializable;

public class AuthRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	
	public AuthRequest() {
		super();
	}

	public AuthRequest(String email, String password) {
		super();
		this.email = email;
		this.password = password;
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
	
	
	
	
}