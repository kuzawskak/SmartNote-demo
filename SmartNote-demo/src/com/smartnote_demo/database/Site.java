package com.smartnote_demo.database;

import java.util.Date;

public class Site {
	
	//private variables
	int _id;
	int _notepad_id;
	String _filename;
	int _site_number;

	
	//Empty contructor
	public Site () {
		
	}
	
	//constructor
	 public Site (int id,int notepad_id ,String filename, int site_number){
	        this._id = id;
	        this._notepad_id = notepad_id;
	        this._filename = filename;
	        this._site_number = site_number;
	    }
	//constructor
	 public Site ( int notepad_id ,String filename, int site_number) {
	        this._notepad_id = notepad_id;
	        this._filename = filename;
	        this._site_number = site_number;
		    }
	//getting ID
	public int getID(){
			return this._id;
		}
 
	// setting id
	public void setID(int id){
			this._id = id;
		}
	
	
	//getting template_id
		public int getNotepadID(){
				return this._notepad_id;
			}
	 
		// setting id
		public void setNotepadID(int notepad_id){
				this._notepad_id = notepad_id;
			}
	
		//getting site_number
		public int getSiteNumber(){
				return this._site_number;
			}
	 
		// setting site_id
		public void setSiteNumber(int site_number){
				this._site_number = site_number;
			}
 
	// getting filename
	public String getFileName(){
			return this._filename;
		}
 
	// setting filename
	public void setFileName(String filename){
			this._filename = filename;
		}
 

}



