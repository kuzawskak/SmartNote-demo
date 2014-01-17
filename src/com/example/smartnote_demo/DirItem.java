
package com.example.smartnote_demo;

import com.example.smartnote_demo.R;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.DragEvent;
import android.graphics.drawable.Drawable;

public class DirItem extends FrameLayout {

	private ImageView mImage;
	private TextView mText;
	
	private int index;
	private float itemX;
	private float itemY;
	private boolean drawn;	

	// It's needed to find screen coordinates
	private Matrix mCIMatrix;
	
	public DirItem(Context context,String filename,int height) {
		
		super(context);
		
		
		
		
		/*FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(100,200);
						//LayoutParams.WRAP_CONTENT, 
						//LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		*/
		//this.setBackgroundColor(Color.BLACK);
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_dir, this, true);
	  	
		
		mImage = (ImageView)itemTemplate.findViewById(R.id.item_image);
		
		mText = (TextView)itemTemplate.findViewById(R.id.item_text);
		mText.setText(filename);
		
	    this.setOnTouchListener(new MyTouchListener());
	
	   
				
	}	
	
	public String getName(){
		return mText.getText().toString();
	}	
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	

	public void setItemX(float x) {
		this.itemX = x;
	}

	public float getItemX() {
		return itemX;
	}

	public void setItemY(float y) {
		this.itemY = y;
	}

	public float getItemY() {
		return itemY;
	}



	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public boolean isDrawn() {
		return drawn;
	}
	
	public void setImageBitmap(Bitmap bitmap){
		mImage.setImageBitmap(bitmap);
		
	}
	
	public void setText(String txt){
		mText.setText(txt);
	}
	
	Matrix getCIMatrix() {
		return mCIMatrix;
	}

	void setCIMatrix(Matrix mMatrix) {
		this.mCIMatrix = mMatrix;
	}

	private final class MyTouchListener implements OnTouchListener {
		boolean start  = false;
    	float x1, x2, y1, y2;
	    public boolean onTouch(View view, MotionEvent motionEvent) {
	    	

	    	

	      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	    	 Log.v("motion","action down");
	    	  x1=motionEvent.getX();
	    	  y1 = motionEvent.getY();
	       // view.setVisibility(View.INVISIBLE);
	        return true;
	      } 
	      else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
	    	  {
	    	  x2 = motionEvent.getX();
	    	  y2 = motionEvent.getY();
	    	  if(Math.abs(y2-y1)>100 && start == false)
	    		  start=true;
	    	  ClipData data = ClipData.newPlainText("", "");
		        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		        view.startDrag(data, shadowBuilder, view, 0);
	    	  return true;
	    	  }
	      else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
	    	  
	    	  Log.v("motion","action up");
	    	  return true;
	      }
	      
	      else
	      {
	        return false;
	      }
	      
	      
	    }
	  }
	
	

	 
}



 





