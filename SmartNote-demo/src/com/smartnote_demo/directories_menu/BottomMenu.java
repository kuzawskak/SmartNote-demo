package com.smartnote_demo.directories_menu;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.smartnote_demo.R;
import com.smartnote_demo.notepad_creator.NotepadCreator;

	public class BottomMenu extends LinearLayout {
		private Context mContext;
		private ImageButton CreateItemButton;
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
		  	
			CreateItemButton = (ImageButton)itemTemplate.findViewById(R.id.createItemButton);
						
			CreateItemButton.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {

					Intent create_notepad_intent = new Intent(v.getContext(),NotepadCreator.class);
					v.getContext().startActivity(create_notepad_intent);
					
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
		  	
			
			CreateItemButton = (ImageButton)itemTemplate.findViewById(R.id.createItemButton);
					
		}	
		

			
			
	}
