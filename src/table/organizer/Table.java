package table.organizer;

import java.util.List;

import table.organizer.model.Consumable;
import table.organizer.database.PersistenceManager;
import table.organizer.exceptions.DuplicatePersonException;
import table.organizer.model.Person;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Table extends TabActivity {
	
	private PersistenceManager pm;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        pm = new PersistenceManager(this);
        pm.open();
        
        TableManager.getInstance().clear();
        fillData();

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ConsumableActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("consumable").setIndicator(res.getText(R.string.consumables),
                          res.getDrawable(R.drawable.icon))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, PersonActivity.class);
        spec = tabHost.newTabSpec("person").setIndicator(res.getText(R.string.persons),
                          res.getDrawable(R.drawable.icon))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
    
    private void fillData() {
    	List<Person> persons = pm.fetchPersons();
    	List<Consumable> consumables = pm.fetchConsumables();
    	
    	for(Person person : persons){
    		try {
				TableManager.getInstance().addPerson(person.getName());
			} catch (DuplicatePersonException e) {
				// TODO: tem que fazer algo?
			}
    	}
    	
    	for (Consumable consumable : consumables) {
    		
			TableManager.getInstance().addConsumable(consumable.getName(), consumable.getPrice(), consumable.getQuantity());
		}
    	
    }

	void persistAll(){
    	List<Person> persons = TableManager.getInstance().getPersons();
    	List<Consumable> consumables = TableManager.getInstance().getConsumables();
    	
    	for(Person person : persons){
    		pm.createPerson(person);
    	}
    	for (Consumable consumable : consumables) {
			pm.createConsumable(consumable);
		}
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        persistAll();
     
    }
}