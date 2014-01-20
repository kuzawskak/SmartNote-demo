package com.smartnote_demo.notepad;

import com.example.smartnote_demo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


public class NotepadItem extends FrameLayout{

	private TextView mText;
	
	
	public NotepadItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_item, this, true);
	  	//
		//mText = (TextView)itemTemplate.findViewById(R.id.notepad_title_textview);
	}

}
