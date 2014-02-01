package com.smartnote_demo.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDatabaseHandler extends SQLiteOpenHelper{

	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "memosManager";
 
    // Contacts table name
    private static final String TABLE_MEMOS = "memos";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_DATE = "date";
 
    public MemoDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MEMOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FILENAME + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMOS);
         // Create tables again
        onCreate(db);
    }
	
	
		
	
	public MemoDatabaseHandler(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}


	

    // Adding new memo
public void addMemo(Memo memo) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, memo.getFileName()); // filename
    values.put(KEY_DATE, memo.getCreationDate()); // creation date
 
    // Inserting Row
    db.insert(TABLE_MEMOS, null, values);
    db.close(); // Closing database connection
}
	
	
//getMemo()
// Getting single memo
public Memo getMemo(int id) {
SQLiteDatabase db = this.getReadableDatabase();

Cursor cursor = db.query(TABLE_MEMOS, new String[] { KEY_ID,
        KEY_FILENAME, KEY_DATE }, KEY_ID + "=?",
        new String[] { String.valueOf(id) }, null, null, null, null);
if (cursor != null)
    cursor.moveToFirst();

Memo memo = new Memo(Integer.parseInt(cursor.getString(0)),
        cursor.getString(1), cursor.getString(2));
// return memo
return memo;
}


//Getting All Memos
public List<Memo> getAllMemos() {
    List<Memo> memoList = new ArrayList<Memo>();
    // Select All Query
    String selectQuery = "SELECT  * FROM " + TABLE_MEMOS;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
        do {
            Memo memo = new Memo();
            memo.setID(Integer.parseInt(cursor.getString(0)));
            memo.setName(cursor.getString(1));
            memo.setCreationDate(cursor.getString(2));
            // Adding contact to list
            memoList.add(memo);
        } while (cursor.moveToNext());
    }

    // return memo list
    return memoList;
}

// Updating single memo
public int updateMemo(Memo memo) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(KEY_FILENAME, memo.getFileName());
    values.put(KEY_DATE, memo.getCreationDate());

    // updating row
    return db.update(TABLE_MEMOS, values, KEY_ID + " = ?",
            new String[] { String.valueOf(memo.getID()) });
}

// Deleting single memo
public void deleteMemo(Memo memo) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_MEMOS, KEY_ID + " = ?",
            new String[] { String.valueOf(memo.getID()) });
    db.close();
}


// Getting memos Count
public int getMemosCount() {
    String countQuery = "SELECT  * FROM " + TABLE_MEMOS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    // return count
    return cursor.getCount();
}

public Cursor GetCursor() {
	    // Select All Query
	SQLiteDatabase db = getReadableDatabase();      
	Cursor cursor =  db.rawQuery( "select rowid _id,* from " + TABLE_MEMOS, null);
	    return cursor;
}
}

