package table.organizer;

import table.organizer.exceptions.DuplicatePersonException;
import table.organizer.model.Person;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonActivity extends ListActivity {
	
	protected static final int DIALOG_CREATE_ITEM = 0;
	final String tag = "TAG";
	PersonAdapter personAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		ListView lv = getListView();
		personAdapter = new PersonAdapter(this);
		lv.addHeaderView(makeListHeader(personAdapter));
		setListAdapter(personAdapter);
		lv.setTextFilterEnabled(true);

	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = createEmptyDialog();
	    switch(id) {
	    	case DIALOG_CREATE_ITEM:
	    		dialog = createNewItemDialog();
	    	default:
	    }
	    return dialog;
	}
	
	private Dialog createEmptyDialog(){
		Context mContext = this;
		Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.add_person_dialog);
		dialog.setTitle("Custom Dialog");

		return dialog;
	}
	
	private Dialog createNewItemDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.add_person_dialog);
		dialog.setTitle(getResources().getString(R.string.new_item));
		
		Button ok = (Button) dialog.findViewById(R.id.add_item_ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText nameEditText = (EditText) dialog.findViewById(R.id.person_name_input);
				
				String name = nameEditText.getText().toString();
				try {
					personAdapter.add(name);
					dialog.dismiss();
				} catch (DuplicatePersonException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), R.string.duplicatePerson, Toast.LENGTH_SHORT);
				}
				
			}
		});
		Button cancel = (Button) dialog.findViewById(R.id.add_item_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}

	private View makeListHeader(final PersonAdapter personAdapter) {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.person_list_header, null);;
		Button insert = (Button)v.findViewById(R.id.plus_button);
		insert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CREATE_ITEM);
				personAdapter.notifyDataSetChanged();
			}
		});
		return v;
	}

	private class PersonAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		final private TableManager table = TableManager.getInstance();
		
		public void add (String name) throws DuplicatePersonException{
			table.addPerson(name);
			notifyDataSetChanged();
		}
		
		public void remove (String name){
			table.removePerson(name);
			notifyDataSetChanged();
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		
		public PersonAdapter(Context context) {
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * The number of items in the list is determined by the number of speeches
		 * in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return table.getNumberOfPersons();
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
				v = mInflater.inflate(R.layout.person_item, parent, false);
			}
			else {
				v = convertView;
			}
			
			TextView personView = (TextView) v.findViewById(R.id.person);
			TextView priceView = (TextView) v.findViewById(R.id.price);
			Button removeButton = (Button) v.findViewById(R.id.remove);
			
			final Person person = table.getPerson(position);
			
			personView.setText(person.getName());
			priceView.setText(table.printPrice(person.getPersonalBill()));
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ConsumedItemsActivity.class);
					Bundle extras = new Bundle();
					extras.putInt(table.POSITION, position);
					intent.putExtras(extras);
					startActivity(intent);
					notifyDataSetChanged();
				}
			});
			
			removeButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					remove(person.getName());
				}
			});
			
			return v;
		}

	}

}
