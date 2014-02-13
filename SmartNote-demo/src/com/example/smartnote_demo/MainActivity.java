package com.example.smartnote_demo;

import com.smartnote_demo.carouselmenu.Carousel;
import com.smartnote_demo.carouselmenu.CarouselAdapter;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemClickListener;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemSelectedListener;
import com.smartnote_demo.directories_menu.Directories;
import com.smartnote_demo.images.GalleryActivity;
import com.smartnote_demo.quick_note.CanvasActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ImageButton ExitButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
   
        setContentView(R.layout.activity_main);
        
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }
        
        Carousel carousel = (Carousel)findViewById(R.id.carousel);
        
        ExitButton = (ImageButton)findViewById(R.id.exitButton);                
        ExitButton.setOnClickListener(new ExitButtonListener());
                       
        carousel.setOnItemClickListener(new OnItemClickListener(){
       
			public void onItemClick(CarouselAdapter<?> parent, View view,
					int position, long id) {	

				switch(position) {
				case 0:
					/***NOTEPADS***/
					Intent directories_intent = new Intent(MainActivity.this,Directories.class);
					startActivity(directories_intent);	
					break;					
				case 1:
					/***PICTURES***/
					Intent images_intent = new Intent(MainActivity.this,GalleryActivity.class);
					GalleryActivity.only_preview = true;
					startActivity(images_intent);
					break;
				case 2:
					/***QUICK NOTES***/
					Intent canvas_intent = new Intent(MainActivity.this,CanvasActivity.class);			
					startActivity(canvas_intent);					
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
					//Notepads
					Toast.makeText(MainActivity.this,"Check your notepads", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					//Gallery
					Toast.makeText(MainActivity.this,"Check and import images", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					//Quick notes
					Toast.makeText(MainActivity.this,"Create quick note", Toast.LENGTH_SHORT).show();
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

