package table.organizer.database;

import java.util.ArrayList;
import java.util.List;

import table.organizer.model.Consumable;
import table.organizer.model.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PersistenceManager {

	private static final String DATABASE_NAME = "tableorganizer";
//	private static final String DATABASE_CREATE = //"create table Person(name alalala PRIMARY_KEY NOT NULL UNIQUE);" +  
//			"create table Consumable(id batata PRIMARY_KEY ASC NOT NULL UNIQUE, name text NOT NULL, price integer NOT NULL, quantity integer NOT NULL);" +
//			"create table Consumes(person text, consumable integer, FOREIGN KEY(person) REFERENCES Person(name), FOREIGN KEY(consumable) REFERENCES Consumable(id), UNIQUE(person, consumable)); ";
	private static final String DATABASE_CREATE_PERSON = "create table Person(name text PRIMARY_KEY NOT NULL UNIQUE);";
	private static final String DATABASE_CREATE_CONSUMABLE = "create table Consumable(id integer PRIMARY_KEY ASC NOT NULL UNIQUE, name text NOT NULL, price integer NOT NULL, quantity integer NOT NULL);";
	private static final String DATABASE_CREATE_CONSUMES = "create table Consumes(person text, consumable integer, FOREIGN KEY(person) REFERENCES Person(name), FOREIGN KEY(consumable) REFERENCES Consumable(id), UNIQUE(person, consumable)); ";

	private static final int DATABASE_VERSION = 3;
	
	private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	private Context context;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Log.d("DB", "Criando bancos");
            db.execSQL(DATABASE_CREATE_PERSON);
            db.execSQL(DATABASE_CREATE_CONSUMABLE);
            db.execSQL(DATABASE_CREATE_CONSUMES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS Person");
            onCreate(db);
        }
    }
    
    public PersistenceManager(Context ctx){
    	context = ctx;
    }

    public long createPerson(Person person)
    {
    	ContentValues values = new ContentValues();
    	values.put("name", person.getName());
    
    	return mDb.insert("Person", null, values);
    }
    
    public List<Person> fetchPersons()
    {
    	List<Person> persons = new ArrayList<Person>();
    	
    	//Cursor c = mDb.query("Person", new String [] {"name"}, "name=?",
    	//		new String[] {name}, null, null, null);
    	Cursor c = mDb.query("Person", new String [] {"name"}, 
    			null, null, null, null, null);

    	c.moveToFirst();
    	int size = c.getCount(); 
    	for(int i = 0; i < size; i++, c.moveToNext()){
    		String fetchedName = c.getString(c.getColumnIndex("name"));
    		persons.add(new Person(fetchedName));
    	}
    	
    	return persons;
    }
    
    public void deletePerson(String name){
    	//TODO
    }
    
    public long createConsumable(Consumable consumable){
    	ContentValues values = new ContentValues();
    	values.put("id", consumable.getId());
    	values.put("name", consumable.getName());
    	values.put("price", consumable.getPrice());
    	values.put("quantity", consumable.getQuantity());
    	
    	return mDb.insert("Consumable", null, values);
    }
    
    public List<Consumable> fetchConsumables(){
    	List <Consumable> consumables = new ArrayList<Consumable>();
    	
    	Cursor c = mDb.query("Consumable", new String[] {"id", "name", "price", "quantity"}, 
    			null, null, null, null, null);
    	c.moveToFirst();
    	int size = c.getCount();
    	for (int i = 0; i < size; i++, c.moveToNext()){
    		int id, price, quantity;
    		String name;
    		id = c.getInt(c.getColumnIndex("id"));
    		name = c.getString(c.getColumnIndex("name"));
    		price = c.getInt(c.getColumnIndex("price"));
    		quantity = c.getInt(c.getColumnIndex("quantity"));

    		consumables.add(new Consumable(name, price, quantity, id));
    	}
    	
    	return consumables;
    }
    
    
    public PersistenceManager open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    
}
