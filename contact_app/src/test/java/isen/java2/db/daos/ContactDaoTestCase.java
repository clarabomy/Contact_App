package isen.java2.db.daos;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;

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
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (1,'Famille')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (2,'Amis')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (3,'Pro')");
		stmt.executeUpdate("DELETE FROM contact");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address,birthday,notes) VALUES (1,'Bomy','Clara','Clawawa','0642398475',2,'clara.bomy@isen.yncrea.fr','Loos','1997-9-13','aime les IA')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,nickname,phone,id_category,email,address, birthday) VALUES (2,'Juzeau','Thibaut','Thichef','0623405698',3,'thibaut.juzeau@isen.yncrea.fr','Lille','1998-8-31')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,email,address,notes) VALUES (3,'Christiaens','Mathilde','0943582113','mathilde.christiaens@isen.yncrea.fr','LaMadeleine','chat addict')");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category) VALUES (4,'Bomy','Corinne','0432129467',2)");
		stmt.executeUpdate("INSERT INTO contact(id,lastname,firstname,phone,id_category,email,notes) VALUES (5,'Jarosset','Corentin','0723435465',3,'coco_du_59@kikoo.fr','where is corentin?')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldAddContact() throws Exception {
		
		 // WHEN 
		Contact contact = new Contact(6, "Dary", "Laure", "", "Versailles", LocalDate.of(1966, Month.FEBRUARY, 3), new Category(1, "Famille"), "dary-immo@gmail.com", "0687981727", "toujours de bonne humeur");
		contactDao.addContact(contact);
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM contact WHERE phone='0687981727'");
		
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("id")).isNotNull();
		assertThat(resultSet.getString("lastname")).isEqualTo("Dary");
		assertThat(resultSet.getString("firstname")).isEqualTo("Laure");
		assertThat(resultSet.getString("nickname")).isEqualTo("");
		assertThat(resultSet.getString("address")).isEqualTo("Versailles");
		assertThat(resultSet.getDate("birthday").toLocalDate()).isEqualTo(LocalDate.of(1966, Month.FEBRUARY, 3));
		assertThat(resultSet.getInt("id_category")).isEqualTo(1);
		assertThat(resultSet.getString("email")).isEqualTo("dary-immo@gmail.com");
		assertThat(resultSet.getString("notes")).isEqualTo("toujours de bonne humeur");
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

/*


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import fr.isen.java2.db.daos.DataSourceFactory;
import org.junit.Before;
import org.junit.Test;

import fr.isen.java2.db.daos.FilmDao;
import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;


public class FilmDaoTestCase {
	
	private FilmDao filmDao = new FilmDao();

	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM film");
		stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26', 1, 120, 'director 1', 'summary of the first film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14', 2, 114, 'director 2', 'summary of the second film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12', 2, 176, 'director 3', 'summary of the third film')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListFilms() {
		// WHEN
		List<Film> films = filmDao.listFilms();
		// THEN
		assertThat(films).hasSize(3);
		assertThat(films).extracting("id", "title", "releaseDate", "genre.id", "duration", "director", "summary").containsOnly(tuple(1, "Title 1", LocalDate.of(2015, Month.NOVEMBER, 26), 1, 120, "director 1", "summary of the first film"),
				tuple(2, "My Title 2", LocalDate.of(2015, Month.NOVEMBER, 14), 2, 114, "director 2", "summary of the second film"),
				tuple(3, "Third title", LocalDate.of(2015, Month.DECEMBER, 12), 2, 176, "director 3", "summary of the third film"));
	 }
	
	 @Test
	 public void shouldListFilmsByGenre() {
		// WHEN
		List<Film> films = filmDao.listFilmsByGenre("Comedy");

		assertThat(films).hasSize(2);
		assertThat(films).extracting("id", "title", "releaseDate", "genre.id", "duration", "director", "summary").containsOnly(
				tuple(2, "My Title 2", LocalDate.of(2015, Month.NOVEMBER, 14), 2, 114, "director 2", "summary of the second film"),
				tuple(3, "Third title", LocalDate.of(2015, Month.DECEMBER, 12), 2, 176, "director 3", "summary of the third film"));
	}
	
	 @Test
	 public void shouldAddFilm() throws Exception {
			// WHEN 
		Film film = new Film(4, "ISEN the movie", LocalDate.of(2016, Month.NOVEMBER, 14), new Genre(1, "Drama"), 114, "Clara B.", "summary of ISEN the movie");
		filmDao.addFilm(film);
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM film WHERE title='ISEN the movie'");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("idfilm")).isNotNull();
		assertThat(resultSet.getString("title")).isEqualTo("ISEN the movie");
		assertThat(resultSet.getDate("release_date").toLocalDate()).isEqualTo(LocalDate.of(2016, Month.NOVEMBER, 14));
		assertThat(resultSet.getInt("genre_id")).isEqualTo(1);
		assertThat(resultSet.getInt("duration")).isEqualTo(114);
		assertThat(resultSet.getString("director")).isEqualTo("Clara B.");
		assertThat(resultSet.getString("summary")).isEqualTo("summary of ISEN the movie");
		resultSet.close();
		statement.close();
		connection.close();

	 }
}
*/