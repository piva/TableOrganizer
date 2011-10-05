package table.organizer.model;

import java.util.List;

public class Person {
	List<Consumable> consumables;
	String name;
	
	public List<Consumable> getConsumables() {
		return consumables;
	}

	public void setConsumables(List<Consumable> consumables) {
		this.consumables = consumables;
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

	public Person(String name){
		this.name = name;
	}
		
}
