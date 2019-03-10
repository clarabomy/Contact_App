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
	
	public void setId(short id) {
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
		
			
		
		// 1. vérifier si le name a un id associé en bdd
			// cas 1. il existe : je passe à la suite
			// cas 2. il n'existe pas : faut ajouter name dans table catégorie pour avoir id associé
		// 2. je modifie id et name

	}
	
	
}
