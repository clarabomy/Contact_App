package isen.java2.model.db.entities;

public class CategoryType {
	protected short id;
	protected String name;
	
	public CategoryType(short id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public short getId() {
		return id;
	}
	public void setId(short id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
