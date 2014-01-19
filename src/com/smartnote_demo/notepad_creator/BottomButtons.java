package com.smartnote_demo.notepad_creator;


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

public class BottomButtons extends LinearLayout {
	
	public BottomButtons(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.bottom_buttons, this, true);
	  	
		CancelButton = (ImageButton)itemTemplate.findViewById(R.id.createButton);
		
		CancelButton.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {
					

				
			}
		});
		
		CreateButton = (ImageButton)itemTemplate.findViewById(R.id.cancelButton);
		
		
		CreateButton.setOnClickListener(new OnClickListener() {
		
			
			@Override
			public void onClick(View v) {
					

				
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
