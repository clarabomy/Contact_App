package isen.java2.model.db.entities;

import java.time.LocalDate;

/**
 * @author Clara Bomy
 *
 *         To respect the DAO model, we use this class. It allows us to abstract the technical
 *         access to data for the rest of the application.
 *         
 *         In the database, there is a table called contact.
 *         In the java code, a contact is an object of the Contact class.
 */
/**
 * @author ISEN
 *
 */
public class Contact {

	protected int id; //id of the contact
	protected String lastname; 
	protected String firstname; 
	protected String nickname; //optional
	protected String address; //optional
	protected LocalDate birthdate; //optional
	protected Category category; //a contact have a category (the contact has the cetgory "Sans catégorie" by default)
	protected String mail; //optional
	protected String phone;
	protected String notes; //optional

	/**
	 * Constructor of the class Contact
	 * 
	 * @param lastname
	 * @param firstname
	 * @param category
	 * @param phone
	 */
	public Contact(String lastname, String firstname, Category category, String phone) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.category = category;
		this.phone = phone;
		
		this.nickname = null;
		this.address = null;
		this.birthdate = null;
		this.mail = null;
		this.notes = null;
		this.id = -1;
	}
	
	/**
	 * Constructor of the class Contact
	 * 
	 * @param id
	 * @param lastname
	 * @param firstname
	 * @param category
	 * @param phone
	 */
	public Contact(int id, String lastname, String firstname, Category category, String phone) {
		this(lastname, firstname, category, phone);
		this.id = id;
	}
	
	/**
	 * Constructor of the class Contact
	 * 
	 * @param lastname
	 * @param firstname
	 * @param nickname
	 * @param address
	 * @param birthdate
	 * @param category
	 * @param mail
	 * @param phone
	 * @param notes
	 */
	public Contact(String lastname, String firstname, String nickname, String address, LocalDate birthdate,
			Category category, String mail, String phone, String notes) {
		this(lastname, firstname, category, phone);
		
		this.nickname = nickname;
		this.address = address;
		this.birthdate = birthdate;
		this.mail = mail;
		this.notes = notes;
	}
	
	/**
	 * Constructor of the class Contact
	 *
	 * @param id
	 * @param lastname
	 * @param firstname
	 * @param nickname
	 * @param address
	 * @param birthdate
	 * @param category
	 * @param mail
	 * @param phone
	 * @param notes
	 */
	public Contact(int id, String lastname, String firstname, String nickname, String address, LocalDate birthdate,
			Category category, String mail, String phone, String notes) {
		this(lastname, firstname, nickname, address, birthdate, category, mail, phone, notes);
		
		this.id = id;
	}
	
	
	/**
	 * @return the id of the contact
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the lastname of the contact
	 */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * @return the firstname of the contact
	 */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * @return the nickname of the contact
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * @param nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * @return the address of the contact
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the birthday of the contact
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}
	
	/**
	 * @param birthDate
	 */
	public void setBirthdate(LocalDate birthDate) {
		this.birthdate = birthDate;
	}

	/**
	 * @return the category of the contact
	 */
	public Category getCategory() {
		return category;
	}
	
	/**
	 * @param category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	
	/**
	 * @return the mail of the contact
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the phone of the contact
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the notes written about the contact
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
