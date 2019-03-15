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
			try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE LOWER(name)=?")) {
				statement.setString(1, name.toLowerCase());
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
	

	public Category updateCategory(Category category) {//récupère catégorie avec nouveau nom et ancien id
		//si n'existe pas, crée nouvelle catégorie
		int newId = getIdCategory(category.getName());
		if (newId == 0) {
			newId = addCategory(category.getName());
		}

		//remplacer id catégorie dans les contacts concernés
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			String sqlQuery = "UPDATE contact SET id_category=? WHERE id_category=?";
			try (PreparedStatement contactStmt = connection.prepareStatement(sqlQuery)) {
				contactStmt.setInt(1, category.getId());//ancienne clé
				contactStmt.setInt(2, newId);//nouvelle clé
				contactStmt.executeUpdate();

				//supprimer ancienne catégorie
				try (PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE id = ?")) {
					statement.setInt(1, category.getId());//ancienne clé
					statement.executeUpdate();

					category.setId(newId);
					return category; //retourne catégorie avec sa nouvelle id
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
