package isen.java2.model.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import isen.java2.model.db.entities.CategoryType;

public class CategoryTypeDao {
	
	public List<CategoryType> getCategoryList() {
		List<CategoryType> listOfCategories = new ArrayList<>();
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (Statement statement = connection.createStatement()) {
				try (ResultSet results = statement.executeQuery("SELECT * FROM category")) {
					while (results.next()) {
						CategoryType category = new CategoryType(
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
	
	public CategoryType getCategoryType(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM category WHERE name=?")) {
				statement.setString(1, name);
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						return new CategoryType(
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

	public void addCategoryType(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "INSERT INTO category(name) VALUES(?)";
			try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, name);
				statement.executeUpdate();
			}
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
