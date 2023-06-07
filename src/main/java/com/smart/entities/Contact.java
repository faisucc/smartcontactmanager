package com.smart.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name="CONTACT")
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    private String image;
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Contact(int cid, String name, String email, String phone, String image) {
		super();
		this.cid = cid;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.image = image;
	}
    @Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", email=" + email + ", phone=" + phone + ", image=" + image
				+ "]";
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
