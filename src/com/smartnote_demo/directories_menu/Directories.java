package com.smartnote_demo.directories_menu;
import com.example.smartnote_demo.R;
import com.example.smartnote_demo.R.id;
import com.example.smartnote_demo.R.layout;
import com.example.smartnote_demo.R.menu;
import com.smartnote_demo.notepad.NotepadItem;

import android.R.string;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
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
	
	int count =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directories);

		//final Button new_dir_button = (Button)findViewById(R.id.button1);
		LinearLayout layoutInsideScrollview = (LinearLayout)findViewById(R.id.linearLayoutInsideScroll);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		layoutInsideScrollview.setPadding(10, 10, 10, 10);
		for(int i=0;i<20;i++)
		{
			count = i;
			layoutInsideScrollview.addView(createNotePad((int)(height*0.9)));
			String h= ""+height;
			Log.d("height",h);
		}
		

		
		
		
	/*	new_dir_button.setOnClickListener(new View.OnClickListener() {
						
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent create_dir_intent = new Intent(Directories.this,CreateDir.class);
				startActivity(create_dir_intent);
			}
		});
		*/
		layoutInsideScrollview.setOnDragListener(new MyDragListener());
	}




	public DirItem createNotePad(int height){
		DirItem notePadView = new DirItem(this,"filenameaaaaaaaaa"+count,height);

	//	notePadView.setImageResource(R.drawable.notepad);
		
	//	Display display = getWindowManager().getDefaultDisplay();
	//	int height = display.getHeight();// ((display.getHeight()*30)/100)
		
		//int width =(int)( display.getHeight()*1.0f/2.0f); // ((display.getWidth()*20)/100)
		//LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
		//		LayoutParams.WRAP_CONTENT,height);
		//parms.gravity = Gravity.CENTER_HORIZONTAL;
		//notePadView.setLayoutParams(parms);
	notePadView.setPadding(0, 0,0,0);
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

	 class MyDragListener implements OnDragListener {
		    //Drawable enterShape = getResources().getDrawable(R.drawable.notepad);
		   // Drawable normalShape = getResources().getDrawable(R.drawable.notepad);

		    @Override
		    public boolean onDrag(View v, DragEvent event) {
				 // Log.v("motion","before remove");
		      int action = event.getAction();
		      switch (event.getAction()) {
		      case DragEvent.ACTION_DRAG_STARTED:
		        // do nothing
			        Log.v("motion","drag started");
		        break;
		      case DragEvent.ACTION_DRAG_ENTERED:
			        Log.v("motion","drag enetered");
		        break;
		      case DragEvent.ACTION_DRAG_EXITED:
			        Log.v("motion","drag exited");
		        break;
		      case DragEvent.ACTION_DROP:
		    	  View view = (View) event.getLocalState();
		    	 // view.setVisibility(View.GONE);
		    	 // view.setVisibility(View.GONE);
		    	  ((LinearLayout)v).removeView(view);
		  
		    
		        break;
		      case DragEvent.ACTION_DRAG_ENDED:
		    	  
			        Log.v("motion","drag ended");

		      break;

		      default:
		        break;
		      }
		      return true;
		    }
	 }
	
}
