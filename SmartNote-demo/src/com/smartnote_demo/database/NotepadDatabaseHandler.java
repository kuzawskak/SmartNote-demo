package com.smartnote_demo.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotepadDatabaseHandler extends SQLiteOpenHelper{

	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "notepadsManager";
 
    // Contacts table name
    private static final String TABLE_NOTEPADS = "notepads";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEMPLATE_ID = "template_id";
    private static final String KEY_SITE_ID = "site_id";
 
    public NotepadDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " 
    + TABLE_NOTEPADS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_FILENAME + " TEXT," 
        		+ KEY_TEMPLATE_ID + " INTEGER,"
        		+ KEY_SITE_ID + " INTEGER,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    
    
    
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTEPADS);
         // Create tables again
        onCreate(db);
    }
	
		
	public NotepadDatabaseHandler(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

    // Adding new Notepad
public void addNotepad(Notepad notepad) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, notepad.getFileName()); // Notepad name
    values.put(KEY_TEMPLATE_ID, notepad.getTemplateID());
    values.put(KEY_SITE_ID, notepad.getSiteID());
    values.put(KEY_DATE, notepad.getCreationDate()); // Notepad creation date


//insert row
    db.insert(TABLE_NOTEPADS, null, values);
    db.close(); // Closing database connection
}
	
	
//get notepad by idn in database
public Notepad getNotepad(String name) {
SQLiteDatabase db = this.getReadableDatabase();
Log.v("notepad",String.format("%s",name));
//String selectQuery = "SELECT  * FROM " + TABLE_NOTEPADS;
String query = "SELECT * FROM " +TABLE_NOTEPADS+" WHERE "+KEY_FILENAME+" LIKE '%" +name+ "%'";

Cursor cursor = db.rawQuery(query, null);
if (cursor != null) {
    if(cursor.moveToFirst()) {
    	Log.v("notepad","cursor is not empty");
    }
    else {
    	Log.v("notepad","cursor is empty");
    }
    }
else Log.v("notepad","cursor is null");

if(cursor==null) {

}

Notepad notepad = new Notepad(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)), cursor.getString(4));
return notepad;
}


//Getting All Contacts
public List<Notepad> getAllNotepads() {
    List<Notepad> notepadList = new ArrayList<Notepad>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_NOTEPADS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            Notepad notepad = new Notepad();
            notepad.setID(Integer.parseInt(cursor.getString(0)));
            notepad.setName(cursor.getString(1));
            notepad.setTemplateID(Integer.parseInt(cursor.getString(2)));
            notepad.setSiteID(Integer.parseInt(cursor.getString(3)));
            notepad.setCreationDate(cursor.getString(4));
            // Adding contact to list
            notepadList.add(notepad);
        } while (cursor.moveToNext());
    }

    // return contact list
    return notepadList;
}

// Updating single notepad
public int updateNotepad(Notepad notepad) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, notepad.getFileName());
    values.put(KEY_DATE, notepad.getCreationDate());
    values.put(KEY_SITE_ID,notepad.getSiteID());
    values.put(KEY_TEMPLATE_ID,notepad.getTemplateID());
    // updating row
    return db.update(TABLE_NOTEPADS, values, KEY_ID + " = ?",
            new String[] { String.valueOf(notepad.getID()) });
}

// Deleting single notepad
public void deleteContact(Notepad notepad) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_NOTEPADS, KEY_ID + " = ?",
            new String[] { String.valueOf(notepad.getID()) });
    db.close();
}


// Getting notepads Count
public int getNotepadsCount() {
    String countQuery = "SELECT  * FROM " + TABLE_NOTEPADS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    // return count
    return cursor.getCount();
}

public Cursor GetCursor() {
	    // Select All Query
	SQLiteDatabase db = getReadableDatabase();      
	Cursor cursor =  db.rawQuery( "select rowid _id,* from " + TABLE_NOTEPADS, null);
	    return cursor;
}
}

