package com.smartnote_demo.notepad_creator;

import com.example.smartnote_demo.R;
import com.smartnote_demo.directories_menu.DirItem;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class NotepadCreator extends Activity {
	

	private LinearLayout NotepadSkins;
	private LinearLayout SiteSkins;
	private int[] NotepadSkinIds={
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
	};
	
	private int[] SiteSkinIds={
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
			R.drawable.notepad,
	};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	        setContentView(R.layout.notepad_creator);
	        
	        NotepadSkins = (LinearLayout)findViewById(R.id.NotepadSkinsContainer);
	        SiteSkins = (LinearLayout)findViewById(R.id.NotepadSitesContainer);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			
			NotepadSkins.setPadding(10, 10, 10, 10);
			SiteSkins.setPadding(10, 10, 10, 10);
			for(int i=0;i<NotepadSkinIds.length;i++)
			{		
				NotepadSkins.addView(createNotePad((int)(height*0.9),NotepadSkinIds[i]));
				String h= ""+height;
				Log.d("height",h);
			}
			
			for(int i=0;i<SiteSkinIds.length;i++)
			{	
				SiteSkins.addView(createNotePad((int)(height*0.9),SiteSkinIds[i]));
				String h= ""+height;
				Log.d("height",h);
			}
				
	
	}
	public NotepadItem createNotePad(int height,int id){
		NotepadItem notePadView = new NotepadItem(this,"skin",height,id);

		return notePadView;		
	}
	



}

