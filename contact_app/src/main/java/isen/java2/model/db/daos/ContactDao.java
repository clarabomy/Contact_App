package isen.java2.model.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

import java.sql.Date;

public class ContactDao {
	
	public int existContact(Contact contact) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "SELECT contact.id FROM contact JOIN category ON contact.id_category = category.id WHERE LOWER(lastname) = ? AND LOWER(firstname) = ?";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
				statement.setString(1, contact.getLastname().toLowerCase());
				statement.setString(2, contact.getFirstname().toLowerCase());
				
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						return results.getInt("id");
					}
				}
			}
			
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	public Contact addContact (Contact contact) {
		
		if (existContact(contact) != 0) {
			return null;
		}
		
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
				contactStmt.setDate(8, contact.getBirthdate()!=null? Date.valueOf(contact.getBirthdate()) : null);
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
	
	public void updateContact(Contact contact) {
		
		if (existContact(contact) != contact.getId()) {
			return;
		}
		
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "UPDATE contact SET lastname=?, firstname=?, nickname=?, phone=?, id_category=?, email=?, address=?, birthday=?, notes=? WHERE id=?";
			try (PreparedStatement contactStmt = connection.prepareStatement(sqlQuery)) {
				contactStmt.setString(1, contact.getLastname());
				contactStmt.setString(2, contact.getFirstname());
				contactStmt.setString(3, contact.getNickname());
				contactStmt.setString(4, contact.getPhone());
				contactStmt.setInt(5, contact.getCategory().getId());
				contactStmt.setString(6, contact.getMail());
				contactStmt.setString(7, contact.getAddress());
				contactStmt.setDate(8, contact.getBirthdate()!=null? Date.valueOf(contact.getBirthdate()) : null);
				contactStmt.setString(9,  contact.getNotes());
				contactStmt.setInt(10,  contact.getId());
				contactStmt.executeUpdate();
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<Contact> searchContact(String search) {
		List<Contact> resultSearch = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "SELECT * FROM contact JOIN category ON contact.id_category = category.id WHERE LOWER(lastname) LIKE ? OR LOWER(firstname) LIKE ? OR LOWER(nickname) LIKE ? OR LOWER(phone) LIKE ? OR LOWER(category.name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(address) LIKE ? OR birthday LIKE ? OR LOWER(notes) LIKE ?";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
				for (int i = 1; i < 10; i++) {
					statement.setString(i, "%" + search + "%");
				}
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						Contact contact = new Contact(results.getInt("id"), 
								results.getString("lastname"),
								results.getString("firstname"), 
								results.getString("nickname"), 
								results.getString("address"),
								(results.getDate("birthday") == null)? null : results.getDate("birthday").toLocalDate(), 
								new Category(results.getInt("id_category"), results.getString("name")),
								results.getString("email"), results.getString("phone"),
								results.getString("notes"));
						
						resultSearch.add(contact);
					}
				}
			}
			
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultSearch;
	}
	
	public void deleteContact(Integer idContact) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE id = ?")) {
				statement.setInt(1, idContact);
				statement.executeUpdate();
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}			
	}

	public List<Contact> listAllContacts() {
		List<Contact> listOfContacts = new ArrayList<>();
		
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "SELECT * FROM contact LEFT JOIN category ON contact.id_category = category.id ORDER BY lastname";
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results =
						statement.executeQuery(sqlQuery)) {
					while (results.next()) {
						Contact contact = new Contact(results.getInt("contact.id"), 
								results.getString("lastname"),
								results.getString("firstname"), 
								results.getString("nickname"), 
								results.getString("address"),
								(results.getDate("birthday") == null)? null : results.getDate("birthday").toLocalDate(), 
								new Category(results.getInt("category.id"), results.getString("name")),
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
			String sqlQuery = "SELECT * FROM contact LEFT JOIN category ON contact.id_category = category.id WHERE category.name = ? ORDER BY lastname";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {			
				statement.setString(1, category);
				
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						Contact contact = new Contact(results.getInt("contact.id"), 
								results.getString("lastname"),
								results.getString("firstname"), 
								results.getString("nickname"), 
								results.getString("address"),
								(results.getDate("birthday") == null)? null : results.getDate("birthday").toLocalDate(), 
								new Category(results.getInt("category.id"), results.getString("name")),
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
