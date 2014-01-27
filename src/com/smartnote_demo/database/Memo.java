package com.smartnote_demo.database;

import java.util.Date;

public class Memo {
	
	//private variables
	int _id;
	String _filename;
	String _creation_date;
	
	//Empty contructor
	public Memo() {
		
	}
	
	//constructor
	 public Memo(int id, String filename, String creation_date){
	        this._id = id;
	        this._filename = filename;
	        this._creation_date = creation_date;
	    }
	//constructor
	 public Memo( String filename, String creation_date) {
		        this._filename = filename;
		        this._creation_date = creation_date;
		    }
	//getting ID
	public int getID(){
			return this._id;
		}
 
	// setting id
	public void setID(int id){
			this._id = id;
		}
 
	// getting filename
	public String getFileName(){
			return this._filename;
		}
 
	// setting filename
	public void setName(String filename){
			this._filename = filename;
		}
 
	// getting creation date
	public String getCreationDate(){
			return this._creation_date;
		}
 
	// setting creation date
	public void setCreationDate(String creation_date){
			this._creation_date = creation_date;
		}
}



