package com.smartnote_demo.directories_menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.smartnote_demo.R;

	public class TopMenu extends LinearLayout {
		
		private ImageButton ExitButton;
		
		public TopMenu(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.top_menu, this, true);
		  	
			ExitButton = (ImageButton)itemTemplate.findViewById(R.id.exitButton);

		}
		
		public TopMenu(Context context) {
			super(context);
				
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.bottom_menu, this, true);
		  	
			ExitButton = (ImageButton)itemTemplate.findViewById(R.id.exitButton);

		}	
}

