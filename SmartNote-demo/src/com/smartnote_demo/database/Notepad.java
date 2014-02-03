package com.smartnote_demo.database;

import java.util.Date;

public class Notepad {
	
	//private variables
	int _id;
	String _filename;
	int _template_id;
	int _site_id;
	String _creation_date;
	
	//Empty contructor
	public Notepad () {
		
	}
	
	//constructor
	 public Notepad (int id, String filename, int template_id,int site_id,String creation_date){
	        this._id = id;
	        this._filename = filename;
	        this._template_id = template_id;
	        this._site_id = site_id;
	        this._creation_date = creation_date;
	    }
	//constructor
	 public Notepad ( String filename, int template_id,int site_id,String creation_date) {
		        this._filename = filename;
		        this._template_id = template_id;
		        this._site_id = site_id;
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
	
	
	//getting template_id
		public int getTemplateID(){
				return this._template_id;
			}
	 
		// setting id
		public void setTemplateID(int template_id){
				this._template_id = template_id;
			}
	
		//getting site_ID
		public int getSiteID(){
				return this._site_id;
			}
	 
		// setting site_id
		public void setSiteID(int site_id){
				this._site_id = site_id;
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



