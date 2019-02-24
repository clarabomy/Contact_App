package isen.java2.model.db.entities;

import java.time.LocalDate;

public class Contact {

	protected int id;
	protected String lastname;
	protected String firstname;
	protected String nickname;
	protected String address;
	protected LocalDate birthdate;
	protected Category category;
	protected String mail;
	protected String phone;
	protected String notes;

	public Contact(int id, String lastname, String firstname, String nickname, String address, LocalDate birthdate,
			Category category, String mail, String phone, String notes) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.address = address;
		this.birthdate = birthdate;
		this.category = category;
		this.mail = mail;
		this.phone = phone;
		this.notes = notes;
	}
	
	public Contact(String lastname, String firstname, String nickname, String address, LocalDate birthdate,
			Category category, String mail, String phone, String notes) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.address = address;
		this.birthdate = birthdate;
		this.category = category;
		this.mail = mail;
		this.phone = phone;
		this.notes = notes;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(LocalDate birthDate) {
		this.birthdate = birthDate;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
