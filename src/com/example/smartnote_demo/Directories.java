package com.example.smartnote_demo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;


public class Directories extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directories);
		//final GridView grid_view = (GridView)findViewById(R.id.gridView1);
		//grid_view.setAdapter(new ButtonAdapter(this));
		
		final Button new_dir_button = (Button)findViewById(R.id.button1);
		new_dir_button.setOnClickListener(new View.OnClickListener() {
		
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent create_dir_intent = new Intent(Directories.this,CreateDir.class);
				startActivity(create_dir_intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.directories, menu);
		return true;
	}

}
