package isen.java2.model.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.CategoryType;
import isen.java2.model.db.entities.Contact;

import java.sql.Date;

public class ContactDao {

	public Contact addContact (Contact contact) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			
			String sqlQuery = "INSERT INTO contact(lastname, firstname, nickname, phone, id_category, email, address, birthday, notes) VALUES(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement contactStmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				contactStmt.setString(1, contact.getLastname());
				contactStmt.setString(2, contact.getFirstname());
				contactStmt.setString(3, contact.getNickname());
				contactStmt.setString(4, contact.getPhone());
				contactStmt.setInt(5, contact.getCategory().getId());
				contactStmt.setString(6, contact.getMail());
				contactStmt.setString(7, contact.getAddress());
				contactStmt.setDate(8, Date.valueOf(contact.getBirthdate()));
				contactStmt.setString(9,  contact.getNotes());
				contactStmt.executeUpdate();
				
				try (ResultSet keys = contactStmt.getGeneratedKeys()) {
					keys.next();
					contact.setId(keys.getInt(1));
					return contact;	
				}	
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Contact> listAllContacts() {
		List<Contact> listOfContacts = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (Statement stmt = connection.createStatement()) {
				try (ResultSet results = stmt
						.executeQuery("SELECT * FROM contact JOIN category ON contact.category_id = category.id_category ORDER BY contact.lastname")) {
					
					while (results.next()) {
						Contact contact = new Contact(results.getInt("id_contact"), 
								results.getString("lastname"),
								results.getString("firstname"), 
								results.getString("nickname"), 
								results.getString("address"),
								results.getDate("birthday").toLocalDate(), 
								new CategoryType(results.getInt("id_category"), results.getString("type_category")),
								results.getString("email"), results.getString("phone"),
								results.getString("notes"));
						
						listOfContacts.add(contact);
					}
				}
			}
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listOfContacts;
	}
	
	public List<Contact> listContactsByCategory(String category) {
		List<Contact> listOfFilteredContacts = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM contact JOIN category ON contact.id_category = category.id WHERE category.name famille ORDER BY contact.lastname")) {
				statement.setString(1, category);
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						Contact contact = new Contact(results.getInt("id_contact"), 
								results.getString("lastname"),
								results.getString("firstname"), 
								results.getString("nickname"), 
								results.getString("address"),
								results.getDate("birthday").toLocalDate(), 
								new CategoryType(results.getInt("id_category"), results.getString("type_category")),
								results.getString("email"), results.getString("phone"),
								results.getString("notes"));
						
						listOfFilteredContacts.add(contact);
					}
				}
			}
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listOfFilteredContacts;
	}
	
}
