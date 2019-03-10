package isen.java2.model.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.Category;

public class CategoryDao {
	
	public List<Category> getCategoryList() {
		List<Category> listOfCategories = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results = statement.executeQuery("SELECT * FROM category")) {
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
			e.printStackTrace();
		}
		return listOfCategories;	
	}
	
	public int getIdCategory(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM category WHERE name=?")) {
				statement.setString(1, name);
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
			e.printStackTrace();
		}	
		return null;
	}

	public int addCategory(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "INSERT INTO category(name) VALUES(?)";
			
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
}
