package table.organizer;

import java.util.ArrayList;
import java.util.HashMap;

import table.organizer.database.TableManager;
import table.organizer.model.Consumable;
import table.organizer.model.Person;
import table.organizer.utils.MoneyUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class PersonsConsumingActivity extends Activity {

	final private TableManager table = TableManager.getInstance(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.consumed_items_layout);
	    
	    Bundle extras = getIntent().getExtras();
	    int consumableId = extras.getInt(table.POSITION);
	    final Consumable consumable = table.getConsumable(consumableId);
	    
	    TextView name = (TextView) findViewById(R.id.name);
	    TextView price = (TextView) findViewById(R.id.price);
	    CheckBox checkBox = (CheckBox) findViewById(R.id.mine);
	    checkBox.setText(R.string.personsConsuming);
	    
	    name.setText(consumable.getName());
	    price.setText(MoneyUtils.printPrice(consumable.getPricePerPerson()));
	    
	    ListView lv = (ListView) findViewById(R.id.items_list);
		final PersonCheckListAdapter checkListAdapter = new PersonCheckListAdapter(this, consumable, price);
		
		lv.setAdapter(checkListAdapter);

		lv.setTextFilterEnabled(true);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					for (Person person: table.fetchPersons()) {
						if (!checkListAdapter.isChecked(person))
							checkListAdapter.remove(person);
					}
				}
				else {
					checkListAdapter.clear();
					populateCheckList(consumable, checkListAdapter);
				}
			}
		});
		
		populateCheckList(consumable, checkListAdapter);
		
	}

	private void populateCheckList(Consumable consumable,
			final PersonCheckListAdapter checkListAdapter) {
		for (Person person : table.fetchPersons()) {
			checkListAdapter.add(person, false);
		}
		
		for (Person person : consumable.getPersons()) {
			checkListAdapter.setChecked(person.getName(), true);
		}
	}
	
}

class PersonCheckListAdapter extends BaseAdapter{
	
	private Consumable consumable;
	private TextView price;
	private LayoutInflater mInflater;
	private ArrayList<Person> items = new ArrayList<Person>();
	private HashMap<String, Boolean> itemsCheck = new HashMap<String, Boolean>();
	
	public void add (Person person, Boolean checked){
		items.add(person);
		itemsCheck.put(person.getName(), checked);
		notifyDataSetChanged();
	}
	
	public void remove (Person person) {
		items.remove(person);
		itemsCheck.remove(person.getName());
		notifyDataSetChanged();
	}
	
	public void clear () {
		items.clear();
		itemsCheck.clear();
		notifyDataSetChanged();
	}
	
	public void setChecked(String name, Boolean checked){
		itemsCheck.put(name, checked);
	}
	
	public Boolean isChecked (Person person) {
		return itemsCheck.get(person.getName());
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public PersonCheckListAdapter(Context context, Consumable consumable, TextView textView) {
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.consumable = consumable; 
		this.price = textView;
	}

	/**
	 * The number of items in the list is determined by the number of speeches
	 * in our array.
	 * 
	 * @see android.widget.ListAdapter#getCount()
	 */
	public int getCount() {
		return itemsCheck.size();
	}

	/**
	 * Since the data comes from an array, just returning the index is
	 * sufficent to get at the data. If we were using a more complex data
	 * structure, we would return whatever object represents one row in the
	 * list.
	 * 
	 * @see android.widget.ListAdapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/**
	 * Use the array index as a unique id.
	 * 
	 * @see android.widget.ListAdapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* Minha propria view, tirada do xml list_item*/
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(R.layout.checklist_item, parent, false);
		}
		else {
			v = convertView;
		}
		
		TextView itemView = (TextView) v.findViewById(R.id.item);
		CheckBox checkBoxView = (CheckBox) v.findViewById(R.id.checkbox);
		
		final Person person = items.get(position);
		String itemName = person.getName();
		Boolean checked = itemsCheck.get(itemName);
		
		checkBoxView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(itemsCheck.get(person.getName())!=isChecked){
					if(isChecked){
						consumable.addPerson(person);
						person.addConsumable(consumable);
					}else{
						consumable.removePerson(person);
						person.removeConsumable(consumable);
					}
				}
								
				setChecked(person.getName(), isChecked);
				
				price.setText(MoneyUtils.printPrice(consumable.getPricePerPerson()));
			}
		});
		
		itemView.setText(itemName);
		checkBoxView.setChecked(checked);			
					
		return v;
	}

}

