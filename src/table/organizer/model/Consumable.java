package table.organizer.model;

import java.util.List;

public class Consumable {
	private List<Person> persons;
	private int price; // in cents
	private int quantity;
	private String name;
	private int id;
	
	public Consumable(String name, int price, int quantity, int id){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.id = id;
	}
	
	public List<Person> getPersons() {
		return persons;
	}
	
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getTotalPrice() {
		return price * quantity;
	}
	
	
}

