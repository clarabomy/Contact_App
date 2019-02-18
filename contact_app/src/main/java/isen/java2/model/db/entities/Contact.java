package isen.java2.model.db.entities;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.Map;


public class Contact {

	protected short id;
	protected String lastname;
	protected String firstname;
	protected String nickname;
	protected LocalDate birthdate;
	protected CategoryType category;
	protected SortedSet <String> mail;
	protected Map <String, String> phones;
	
	
	public Contact(short id, String lastname, String firstname, String nickname, LocalDate birthdate,
			CategoryType category, SortedSet<String> mail, Map<String, String> phones) {
		super();
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.nickname = nickname;
		this.birthdate = birthdate;
		this.category = category;
		this.mail = mail;
		this.phones = phones;
	}

	public short getId() {
		return id;
	}
	
	public void setId(short id) {
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
	
	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(LocalDate birthDate) {
		this.birthdate = birthDate;
	}
	
	public CategoryType getCategory() {
		return category;
	}
	
	public void setCategory(CategoryType category) {
		this.category = category;
	}
	
	public SortedSet<String> getMail() {
		return mail;
	}
	
	public void setMail(SortedSet<String> mail) {
		this.mail = mail;
	}
	
	public Map<String, String> getPhones() {
		return phones;
	}
	
	public void setPhones(Map<String, String> phones) {
		this.phones = phones;
	}
	
	
}
