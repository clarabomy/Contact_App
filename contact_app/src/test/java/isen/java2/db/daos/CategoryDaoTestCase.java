package isen.java2.db.daos;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import isen.java2.model.db.daos.CategoryDao;
import isen.java2.model.db.daos.DataSourceFactory;
import isen.java2.model.db.entities.Category;

public class CategoryDaoTestCase {
	
	private CategoryDao categoryDao = new CategoryDao(); 
	
	@Before
	public void initDatabase() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DELETE FROM category");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (1,'Sans catégorie')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (2,'Amis')");
		stmt.executeUpdate("INSERT INTO category(id,name) VALUES (3,'Pro')");
		stmt.close();
		connection.close();
	}
	
	@Test 
	public void shouldListCategories() {
		
		//WHEN
		List<Category> categories = categoryDao.getCategoryList();
		
		// THEN
		assertThat(categories).hasSize(3);
		assertThat(categories).extracting("id", "name").containsOnly(tuple(1, "Sans catégorie"), tuple(2, "Amis"),
				tuple(3, "Pro"));
	}
	
	@Test
	public void shouldGetCategoryByName() {
		
		// WHEN
		Category category = categoryDao.getCategory("Amis");
		
		// THEN
		assertThat(category.getId()).isEqualTo(2);
		assertThat(category.getName()).isEqualTo("Amis");
	}
	
	@Test
	public void shouldNotGetUnknownCategory() {
		
		// WHEN
		Category category = categoryDao.getCategory("Unknown");
		
		// THEN
		assertThat(category).isNull();
	}
	
	@Test
	public void shouldAddCategory() throws Exception {
		
		// WHEN 
		categoryDao.addCategory("Perso");
		
		// THEN
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM category WHERE name='Perso'");

		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("id")).isNotNull();
		assertThat(resultSet.getString("name")).isEqualTo("Perso");
		assertThat(resultSet.next()).isFalse();
		
		resultSet.close();
		statement.close();
		connection.close();
	}
	
}



