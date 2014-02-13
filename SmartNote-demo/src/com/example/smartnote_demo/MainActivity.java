package com.example.smartnote_demo;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.smartnote_demo.carouselmenu.Carousel;
import com.smartnote_demo.carouselmenu.CarouselAdapter;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemClickListener;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemSelectedListener;
import com.smartnote_demo.carouselmenu.CarouselItem;
import com.smartnote_demo.directories_menu.Directories;
import com.smartnote_demo.events_menu.Calendar;
import com.smartnote_demo.images.GalleryActivity;
import com.smartnote_demo.quick_note.CanvasActivity;
import com.smartnote_demo.database.MemoDatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageButton ExitButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Carousel carousel = (Carousel)findViewById(R.id.carousel);
        
        ExitButton = (ImageButton)findViewById(R.id.exitButton);                
        ExitButton.setOnClickListener(new ExitButtonListener());
                       
        carousel.setOnItemClickListener(new OnItemClickListener(){
       
			public void onItemClick(CarouselAdapter<?> parent, View view,
					int position, long id) {	

				switch(position) {
				case 0:
					/***EVENTS***/
					Intent events_intent = new Intent(MainActivity.this,Calendar.class);	
					startActivity(events_intent);
					break;					
				case 1:
					/***NOTEPADS***/
					Intent directories_intent = new Intent(MainActivity.this,Directories.class);
					startActivity(directories_intent);	
					break;					
				case 2:
					/***OTHERS***/
					break;					
				case 3:
					/***PICTURES***/
					Intent images_intent = new Intent(MainActivity.this,GalleryActivity.class);
					startActivity(images_intent);
					break;
				case 4:
					/***QUICK NOTES***/
					Intent canvas_intent = new Intent(MainActivity.this,CanvasActivity.class);			
					startActivity(canvas_intent);					
					break;
				case 5:
					/***RECORDS***/					
					break;					
				}
		
			}		
        });

        carousel.setOnItemSelectedListener(new OnItemSelectedListener(){
       	
        	//show toast with functionality of selected item
			public void onItemSelected(CarouselAdapter<?> parent, View view,
					int position, long id) {

				switch(position){
				case 0:
					//Events 						
					Toast.makeText(MainActivity.this,"Check events", Toast.LENGTH_SHORT).show();								
					break;
				case 1:
					//Notepads
					Toast.makeText(MainActivity.this,"Check your notepads", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					//Others
					Toast.makeText(MainActivity.this,"Others - not implemented yet", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					//Gallery
					Toast.makeText(MainActivity.this,"Check and import images", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					//Quick notes
					Toast.makeText(MainActivity.this,"Create quick note", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					//Records
					Toast.makeText(MainActivity.this,"Check and import records", Toast.LENGTH_SHORT).show();
					break;					
				}				
			}

			public void onNothingSelected(CarouselAdapter<?> parent) {
				
			}
        	
        }
        );
        
    }
    
   

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    }
}

