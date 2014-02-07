package com.smartnote_demo.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SiteDatabaseHandler extends SQLiteOpenHelper{

	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "sitesManager";
 
    // Sites table name
    private static final String TABLE_SITES = "sites";
 
    // Sites Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NOTEPAD_ID = "notepad_id";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_SITE_NUMBER = "site_number";
 
    public SiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SITES_TABLE = "CREATE TABLE " 
    + TABLE_SITES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_NOTEPAD_ID + "INTEGER," 
                + KEY_FILENAME + " TEXT," 
        		+ KEY_SITE_NUMBER + " INTEGER" +")";
 
        db.execSQL(CREATE_SITES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITES);
         // Create tables again
        onCreate(db);
    }
	
		
	public SiteDatabaseHandler(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

    // Adding new Notepad
public void addSite(Site site) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, site.getFileName()); // Notepad name
    values.put(KEY_NOTEPAD_ID, site.getNotepadID());
    values.put(KEY_SITE_NUMBER, site.getSiteNumber());
//insert row
    db.insert(TABLE_SITES, null, values);
    db.close(); // Closing database connection
}
	
	
//get notepad by idn in database
public Site getSite(int id) {
SQLiteDatabase db = this.getReadableDatabase();

Cursor cursor = db.query(TABLE_SITES, new String[] { KEY_ID,KEY_NOTEPAD_ID,
        KEY_FILENAME, KEY_SITE_NUMBER }, KEY_ID + "=?",
        new String[] { String.valueOf(id) }, null, null, null, null);
if (cursor != null)
    cursor.moveToFirst();

Site site = new Site(Integer.parseInt(cursor.getString(0)),
        Integer.parseInt(cursor.getString(1)),cursor.getString(2),Integer.parseInt(cursor.getString(3)));
// return site
return site;
}




//Getting All Sites
public List<Site> getAllSitesFromNotepad(int notepad_id) {
    List<Site> siteList = new ArrayList<Site>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_SITES;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()&& Integer.parseInt(cursor.getString(1))==notepad_id) {
        do {
            Site site = new Site();
            site.setID(Integer.parseInt(cursor.getString(0)));
            site.setNotepadID(Integer.parseInt(cursor.getString(1)));
            site.setFileName(cursor.getString(2));
            site.setSiteNumber(Integer.parseInt(cursor.getString(3)));
            // Adding contact to list
            siteList.add(site);
        } while (cursor.moveToNext());
    }

    // return site list
    return siteList;
}

// Updating single site
public int updateSite(Site site) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, site.getFileName());
    values.put(KEY_NOTEPAD_ID, site.getNotepadID());
    values.put(KEY_SITE_NUMBER, site.getSiteNumber());
    // updating row
    return db.update(TABLE_SITES, values, KEY_ID + " = ?",
            new String[] { String.valueOf(site.getID()) });
}

// Deleting single site
public void deleteContact(Site site) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_SITES, KEY_ID + " = ?",
            new String[] { String.valueOf(site.getID()) });
    db.close();
}


// Getting sites Count
public int getNotepadsCount() {
    String countQuery = "SELECT  * FROM " + TABLE_SITES;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    // return count
    return cursor.getCount();
}

public Cursor GetCursor() {
	    // Select All Query
	SQLiteDatabase db = getReadableDatabase();      
	Cursor cursor =  db.rawQuery( "select rowid _id,* from " + TABLE_SITES, null);
	    return cursor;
}
}

