package com.msauth.security;

import java.io.Serializable;

public class TokenDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String type;
	private String token;
	
	public TokenDTO() {
		super();
	}

	public TokenDTO(String type, String token) {
		super();
		this.type = type;
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
