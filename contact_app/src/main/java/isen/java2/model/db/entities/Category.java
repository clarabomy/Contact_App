package isen.java2.model.db.entities;

import isen.java2.model.db.daos.CategoryDao;

/**
 * @author Clara Bomy
 *
 *         To respect the DAO model, we use this class. It allows us to abstract the technical
 *         access to data for the rest of the application.
 *         
 *         In the database, there is a table called category.
 *         In the java code, a category is an object of the Category class.
 */
public class Category {
	
	private CategoryDao categoryDao = new CategoryDao(); 
	protected int id;
	protected String name;
	
	/**
	 * Contructor of the class Category
	 * 
	 * @param id
	 * @param name
	 */
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Constructor of the class Category
	 * 
	 * @param name
	 */
	public Category(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the id of the category
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {	
		//We check that the category is not already in the DB 
		int id = categoryDao.getIdCategory(name);
		
		//If it's not, we create a new one
		if (id == 0) {
			id = categoryDao.addCategory(name);
		}
		
		this.name = name;
		this.id = id;
	}
}
