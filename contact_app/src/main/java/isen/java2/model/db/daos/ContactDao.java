package isen.java2.model.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

import java.sql.Date;

/**
 * @author Clara Bomy
 *
 *         this is our Contact Data Access Class just as a reminder, this is it"s
 *         structure ; contact(lastname,firstname,nickname,address,birthdate, category, mail, phone, notes)
 */
public class ContactDao {
	
	
	/**
	 * Checks if a contact already exists in the DB
	 * 
	 * @param contact
	 * @return the id of the corresponding contact (or 0 if the contact doesn't exist)
	 * 
	 */
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
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		
		return 0;
	}

	/**
	 * Method Used to save a contact in the DB
	 * 
	 * @param contact
	 * @return the contact that was persisted, with it's ID
	 * 
	 */
	public Contact addContact (Contact contact) {
		
		//We check if the contact is already in the DB (we can't add the same contact with the same lastname, firstname or ID, twice)
		if (existContact(contact) != 0) {
			return null;
		}
		
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {			
			String sqlQuery = "INSERT INTO contact(lastname, firstname, nickname, phone, id_category, email, address, birthday, notes) VALUES(?,?,?,?,?,?,?,?,?)";
			
			// Here we pass an option to tell the DB that we want to get the generated keys back
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
				
				// Grab the key and set it in our object
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
	
	/**
	 * Method Used to modify a contact in the DB
	 * 
	 * @param contact
	 * 
	 */
	public void updateContact(Contact contact) {
		
		//We check if the contact is already in the DB (we can't add the same contact with the same lastname, firstname or ID, twice)
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
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		
	}
	
	/**
	 * Method used to search a contact in the DB
	 * 
	 * @param search
	 * @return the list of contact found in the research
	 * 
	 */
	public List<Contact> searchContact(String search) {
		List<Contact> resultSearch = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			
			//The research is made in all the data of the DB
			String sqlQuery = "SELECT * FROM contact JOIN category ON contact.id_category = category.id WHERE LOWER(lastname) LIKE ? OR LOWER(firstname) LIKE ? OR LOWER(nickname) LIKE ? OR LOWER(phone) LIKE ? OR LOWER(category.name) LIKE ? OR LOWER(email) LIKE ? OR LOWER(address) LIKE ? OR birthday LIKE ? OR LOWER(notes) LIKE ?";
			
			//We use a prepared statement 
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
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		
		return resultSearch;
	}
	
	/**
	 * Method used to delete a contact in the DB
	 *
	 * @param idContact
	 */
	public void deleteContact(Integer idContact) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("DELETE FROM contact WHERE id = ?")) {
				statement.setInt(1, idContact);
				statement.executeUpdate();
			}
		}
		
		catch (SQLException e) {
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}			
	}

	/**
	 * Method used to list all contact 
	 *
	 * @return the list of all contacts in the database
	 */
	public List<Contact> listAllContacts() {
		List<Contact> listOfContacts = new ArrayList<>();
		
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			
			// The query does the join between the contact and the category. 
			String sqlQuery = "SELECT * FROM contact LEFT JOIN category ON contact.id_category = category.id ORDER BY lastname";
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results =
						statement.executeQuery(sqlQuery)) {
					
					// we instanciate two objects from our Resultset line : a contact and a category
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
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		
		return listOfContacts;
	}
	
	
	/**
	 * 	 Method used to list all contact with a given category
     *
	 * @param category
	 * @return a list of contacts off the corresponding genre
	 * 
	 */
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
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		
		return listOfFilteredContacts;
	}
	
}
