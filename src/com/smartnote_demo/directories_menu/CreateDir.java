package com.smartnote_demo.directories_menu;

import com.example.smartnote_demo.ButtonAdapter;
import com.example.smartnote_demo.R;
import com.example.smartnote_demo.R.id;
import com.example.smartnote_demo.R.layout;
import com.example.smartnote_demo.R.menu;
import com.smartnote_demo.notepad.NotepadItem;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class CreateDir extends Activity {

	NotepadItem[] notepads =new NotepadItem[10];
	ButtonAdapter adapter = new ButtonAdapter(this, notepads);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_dir);
		
		final Button ok_button = (Button)findViewById(R.id.button_ok);
		final TextView dir_name_textview = (TextView)findViewById(R.id.textView1);
		Log.v("tag"," notepad create");
		final Context context = this;
		Log.v("tag"," notepad create");
		notepads[0] = new NotepadItem(context);
	//	GridView grid_view = (GridView)findViewById(R.id.gridView1);
		Log.v("tag"," notepad create");
		if(adapter==null) Log.v("tag","null");
		//grid_view.setAdapter(adapter);
		Log.v("tag"," notepad create");
		
		
ok_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
	
		Log.v("tag"," notepad create");
			NotepadItem new_notepad = new NotepadItem(context);
			Log.v("tag"," notepad create");
			notepads[1] = new_notepad;
		//	GridView grid_view = (GridView)findViewById(R.id.gridView1);
			Log.v("tag"," notepad create");
			adapter.AddItem(new_notepad);
			Log.v("tag"," notepad create");
		//grid_view.setAdapter(adapter);
			
			//go to previous activity
				
				//add new item to directories_activity
				
				
			finish();

				
			}
		});
		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_dir, menu);
		return true;
	}

}
