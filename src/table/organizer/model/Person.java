package table.organizer.model;

import java.util.ArrayList;
import java.util.List;

import table.organizer.TableManager;

public class Person {
	TableManager table = TableManager.getInstance();
	List<Consumable> consumables;
	String name;
	
	public Person(String name){
		this.name = name;
		consumables = new ArrayList<Consumable>();
	}
	
	public String getID(){
		return name;
	}
	
	/**
	 * 
	 * @param
	 * @return personal bill to be paid in cents
	 */
	public int getPersonalBill(){
		int bill = 0;
		int tip = table.getTip();
		
		for (Consumable consumable : consumables) {
			bill += consumable.getPricePerPerson();
		}
		
		//Int division rounded floor
		return (bill + (bill*tip)/100);
	}
	
	public List<Consumable> getConsumables() {
		return consumables;
	}

	public void setConsumables(List<Consumable> consumables) {
		this.consumables = consumables;
	}
	
	public void addConsumable(Consumable consumable){
		this.consumables.add(consumable);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void removeConsumable(Consumable consumable) {
		consumables.remove(consumable);
	}

}
