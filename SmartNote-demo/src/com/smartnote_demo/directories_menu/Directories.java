package com.smartnote_demo.directories_menu;
import java.util.List;

import com.example.smartnote_demo.R;
import com.smartnote_demo.database.Notepad;
import com.smartnote_demo.database.NotepadDatabaseHandler;
import com.smartnote_demo.database.SiteDatabaseHandler;

import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;


public class Directories extends Activity implements OnItemClickListener {
	
	int count =0;
	private String mSelectedName;
	private DirItem mSelectedDir;
	//layout inside ScrollView with directories (notepads)
	private LinearLayout mDirectoriesContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directories);

        //getting all saved notepads from database        
        NotepadDatabaseHandler db = new NotepadDatabaseHandler(this);
        List<Notepad> notepads = db.getAllNotepads();      
                       
		mDirectoriesContainer = (LinearLayout)findViewById(R.id.linearLayoutInsideScroll);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		mDirectoriesContainer.setPadding(10, 10, 10, 10);
		
        for (Notepad n : notepads) {
            int skin_id = n.getTemplateID();
            int site_id = n.getSiteID();
            String  name = n.getFileName();
            int database_id = n.getID();
            String log = 
            		"Id: "+ database_id
            		+" ,Name: " + skin_id
            		+", template id:" + site_id
            		+ ", site id: "+ name;

					DirItem dir= createNotePad((int)(height*0.9),skin_id,site_id,name,database_id);
					mDirectoriesContainer.addView(dir);
					registerForContextMenu(dir);
        Log.d("directories", log);
        }
//		for(int i=0;i<20;i++)
//		{
//			count = i;
//			layoutInsideScrollview.addView(createNotePad((int)(height*0.9),skin_id,site_id));
//			String h= ""+height;
//			Log.d("height",h);
//		}
		

		
		
		
	/*	new_dir_button.setOnClickListener(new View.OnClickListener() {
						
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent create_dir_intent = new Intent(Directories.this,CreateDir.class);
				startActivity(create_dir_intent);
			}
		});
		*/
        
        
        
		//mDirectoriesContainer.setOnDragListener(new MyDragListener());
		
	
	}



	
	   @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	    super.onCreateContextMenu(menu, v, menuInfo);  
	    	mSelectedDir = (DirItem)v;
	    	
	        menu.setHeaderTitle(mSelectedDir.getName()); 
	        menu.add(0, v.getId(), 0, "Delete");  
	        menu.add(0, v.getId(), 0, "Settings");  
	    } 
	
	   
	   
	   
	   @Override  
	    public boolean onContextItemSelected(MenuItem item) {  
	        if(item.getTitle()=="Delete")
	        {//delete notepad;
	        	 NotepadDatabaseHandler db = new NotepadDatabaseHandler(this);
	        	 db.deleteNotepad(mSelectedDir.getName());
	        	 db.close();
	        	 SiteDatabaseHandler sites_db = new SiteDatabaseHandler(this);
	        	 sites_db.deleteNotepadContent(mSelectedDir.getName());
	        	 sites_db.close();
	        	 mDirectoriesContainer.removeView(mSelectedDir);	        	 	        	 
	        }
	        else if(item.getTitle()=="Settings"){
	        	//open notepad settings
	        	//load Creator
	        }  
	        else {return false;}  
	    return true;  
	    }  

	public DirItem createNotePad(int height,int skin_id, int site_id, String name,int database_id){
		DirItem notePadView = new DirItem(this,"filename",height,skin_id,site_id,name,database_id);

	//	notePadView.setImageResource(R.drawable.notepad);
		
	//	Display display = getWindowManager().getDefaultDisplay();
	//	int height = display.getHeight();// ((display.getHeight()*30)/100)
		
		//int width =(int)( display.getHeight()*1.0f/2.0f); // ((display.getWidth()*20)/100)
		//LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
		//		LayoutParams.WRAP_CONTENT,height);
		//parms.gravity = Gravity.CENTER_HORIZONTAL;
		//notePadView.setLayoutParams(parms);
	//notePadView.setPadding(0, 0,0,0);
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
