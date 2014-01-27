package com.smartnote_demo.directories_menu;


	import java.util.Calendar;

import android.R.string;
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
import com.smartnote_demo.database.Notepad;
import com.smartnote_demo.database.NotepadDatabaseHandler;
import com.smartnote_demo.notepad_creator.NotepadCreator;

	public class BottomMenu extends LinearLayout {
		private Context mContext;
		public BottomMenu(Context context, AttributeSet attrs) {
			super(context, attrs);
			mContext = context;
			// TODO Auto-generated constructor stub
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.bottom_menu, this, true);
		  	
			HomeButton = (ImageButton)itemTemplate.findViewById(R.id.homeButton);
			CreateItemButton = (ImageButton)itemTemplate.findViewById(R.id.createItemButton);
			
			
			CreateItemButton.setOnClickListener(new OnClickListener() {
			
				
				@Override
				public void onClick(View v) {
					Intent create_notepad_intent = new Intent(v.getContext(),NotepadCreator.class);
					v.getContext().startActivity(create_notepad_intent);
					SharedPreferences sp  = mContext.getSharedPreferences("NEW_NOTEPAD", 0);			        
			        int site_id = sp.getInt("site_id", 0);
			        int template_id = sp.getInt("template_id", 0);
			        String name = sp.getString("name", "new_notepad");
			        String current_date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			        NotepadDatabaseHandler db = new NotepadDatabaseHandler(mContext);
			        db.addNotepad(new Notepad(name,template_id,site_id,current_date));
				}
			});
			
		}
		
		public BottomMenu(Context context) {
			super(context);
			
			
			
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.bottom_menu, this, true);
		  	
			HomeButton = (ImageButton)itemTemplate.findViewById(R.id.homeButton);
			CreateItemButton = (ImageButton)itemTemplate.findViewById(R.id.createItemButton);
					
		}	
		
		private ImageButton HomeButton;
		private ImageButton CreateItemButton;
		
		
		
			
	}
