package table.organizer;

import java.util.List;

import table.organizer.exceptions.DuplicatePersonException;
import table.organizer.model.Consumable;
import table.organizer.model.Person;

public class TableManager {
	private int nextId;
	private List<Person> persons;
	private List<Consumable> consumables;
	private int tip;
	private static TableManager tableManager = new TableManager();

	public static TableManager getInstance(){
		return tableManager;
	}
	
	private TableManager() {
		nextId = 0;
		tip = 10;
	}
	
	public int getTip() {
		return tip;
	}

	public void setTip(int tip) {
		this.tip = tip;
	}

	/**
	 * 
	 * @return total bill price in cents
	 */
	public int getTotalBill(){
		
		int price = 0;
		
		for(Consumable consumable : consumables){
			price += consumable.getTotalPrice();
		}
		
		return price;
	}
	
	Person addPerson(String name) throws DuplicatePersonException {

		for (Person person : persons) {
			if(person.getName().equals(name)){
				throw new DuplicatePersonException("Person already exists");
			}
		}
		
		Person newPerson = new Person(name);
		
		persons.add(newPerson);
		
		return newPerson;
	}
	
	boolean removePerson(String name){
		return persons.remove(new Person(name));
	}
	
	Consumable addConsumable(String name, int price, int quantity){
		
		Consumable newConsumable = new Consumable(name, price, quantity, ++nextId);
		
		consumables.add(newConsumable);
		
		return newConsumable;
	}
	
	boolean removeConsumable(int id){
		return consumables.remove(new Consumable(null, 0, 0, id));
	}
	
	void addConsumableToPerson(Consumable consumable, Person person){
		consumable.addPerson(person);
		person.addConsumable(consumable);
	}
	
}
