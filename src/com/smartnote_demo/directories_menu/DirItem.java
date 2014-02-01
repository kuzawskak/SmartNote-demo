
package com.smartnote_demo.directories_menu;

import java.io.File;
import java.io.FileInputStream;

import com.example.smartnote_demo.MainActivity;
import com.example.smartnote_demo.R;
import com.example.smartnote_demo.R.id;
import com.example.smartnote_demo.R.layout;
import com.smartnote_demo.carouselmenu.CarouselItem;

import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;
import android.view.View.OnTouchListener;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.DragEvent;
import android.graphics.drawable.Drawable;
import android.net.nsd.NsdManager.RegistrationListener;





public class DirItem extends FrameLayout {

	private ImageView mImage;
	private TextView mText;
	private String mName;
	private Context mContext;
	private int index;
	private float itemX;
	private float itemY;
	private boolean drawn;	

	// It's needed to find screen coordinates
	private Matrix mCIMatrix;
	
	public DirItem(Context context,String filename,int height,int skin_id, int site_id,String name) {
		
		super(context);
		mContext = context;
		mName = name;
		String h = ""+height;
		Log.d("heightinside",h);
		
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
				//(200,300);
						//LayoutParams.MATCH_PARENT, 
						//LayoutParams.MATCH_PARENT);	
		params.weight = 1;
		
		this.setLayoutParams(params);
	 	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_dir, this, true);

		mImage = (ImageView)itemTemplate.findViewById(R.id.item_image);
		Log.d("directories","creating dir item: "+skin_id);
		mImage.setImageResource(skin_id);
		mText = (TextView)itemTemplate.findViewById(R.id.grid_item_label);
		mText.setText(mName);
	
	   // this.setOnTouchListener(new MyTouchListener());
	    this.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "long click", 
						Toast.LENGTH_SHORT).show();	
				return false;
			}
		});
	   
				
	}	
	
	public void loadBitmap(String filename)
	{
		Bitmap bmForeground = null;
		try {
		File filePath = mContext.getFileStreamPath("desiredFilename.png");//here filename should be used
		FileInputStream fi = new FileInputStream(filePath);
		bmForeground =  BitmapFactory.decodeStream(fi);
		if(bmForeground==null) Log.e("smart","no bitmap");
		} catch (Exception ex) {
		Log.e("getThumbnail() on internal storage", ex.getMessage());
		}
	
			bmForeground=Bitmap.createScaledBitmap(bmForeground,200,300,true);
			mImage.setImageBitmap(bmForeground);
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

	private final class NotepadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//open view for new notepad create			
		}
	
	}
	
	private final class NotepadGestureListener implements OnLongClickListener{

		@Override
		public boolean onLongClick(View v) {
			// show context menu
			return false;
		}
		
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



 





