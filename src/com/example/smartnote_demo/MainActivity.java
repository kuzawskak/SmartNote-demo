package com.example.smartnote_demo;

import com.smartnote_demo.carouselmenu.Carousel;
import com.smartnote_demo.carouselmenu.CarouselAdapter;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemClickListener;
import com.smartnote_demo.carouselmenu.CarouselAdapter.OnItemSelectedListener;
import com.smartnote_demo.carouselmenu.CarouselItem;
import com.smartnote_demo.directories_menu.Directories;

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
				
				Toast.makeText(MainActivity.this, 
						String.format("%s has been clicked", 
						((CarouselItem)parent.getChildAt(position)).getName()), 
						Toast.LENGTH_SHORT).show();				
			}		
        });

        carousel.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(CarouselAdapter<?> parent, View view,
					int position, long id) {
				
		   //     final TextView txt = (TextView)(findViewById(R.id.selected_item));
		        
				switch(position){
				case 0:
					//txt.setText("Events chosen - show calendar");
					break;
				case 1:
					//txt.setText("Notes chosen - show folders");
					Intent directories_intent = new Intent(MainActivity.this,Directories.class);
					startActivity(directories_intent);
					
					break;
				case 2:
					//txt.setText("Others chosen - ...");
					break;
				case 3:
					//txt.setText("Pictures chosen - show gallery");
					break;
				case 4:
				//	txt.setText("Quick notes chosen - show panel with canvas");
					Log.e("smart","1");
					Intent canvas_intent = new Intent(MainActivity.this,CanvasActivity.class);			
					Log.e("smart","failure 2");
					//else
						
					startActivity(canvas_intent);
					Log.e("smart","3");
					break;
				case 5:
					//txt.setText("Records chosen  -show folder with records");
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

