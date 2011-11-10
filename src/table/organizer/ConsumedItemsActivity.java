package table.organizer;

import java.util.ArrayList;
import java.util.HashMap;

import table.organizer.model.Consumable;
import table.organizer.model.Person;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class ConsumedItemsActivity extends Activity {

	TableManager table = TableManager.getInstance();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.consumed_items_layout);
	    
	    Bundle extras = getIntent().getExtras();
	    int position = extras.getInt(table.POSITION);
	    final Person person = table.getPerson(position);
	    
	    TextView name = (TextView) findViewById(R.id.name);
	    TextView price = (TextView) findViewById(R.id.price);
	    CheckBox checkBox = (CheckBox) findViewById(R.id.mine);
	    
	    name.setText(person.getName());
	    price.setText(table.printPrice(person.getPersonalBill()));
	    
	    ListView lv = (ListView) findViewById(R.id.items_list);
		final ConsumableCheckListAdapter checkListAdapter = new ConsumableCheckListAdapter(this, person, price);
		
		lv.setAdapter(checkListAdapter);

		lv.setTextFilterEnabled(true);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					for (Consumable consumable : table.getConsumables()) {
						if (!checkListAdapter.isChecked(consumable))
							checkListAdapter.remove(consumable);
					}
				}
				else {
					checkListAdapter.clear();
					populateCheckList(person, checkListAdapter);
				}
			}
		});
		
		populateCheckList(person, checkListAdapter);
		
	}

	private void populateCheckList(Person person,
			final ConsumableCheckListAdapter checkListAdapter) {
		for (Consumable consumable : table.getConsumables()) {
			checkListAdapter.add(consumable, false);
		}
		
		for (Consumable consumable : person.getConsumables()) {
			checkListAdapter.setChecked(consumable.getId(), true);
		}
	}
	
}

class ConsumableCheckListAdapter extends BaseAdapter{
	
	private Person person;
	private TextView price;
	private LayoutInflater mInflater;
	private ArrayList<Consumable> items = new ArrayList<Consumable>();
	private HashMap<Integer, Boolean> itemsCheck = new HashMap<Integer, Boolean>();
	
	public void add (Consumable consumable, Boolean checked){
		items.add(consumable);
		itemsCheck.put(consumable.getId(), checked);
		notifyDataSetChanged();
	}
	
	public void remove (Consumable consumable) {
		items.remove(consumable);
		itemsCheck.remove(consumable.getId());
		notifyDataSetChanged();
	}
	
	public void clear () {
		items.clear();
		itemsCheck.clear();
		notifyDataSetChanged();
	}
	
	public void setChecked(int id, Boolean checked){
		itemsCheck.put(id, checked);
	}
	
	public Boolean isChecked (Consumable consumable) {
		return itemsCheck.get(consumable.getId());
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public ConsumableCheckListAdapter(Context context, Person person, TextView textView) {
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.person = person; 
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
		
		final Consumable consumable = items.get(position);
		String itemName = consumable.getName();
		Boolean checked = itemsCheck.get(consumable.getId());
		
		checkBoxView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				Log.d("tag", "entrei");
				if(itemsCheck.get(consumable.getId())!=isChecked){
					if(isChecked){
						person.addConsumable(consumable);
						consumable.addPerson(person);
					}else{
						person.removeConsumable(consumable);
						consumable.removePerson(person);
					}
				}
								
				setChecked(consumable.getId(), isChecked);
				
				price.setText(TableManager.getInstance().printPrice(person.getPersonalBill()));
			}
		});
		
		itemView.setText(itemName);
		checkBoxView.setChecked(checked);			
					
		return v;
	}

}

