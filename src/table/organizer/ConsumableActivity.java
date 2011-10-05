package table.organizer;

import java.util.ArrayList;
import java.util.List;

import table.organizer.model.Consumable;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumableActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Log.d("BLA", "Bla 1");
		
		super.onCreate(savedInstanceState);
		List<Consumable> consumables;// = TableManager.getInstance().getConsumables();
		consumables = new ArrayList<Consumable>();
		consumables.add(new Consumable("Consumable 1", 100, 1, 1));
		
		Log.d("BLA", "Bla 2");
		
		String consumableStrings[] = new String[consumables.size()];
		int pos = 0;
		for (Consumable consumable : consumables) {
			consumableStrings[pos++] = consumable.getName();		
		}
		
		Log.d("BLA", "Bla 3");
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.consumable_item, consumableStrings));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
						Toast.LENGTH_SHORT).show();
			}
		});

	}

}
