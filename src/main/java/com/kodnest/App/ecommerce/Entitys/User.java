package com.kodnest.App.ecommerce.Entitys;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer userId;
	@Column(nullable = false, unique = true)
	String username;
	@Column(nullable = false, unique = true)
	String email;
	@Column(nullable = false)
	String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	Role role;
	@Column
	private LocalDateTime crearedAt = LocalDateTime.now();
	@Column
	private LocalDateTime updatedAt = LocalDateTime.now();
	
	
	public User(Integer userId, String username, String email, String password,
			Role role, LocalDateTime crearedAt, LocalDateTime updatedAt) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.crearedAt = crearedAt;
		this.updatedAt = updatedAt;
	}


	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
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


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public LocalDateTime getCrearedAt() {
		return crearedAt;
	}


	public void setCrearedAt(LocalDateTime crearedAt) {
		this.crearedAt = crearedAt;
	}


	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	
}
