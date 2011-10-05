package table.organizer.model;

import java.util.ArrayList;
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
		persons = new ArrayList<Person>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consumable other = (Consumable) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<Person> getPersons() {
		return persons;
	}
	
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
	public void addPerson(Person person){
		this.persons.add(person);
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

	public int getPricePerPerson() {
		if(getTotalPrice()%persons.size() == 0){
			return (getTotalPrice()/persons.size());	
		} else {
			return (getTotalPrice()/persons.size() + 1);
		}
	}
	
}

