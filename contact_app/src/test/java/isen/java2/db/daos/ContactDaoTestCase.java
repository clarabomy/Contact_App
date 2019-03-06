package isen.java2.db.daos;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import isen.java2.model.db.daos.ContactDao;
import isen.java2.model.db.daos.DataSourceFactory;
import isen.java2.model.db.entities.Category;
import isen.java2.model.db.entities.Contact;

public class ContactDaoTestCase {
	
	private ContactDao contactDao = new ContactDao();

	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM category");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (1,'Sans catégorie')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (2,'Amis')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (3,'Pro')");
		stmt.executeUpdate("DELETE FROM contact");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address,birthday,notes) VALUES (1,'Bomy','Clara','Clawawa','0642398475',2,'clara.bomy@isen.yncrea.fr','Loos','1997-9-13','aime les IA')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address, birthday) VALUES (2,'Juzeau','Thibaut','Thichef','0623405698',2,'thibaut.juzeau@isen.yncrea.fr','Lille','1998-8-31')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category, email,address,notes) VALUES (3,'Christiaens','Mathilde','0943582113',1,'mathilde.christiaens@isen.yncrea.fr','LaMadeleine','chat addict')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category) VALUES (4,'Bomy','Corinne','0432129467',2)");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category,email,notes) VALUES (5,'Jarosset','Corentin','0723435465',3,'coco_du_59@kikoo.fr','where is corentin?')");
		stmt.close();
		connection.close();
	}	
	
	@Test
	 public void shouldListContacts() {
		// WHEN
		List<Contact> contacts = contactDao.listAllContacts();
			
		// THEN
		assertThat(contacts).hasSize(5);
		assertThat(contacts).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsExactly(
			tuple(1, "Bomy", "Clara", "Clawawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"),
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0432129467", null),
			tuple(3, "Christiaens", "Mathilde", null, "LaMadeleine",  null, 1, "mathilde.christiaens@isen.yncrea.fr", "0943582113", "chat addict"),
			tuple(5, "Jarosset", "Corentin", null, null, null, 3, "coco_du_59@kikoo.fr", "0723435465", "where is corentin?"),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null));

	}
	 
	 @Test
	 public void shouldListContactsByGenre() {
		// WHEN
		List<Contact> contacts = contactDao.listContactsByCategory("Amis");

		// THEN
		assertThat(contacts).hasSize(3);
		assertThat(contacts).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsExactly(
			tuple(1, "Bomy", "Clara", "Clawawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"),
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0432129467", null),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null));	
	 }
	 
	 @Test
	 public void shouldSearchByPhone() {
		// WHEN
		List<Contact> resultSearch = contactDao.searchContact("06");
		
		// THEN
		assertThat(resultSearch).hasSize(2);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null),	
			tuple(1, "Bomy", "Clara", "Clawawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 
	 @Test
	 public void shouldSearchByCategoryNames() {
		// WHEN
		List<Contact> resultSearch = contactDao.searchContact("ami");
		
		// THEN
		assertThat(resultSearch).hasSize(3);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(4, "Bomy", "Corinne", null, null, null, 2, null, "0432129467", null),
			tuple(2, "Juzeau", "Thibaut", "Thichef", "Lille", LocalDate.of(1998, Month.AUGUST, 31), 2, "thibaut.juzeau@isen.yncrea.fr", "0623405698", null),	
			tuple(1, "Bomy", "Clara", "Clawawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 
	 @Test
	 public void shouldSearchByDate() {
		// WHEN
		List<Contact> resultSearch = contactDao.searchContact("1997-09");
		
		// THEN
		assertThat(resultSearch).hasSize(1);
		assertThat(resultSearch).extracting("id", "lastname", "firstname", "nickname", "address", "birthdate", "category.id", "mail", "phone", "notes").containsOnly(
			tuple(1, "Bomy", "Clara", "Clawawa", "Loos", LocalDate.of(1997, Month.SEPTEMBER, 13), 2, "clara.bomy@isen.yncrea.fr", "0642398475", "aime les IA"));
	 }	
	 

	@Test
	 public void shouldAddContact() throws Exception {
		
		 // WHEN 
		Contact contact = new Contact("Dary", "Laure", new Category(3, "Pro"), "0687981727");
		contactDao.addContact(contact);
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM contact WHERE phone='0687981727'");
		
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("id")).isNotNull();
		assertThat(resultSet.getString("lastname")).isEqualTo("Dary");
		assertThat(resultSet.getString("firstname")).isEqualTo("Laure");
		assertThat(resultSet.getInt("id_category")).isEqualTo(3);
		assertThat(resultSet.next()).isFalse();

		resultSet.close();
		statement.close();
		connection.close();

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
		Contact contact = new Contact(5, "Jarosset", "Corentin", "", "Lille", LocalDate.of(1998, Month.FEBRUARY, 18), new Category(2, "Ami"), "coco_du_59@kikoo.fr", "0723435465", "He is here !");
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
		assertThat(resultSet.getString("email")).isEqualTo("coco_du_59@kikoo.fr");
		assertThat(resultSet.getString("notes")).isEqualTo("He is here !");
		assertThat(resultSet.next()).isFalse();

		resultSet.close();
		statement.close();
		connection.close();

	 }
		 
}
