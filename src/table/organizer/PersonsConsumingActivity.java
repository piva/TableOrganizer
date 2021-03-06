package table.organizer;

import java.util.ArrayList;
import java.util.HashMap;

import table.organizer.model.Consumable;
import table.organizer.model.Person;
import table.organizer.model.TableManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class PersonsConsumingActivity extends Activity {

	TableManager table = TableManager.getInstance(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.consumed_items_layout);

		Bundle extras = getIntent().getExtras();
		int position = extras.getInt(table.POSITION);
		final Consumable consumable = table.getConsumable(position);

		TextView name = (TextView) findViewById(R.id.name);
		TextView price = (TextView) findViewById(R.id.price);
		CheckBox checkBox = (CheckBox) findViewById(R.id.mine);
		checkBox.setText(R.string.personsConsuming);

		name.setText(consumable.getName());
		price.setText(table.printPrice(consumable.getPricePerPerson()));

		ListView lv = (ListView) findViewById(R.id.items_list);
		final PersonCheckListAdapter checkListAdapter = new PersonCheckListAdapter(this, consumable, price);

		lv.setAdapter(checkListAdapter);

		lv.setTextFilterEnabled(true);

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					for (Person person: table.getPersons()) {
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
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.clear:
            return true;
        case R.id.tip:
            return true;
        case R.id.help:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	private void populateCheckList(Consumable consumable,
			final PersonCheckListAdapter checkListAdapter) {
		for (Person person : table.getPersons()) {
			checkListAdapter.add(person, false);
		}

		for (Person person : consumable.getPersons()) {
			checkListAdapter.setChecked(person.getName(), true);
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
							table.addConsumableToPerson(consumable, person);
						}else{
							table.removeConsumableFromPerson(consumable, person);
						}
					}

					setChecked(person.getName(), isChecked);

					price.setText(table.printPrice(consumable.getPricePerPerson()));
				}
			});

			itemView.setText(itemName);
			checkBoxView.setChecked(checked);			

			return v;
		}

	}

}