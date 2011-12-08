package table.organizer;

import table.organizer.model.TableManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Table extends TabActivity {
    public static final int TIP_DIALOG = 177;
    public static final String PREFS_NAME = "SaveConfigFile";
    public static final String TIP_KEY = "tip";

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TableManager.getInstance(getApplicationContext());

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ConsumableActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("consumable").setIndicator(res.getText(R.string.consumables),
                          res.getDrawable(R.drawable.ic_tab_item))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, PersonActivity.class);
        spec = tabHost.newTabSpec("person").setIndicator(res.getText(R.string.persons),
                          res.getDrawable(R.drawable.ic_tab_person))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
    
    @Override
    public void onPause () {
    	super.onPause();
    	
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(TIP_KEY, TableManager.getInstance(this).getTip());
        editor.commit();
    }
    
    @Override
    public void onResume () {
    	super.onResume();
    	
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	int tip = settings.getInt(TIP_KEY, TableManager.DEFAULT_TIP);
    	TableManager.getInstance(this).setTip(tip);
    }
    
}