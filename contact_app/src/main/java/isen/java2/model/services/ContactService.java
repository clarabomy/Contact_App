package isen.java2.model.services;

import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.Contact;

public class ContactService {

	private List<Contact> contacts;
	
	private ContactService() {
		contacts = new ArrayList<>();
	}
	
	public List<Contact> getContacts() {
		return contacts;
	}

	private static class ContactServiceHolder {
		private static final ContactService INSTANCE = new ContactService();
	}

	public static ContactService getInstance() {
		return ContactServiceHolder.INSTANCE;
	}

}
