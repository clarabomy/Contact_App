package isen.java2.model.db.entities;

import isen.java2.model.db.daos.CategoryDao;

public class Category {
	
	private CategoryDao categoryDao = new CategoryDao(); 
	protected int id;
	protected String name;
	
	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Category(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {	
		int id = categoryDao.getIdCategory(name);
		
		if (id == 0) {
			id = categoryDao.addCategory(name);
		}
		
		this.name = name;
		this.id = id;
	}
}
