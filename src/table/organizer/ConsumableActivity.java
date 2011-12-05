package table.organizer;

import table.organizer.model.Consumable;
import table.organizer.model.TableManager;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ConsumableActivity extends ListActivity {
	
	protected static final int DIALOG_CREATE_ITEM = 0;
	final String tag = "TAG";
	ConsumableAdapter consumableAdapter;
	final private TableManager table = TableManager.getInstance(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		consumableAdapter = new ConsumableAdapter(this);
		
		ListView lv = getListView();
		lv.addHeaderView(makeListHeader(consumableAdapter));
		
		setListAdapter(consumableAdapter);

		lv.setTextFilterEnabled(true);

	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	Log.d("tag", "menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.clear:
        	table.clear();
        	consumableAdapter.notifyDataSetChanged();
            return true;
        case R.id.tip:
            return true;
        case R.id.help:
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		consumableAdapter.notifyDataSetChanged();
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

		dialog.setContentView(R.layout.add_consumable_dialog);
		dialog.setTitle("Custom Dialog");
		return dialog;
	}
	
	private Dialog createNewItemDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.add_consumable_dialog);
		dialog.setTitle(getResources().getString(R.string.new_item));
		final EditText nameEditText = (EditText) dialog.findViewById(R.id.consumable_name_input);
		final EditText quantityEditText = (EditText) dialog.findViewById(R.id.consumable_quantity_input);
		final EditText priceEditText = (EditText) dialog.findViewById(R.id.consumable_price_input);
		Button ok = (Button) dialog.findViewById(R.id.add_item_ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				String name = nameEditText.getText().toString();
				int quantity = Integer.parseInt(quantityEditText.getText().toString());
				double price = Double.parseDouble(priceEditText.getText().toString());
				consumableAdapter.add(name, (int) (price*100), quantity);
				nameEditText.setText("");
				nameEditText.requestFocus();
				quantityEditText.setText("");
				priceEditText.setText("");
				dialog.dismiss();
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

	private View makeListHeader(final ConsumableAdapter consumableAdapter) {
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.consumable_list_header, null);;
		Button insert = (Button)v.findViewById(R.id.plus_button);
		insert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CREATE_ITEM);
				consumableAdapter.notifyDataSetChanged();
			}
		});
		return v;
	}

	private class ConsumableAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public void add (String name, int price, int quantity) {
			table.addConsumable(name, price, quantity);
			notifyDataSetChanged();
		}
		
		public void remove (int id){
			table.removeConsumable(id);
			notifyDataSetChanged();
		}
		
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		
		public ConsumableAdapter(Context context) {
			mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * The number of items in the list is determined by the number of speeches
		 * in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return table.getNumberOfConsumables();
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
				v = mInflater.inflate(R.layout.consumable_item, parent, false);
			}
			else {
				v = convertView;
			}
			
			TextView consumableView= (TextView) v.findViewById(R.id.consumable);
			TextView quantityView = (TextView) v.findViewById(R.id.quantity);
			TextView numPersonsView = (TextView) v.findViewById(R.id.numpersons);
			TextView priceView = (TextView) v.findViewById(R.id.price);
			Button removeButton = (Button) v.findViewById(R.id.remove);
			
			final Consumable consumable = table.getConsumable(position);
			
			consumableView.setText(consumable.getName());
			quantityView.setText(""+consumable.getQuantity());
			numPersonsView.setText(""+consumable.getPersons().size());
			priceView.setText(table.printPrice(consumable.getPrice()));
			
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), PersonsConsumingActivity.class);
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
					remove(consumable.getId());
				}
			});
			
			return v;
		}

	}

}
