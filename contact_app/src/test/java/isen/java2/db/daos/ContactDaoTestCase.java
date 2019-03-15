package isen.java2.db.daos;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.daos.DataSourceFactory;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;


/**
 * @author Clara Bomy
 * 
 *         Test class used to test the behavior of the ContactDao Class. This
 *         class replaces the application class we used before, and becomes our
 *         entry point into the program
 */
public class ContactDaoTestCase {
	
	private ContactDao contactDao = new ContactDao();

	/**
	 * Method used to create our controlled environment, called
	 * every time a test runs
	 * 
	 * @throws Exception
	 */
	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM category");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (1,'Sans catégorie')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (2,'Amis')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (3,'Pro')");
		stmt.executeUpdate("DELETE FROM contact");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address,birthday,notes) VALUES (1,'Bomy','Clara','Clawa','0642398475',2,'clara.bomy@isen.yncrea.fr','Loos','1997-9-13','aime les IA')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address, birthday) VALUES (2,'Juzeau','Thibaut','Thichef','0623405698',2,'thibaut.juzeau@isen.yncrea.fr','Lille','1998-8-31')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category, email,address,notes) VALUES (3,'Christiaens','Mathilde','0943582113',1,'mathilde.christiaens@isen.yncrea.fr','La Madeleine','chat addict')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category) VALUES (4,'Bomy','Corinne','0732129467',2)");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category,email,notes) VALUES (5,'Jarosset','Corentin','0723435465',3,'corentin.jarosset@isen.yncrea.fr','adore le JAVA')");
		stmt.close();
		connection.close();
	}	
	
	
	 /**
	 * Method used to restore the DB after the tests
	 * 
	 * @throws Exception
	 */
	@Test
	 public void shouldListContacts() {
		// WHEN - we call our DAO to get films
		List<Contact> contacts = contactDao.listAllContacts();
			
		// THEN - our list should contains the 5 following items
		assertThat(contacts).hasSize(5);
		assertThat(contacts).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsExactly(
			tuple(1, "Bomy", "Clara", "Clawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"),
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0732129467", null),
			tuple(3, "Christiaens", "Mathilde", null, "La Madeleine",  null, 1, "mathilde.christiaens@isen.yncrea.fr", "0943582113", "chat addict"),
			tuple(5, "Jarosset", "Corentin", null, null, null, 3, "corentin.jarosset@isen.yncrea.fr", "0723435465", "adore le JAVA"),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null));

	}
	 
	 @Test
	 public void shouldListContactsByGenre() {
		// WHEN - we call our DAO to get films with the "Amis" category
		List<Contact> contacts = contactDao.listContactsByCategory("Amis");

		// THEN - our list should contains 3 items of category "Amis"
		assertThat(contacts).hasSize(3);
		assertThat(contacts).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsExactly(
			tuple(1, "Bomy", "Clara", "Clawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"),
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0732129467", null),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null));	
	 }
	 
	 @Test
	 public void shouldSearchByPhone() {
		// WHEN - we call our DAO to search contacts whose phone contains "06"
		List<Contact> resultSearch = contactDao.searchContact("06");
		
		// THEN - our list should contains the 2 following items
		assertThat(resultSearch).hasSize(2);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null),	
			tuple(1, "Bomy", "Clara", "Clawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 
	 @Test
	 public void shouldSearchByCategoryNames() {
		// WHEN - we call our DAO to search contacts whose phone contains "06"
		List<Contact> resultSearch = contactDao.searchContact("ami");
		
		// THEN - our list should contains the 3 following items
		assertThat(resultSearch).hasSize(3);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0732129467", null),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null),	
			tuple(1, "Bomy", "Clara", "Clawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 
	 @Test
	 public void shouldSearchByDate() {
		// WHEN - we call our DAO to search contacts whose birthdate is in 1997, September
		List<Contact> resultSearch = contactDao.searchContact("1997-09");
		
		// THEN - our list should contains this following item
		assertThat(resultSearch).hasSize(1);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(1, "Bomy", "Clara", "Clawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 
	 @Test
	 public void shouldTellThatContactNotAlreadyExists() {
		// WHEN - we call our DAO to tell us if "Michel Sardou" is already in the DB
		Contact contact = new Contact("Sardou", "Michel", new Category(0, "Sans catégorie"), "0609888888");
		Integer result = contactDao.existContact(contact);
		
		// THEN - As "Michel Sardou" is not in the DB, it should return 0.
		assertThat(result).isEqualTo(0);		
	 }
	 
	 @Test
	 public void shouldTellThatContactAlreadyExistsAndIsDifferent() {
		// WHEN
		Contact contact = new Contact("Bomy", "Clara", new Category(0, "Sans catégorie"), "0648457658");
		Integer result = contactDao.existContact(contact);
		
		// THEN - As "Clara Bomy" is already in the DB, it should return the id of the existing contact.
		assertThat(result).isNotEqualTo(contact.getId());		
	 }
	 
	 @Test
	 public void shouldTellThatContactAlreadyExistsAndIsTheSame() {
		// WHEN -  we try to add a new contact "Clara Bomy" but this contact is already in the DB
		Contact contact = new Contact(1, "Bomy", "Clara", new Category(0, "Sans catégorie"), "0648457658");
		Integer result = contactDao.existContact(contact);
		
		// THEN - As "Clara Bomy" is already in the DB, it should return the id of the contact.
		assertThat(result).isEqualTo(contact.getId());		
	 }


	@Test
	 public void shouldAddContact() throws Exception {
		
		 // WHEN 
		Contact contactToAdd = new Contact("Dary", "Laure", new Category(3, "Pro"), "0687981727");
		Contact newContact = contactDao.addContact(contactToAdd);
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM contact WHERE phone='0687981727'");
		
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("id")).isNotNull();
		assertThat(resultSet.getString("lastname")).isEqualTo("Dary");
		assertThat(resultSet.getString("firstname")).isEqualTo("Laure");
		assertThat(resultSet.getInt("id_category")).isEqualTo(3);
		assertThat(resultSet.getString("phone")).isEqualTo("0687981727");
		assertThat(resultSet.next()).isFalse();

		assertThat(resultSet.next()).isFalse(); // only one contact persisted in DB
		resultSet.close();
		statement.close();
		connection.close();
		
		// Now check the contact that we got back
		assertThat(newContact).isNotNull(); // We got a contact
		assertThat(newContact.getId()).isNotNull(); // the contact has an id
		
		// the contact that we got back is the same that the one we stored
		assertThat(newContact.getLastname()).isEqualTo(contactToAdd.getLastname());
		assertThat(newContact.getFirstname()).isEqualTo(contactToAdd.getFirstname());
		assertThat(newContact.getCategory().getId()).isEqualTo(contactToAdd.getCategory().getId());
		assertThat(newContact.getPhone()).isEqualTo(contactToAdd.getPhone());

	 }
	 
	 @Test
	 public void shouldDeleteContact() throws Exception {
		
		 // WHEN 
		int contact_id = 4;
		contactDao.deleteContact(contact_id);
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM contact WHERE id =" + contact_id);
		
		assertThat(resultSet.next()).isFalse();
		
		resultSet.close();
		statement.close();
		connection.close();

	 }
	 
	 @Test
	 public void shouldUpdateContact() throws Exception {
		
		 // WHEN 
		Contact contact = new Contact(5, "Jarosset", "Corentin", "", "Lille", LocalDate.of(1998, Month.FEBRUARY, 18), new Category(2, "Ami"), "corentin.jarosset@isen.yncrea.fr", "0723435465", "Adore le JAVA (?)");
		contactDao.updateContact(contact);

		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM contact WHERE id=" + contact.getId());
		
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("id")).isEqualTo(contact.getId());
		assertThat(resultSet.getString("lastname")).isEqualTo("Jarosset");
		assertThat(resultSet.getString("firstname")).isEqualTo("Corentin");
		assertThat(resultSet.getString("nickname")).isEqualTo("");
		assertThat(resultSet.getString("address")).isEqualTo("Lille");
		assertThat(resultSet.getDate("birthday").toLocalDate()).isEqualTo(LocalDate.of(1998, Month.FEBRUARY, 18));
		assertThat(resultSet.getInt("id_category")).isEqualTo(2);
		assertThat(resultSet.getString("phone")).isEqualTo("0723435465");
		assertThat(resultSet.getString("email")).isEqualTo("corentin.jarosset@isen.yncrea.fr");
		assertThat(resultSet.getString("notes")).isEqualTo("Adore le JAVA (?)");
		assertThat(resultSet.next()).isFalse();

		resultSet.close();
		statement.close();
		connection.close();

	 }
	 
	 @After
	 public void cleanDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM contact");
		stmt.executeUpdate("DELETE FROM category");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (1,'Sans catégorie')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (2,'Amis')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (3,'Pro')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (4,'Famille')");
		stmt.close();
		connection.close();
	 }
		 
}
