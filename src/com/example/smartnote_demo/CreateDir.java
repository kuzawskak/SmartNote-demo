package com.example.smartnote_demo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class CreateDir extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_dir);
		
		final Button ok_button = (Button)findViewById(R.id.button_ok);
		final TextView dir_name_textview = (TextView)findViewById(R.id.textView1);
		
		final Context context = this;
		
ok_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
	
			//add new button to MainActivity
				//Button new_folder_button = new Button(context);
				//new_folder_button.setText(dir_name_textview.getText());
				
				
			//GridView grid_view = (GridView)findViewById(R.id.gridView1);
	
			//grid_view.addView(new_folder_button);
			//go to previous activity
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
