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

/**
 * @author Clara Bomy
 *
 *         this is our Category Data Access Class just as a reminder, this is it"s
 *         structure ; category(id, name)
 *         
 */
public class CategoryDao {
	
	/**
	 * @return the list of all categories in the database
	 */
	public List<Category> getCategoryList() {
		
		// Instantiate the list to return
		List<Category> listOfCategories = new ArrayList<>();
		// get a new connection
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			// get a new statement
			try (Statement statement = connection.createStatement()) {
				// get a new resultset
				try (ResultSet results = statement.executeQuery("SELECT * FROM category ORDER BY name")) {
					// we iterate on the resultset to create a new category
					// from the resuultset values and put it into the list
					while (results.next()) {
						Category category = new Category(
								results.getInt("id"),
								results.getString("name"));
						
						listOfCategories.add(category);
					}
				}
			}
		}
		catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}
		return listOfCategories;	
	}
	
	/**
	 * Gets the id of a category by it's name
	 * 
	 * @param name
	 * @return the id of the corresponding category
	 * 
	 */
	public int getIdCategory(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			
			// We will use a prepared statement, as we have variables to pass to the request
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE LOWER(name)=?")) {
				statement.setString(1, name.toLowerCase());
				try (ResultSet results = statement.executeQuery()) {
					// We take care of the case where the name returns nothing
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
	 * Gets a category by it's name
	 * 
	 * @param name
	 * @return the corresponding category
	 * 
	 */
	public Category getCategory(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM category WHERE name=?")) {
				statement.setString(1, name);
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						return new Category(
								results.getInt("id"),
								results.getString("name"));
					}
				}
			}
		} 
		catch (SQLException e) {
			throw new RuntimeException("Something went horribly wrong with our DB and you cannot do much about it...", e);
		}	
		return null;
	}

	/**
	 * Adds a new category
	 * 
	 * @param name
	 * @return the id of the new category (or 0 if something went wrong)
	 */
	public int addCategory(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "INSERT INTO category(name) VALUES(?)";
			
			// execcuteUpdate() instead of execcuteQuery(), since we are writing to the DB
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, name);
				statement.executeUpdate();
				
				try (ResultSet keys = statement.getGeneratedKeys()) {
					keys.next();
					return keys.getInt(1);	
				}	
			}
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	

	/**
	 * Updates a category (get back category with new name and old id)
	 * 
	 * @param name
	 * @return the updated category with its new id (or null if something went wrong)
	 */
	public Category updateCategory(Category category) {
		// if the category does not exist, we create a new category	
		int newId = getIdCategory(category.getName());
		if (newId == 0) {
			newId = addCategory(category.getName());
		}

		// replace id category in the relevant contacts
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "UPDATE contact SET id_category=? WHERE id_category=?";
			try (PreparedStatement contactStmt = connection.prepareStatement(sqlQuery)) {
				contactStmt.setInt(1, category.getId());//old id
				contactStmt.setInt(2, newId);//new id
				contactStmt.executeUpdate();

				//delete old category
				try (PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE id = ?")) {
					statement.setInt(1, category.getId());//old id
					statement.executeUpdate();

					category.setId(newId);
					return category; 
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
