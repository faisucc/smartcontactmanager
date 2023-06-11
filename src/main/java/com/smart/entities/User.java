package com.smart.entities;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.*;
@Entity
@Table(name="USER")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Size(max=30, message="Maximum of 30 characters are only allowed.")
	private String name;
	@Column(unique = true)
	private String email;
	@Size(min=8, message = "Minimum 8 characters required")
	@Pattern(regexp = "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "Your password must contain an uppercase letter, a lowercase letter, a number and a special character.")
	private String password;
	private String imageURL;
	private String role;
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", imageURL="
				+ imageURL + ", role=" + role + "]";
	}
	
}
