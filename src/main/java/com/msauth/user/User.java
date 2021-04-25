package com.msauth.user;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
    private String name;
    private String email;
    private String password;
    private Boolean enable;
    private String role;
    private String imageUrl;
    
	public User() {
	}
	
	public User(String id, String name, String email, String password, Boolean enable, String role, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.enable = enable;
		this.role = role;
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
