package com.smartnote_demo.notepad_creator;


import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartnote_demo.MainActivity;
import com.example.smartnote_demo.R;
import com.smartnote_demo.database.Memo;
import com.smartnote_demo.database.MemoDatabaseHandler;
import com.smartnote_demo.database.Notepad;
import com.smartnote_demo.database.NotepadDatabaseHandler;
import com.smartnote_demo.notepad_creator.NotepadCreator;

public class BottomButtons extends LinearLayout {
	
	private Context mContext;
	
	
	public BottomButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.bottom_buttons, this, true);
	  	
		CancelButton = (ImageButton)itemTemplate.findViewById(R.id.cancelButton);
		
		CancelButton.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {
					

				
			}
		});
		
		CreateButton = (ImageButton)itemTemplate.findViewById(R.id.createButton);
		
		
		CreateButton.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {
					Log.v("button", "create button clicked");
				 SharedPreferences sp = mContext.getSharedPreferences("NEW_NOTEPAD", 0);
				    String name = sp.getString("name", "new_notepad");
				   
				    int site_id = sp.getInt("site_id",0);
				    Log.v("directories",""+site_id);
				    int template_id = sp.getInt("template_id",0);
				    Log.v("directories",""+template_id);
				    String current_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
				    Notepad new_notepad = new Notepad(name,template_id,site_id,current_date);
				    NotepadDatabaseHandler db = new NotepadDatabaseHandler(mContext);
				  	Log.d("Insert: ", "Inserting ...");	 
			        db.addNotepad(new_notepad);
			        Log.d("Insert",name + "inserted ");
			    
				
			}
		});
		
	}
	
	public BottomButtons(Context context) {
		super(context);
		
		
		
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.bottom_buttons, this, true);
	  	
		CancelButton = (ImageButton)itemTemplate.findViewById(R.id.createButton);
		CreateButton = (ImageButton)itemTemplate.findViewById(R.id.cancelButton);
		
		CancelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go back (close the activity)and do nothing
			}
		});
		
		CreateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//add the notepad to database and reload the previous (directory activity)
				
			}
		});
				
	}	
	
	private ImageButton CancelButton;
	private ImageButton CreateButton;
	
}
