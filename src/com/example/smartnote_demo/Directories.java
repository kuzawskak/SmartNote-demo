package com.example.smartnote_demo;
import com.smartnote_demo.notepad.NotepadItem;

import android.R.string;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;






public class Directories extends Activity implements OnItemClickListener {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directories);
		//final GridView grid_view = (GridView)findViewById(R.id.gridView1);
		//grid_view.setAdapter(new ButtonAdapter(this));
		
		final Button new_dir_button = (Button)findViewById(R.id.button1);
		
		// GridView gridview = (GridView) findViewById(R.id.gridView1);
	//	    gridview.setAdapter(new ImageAdapter(this));

		LinearLayout layoutInsideScrollview = (LinearLayout)findViewById(R.id.linearLayoutInsideScroll);
	
	//layoutInsideScrollview.addView(imageView,0);

		layoutInsideScrollview.setPadding(10, 10, 10, 10);
		for(int i=0;i<20;i++)
		{
			layoutInsideScrollview.addView(createNotePad());
		}
		
		
		new_dir_button.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent create_dir_intent = new Intent(Directories.this,CreateDir.class);
				startActivity(create_dir_intent);
			}
		});
	}




	public DirItem createNotePad(){
		DirItem notePadView = new DirItem(this,"filename");

	//	notePadView.setImageResource(R.drawable.notepad);
		
	//	Display display = getWindowManager().getDefaultDisplay();
	//	int height = display.getHeight();// ((display.getHeight()*30)/100)
		
		//int width =(int)( display.getHeight()*1.0f/2.0f); // ((display.getWidth()*20)/100)
		//LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
		//		LayoutParams.WRAP_CONTENT,height);
		//parms.gravity = Gravity.CENTER_HORIZONTAL;
		//notePadView.setLayoutParams(parms);
	notePadView.setPadding(100, 0,100,0);
		return notePadView;		
	}
	
	private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directories, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
