package com.smart.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
    private String name;
    @Column(unique = true)
    private String email;
	@Size(min=10, max=10, message = "Phone no is supposed to have 10 digits")
	@Pattern(regexp = "\\d+", message = "Can only contain digits")
	@Column(unique = true)
    private String phone;
	private String countryCode;
	private String image;
    @ManyToOne
    private User user;
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(int cid, String name, String email, String phone, String countryCode, String image, User user) {
		this.cid = cid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.countryCode = countryCode;
		this.image = image;
		this.user = user;
	}


	@Override
	public String toString() {
		return "Contact{" +
				"cid=" + cid +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", countryCode='" + countryCode + '\'' +
				", image='" + image + '\'' +
				", user=" + user +
				'}';
	}

	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
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
	public String getPhone() {
		return phone;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
