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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

/**
 * 
 * Activity with saved notepads loaded from database
 * (each item is reacting on long press and click 
 * long click - to show context menu
 * click  - to open the notepad to edit)
 *
 */
public class Directories extends Activity implements OnItemClickListener {
	
	int count =0;
	private DirItem mSelectedDir;
	//layout inside ScrollView with directories (notepads)
	private LinearLayout mDirectoriesContainer;
	private NotepadDatabaseHandler notepads_handler;
	private SiteDatabaseHandler sites_handler;
	
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
		//to be used on constructor  - to dynamically set item size for screen
		int width = size.x;
		int height = size.y;
				
		if(notepads.isEmpty()) {
			Toast.makeText(this, "No notepads created yet.",Toast.LENGTH_SHORT).show();
		}
		
		
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


	}

	
	   @Override  
	    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	    super.onCreateContextMenu(menu, v, menuInfo);  
	    	mSelectedDir = (DirItem)v;	    	
	        menu.setHeaderTitle(mSelectedDir.getName()); 
	        menu.add(0, v.getId(), 0, "Delete");  
	        menu.add(0, v.getId(), 0, "Info");  
	    } 
	
	   	   
	   @Override  
	    public boolean onContextItemSelected(MenuItem item) {  
	        if(item.getTitle()=="Delete")
	        {//delete notepad;
	        	 deleteNotepad();       	 	        	 
	        }
	        else if(item.getTitle()=="Info"){
	        	//open notepad settings
	        	showInfo();
	        	//show toast with info
	        }  
	        else {return false;}  
	    return true;  
	    }  

	public DirItem createNotePad(int height,int skin_id, int site_id, String name,int database_id){
		DirItem notePadView = new DirItem(this,"filename",height,skin_id,site_id,name,database_id);
		return notePadView;		
	}
		
	public void showInfo() {
		String creation_date;
		int sites_count ;
		String name;
		if(mSelectedDir!=null) {
			name=mSelectedDir.getName();
			notepads_handler = new NotepadDatabaseHandler(this);
			Notepad current = notepads_handler.getNotepad(name);
			creation_date = current.getCreationDate();
			sites_handler = new SiteDatabaseHandler(this);
			sites_count = sites_handler.getAllSitesFromNotepad(name).size();
		} 
		else
		{
			name = "unknown";
			creation_date = "unknown";
			sites_count = 0;
		}
		
		String toast_text = String.format
				("Name: %s \n Creation date: %s \n Sites: %d  ",
						name,creation_date,sites_count);
		Toast info_toast = Toast.makeText(this, toast_text, Toast.LENGTH_LONG);
		info_toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		info_toast.show();
		
	}
	
	public void deleteNotepad() {
		notepads_handler = new NotepadDatabaseHandler(this);
   	 	notepads_handler.deleteNotepad(mSelectedDir.getName());
   	 
   	 	sites_handler = new SiteDatabaseHandler(this);
   	 	sites_handler.deleteNotepadContent(mSelectedDir.getName());
   	
   	 	mDirectoriesContainer.removeView(mSelectedDir);	 
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(notepads_handler!=null) {
			notepads_handler.close();
		}
		if(sites_handler!=null) {
			sites_handler.close();
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(notepads_handler!=null) {
		notepads_handler.close();
		}
		if(sites_handler!=null) {
		sites_handler.close();
		}
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
