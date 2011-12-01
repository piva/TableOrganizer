package table.organizer.model;

import java.util.ArrayList;
import java.util.List;


public class Person {
	private List<Consumable> consumables;
	private String name;
	
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
		
		for (Consumable consumable : consumables) {
			bill += consumable.getPricePerPerson();
		}
		
		return bill;
	}
	
	public List<Consumable> getConsumables() {
		return consumables;
	}

	protected void setConsumables(List<Consumable> consumables) {
		this.consumables = consumables;
	}
	
	protected void addConsumable(Consumable consumable){
		if(consumable != null)
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

	protected void setName(String name) {
		this.name = name;
	}

	protected void removeConsumable(Consumable consumable) {
		consumables.remove(consumable);
	}

}
