package com.smartnote_demo.directories_menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.smartnote_demo.ExitButtonListener;
import com.example.smartnote_demo.MainActivity;
import com.example.smartnote_demo.R;

	public class TopMenu extends LinearLayout {
		
		private ImageButton ExitButton;
		private Context mContext;
		public TopMenu(Context context, AttributeSet attrs) {
			super(context, attrs);
			mContext = context;
			// TODO Auto-generated constructor stub
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.top_menu, this, true);
		  	
			ExitButton = (ImageButton)itemTemplate.findViewById(R.id.exitButton);

			
			ExitButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(mContext,MainActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    intent.putExtra("Exit me", true);
				    mContext.startActivity(intent);
				    ((Activity) mContext).finish();
					
				}
			});
			
		}
		
		public TopMenu(Context context) {
			super(context);
				mContext = context;
			LinearLayout.LayoutParams params = 
					new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.WRAP_CONTENT);
			
			this.setLayoutParams(params);
			
		  	LayoutInflater inflater = LayoutInflater.from(context);
			View itemTemplate = inflater.inflate(R.layout.bottom_menu, this, true);
		  	
			ExitButton = (ImageButton)itemTemplate.findViewById(R.id.exitButton);
			ExitButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(mContext,MainActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    intent.putExtra("Exit me", true);
				    mContext.startActivity(intent);
				    ((Activity) mContext).finish();
					
				}
			});
		}	
}

