package com.smartnote_demo.directories_menu;


	import android.content.Context;
import android.content.Intent;
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
import com.smartnote_demo.notepad_creator.NotepadCreator;

	public class BottomMenu extends LinearLayout {
		
		public BottomMenu(Context context, AttributeSet attrs) {
			super(context, attrs);
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
