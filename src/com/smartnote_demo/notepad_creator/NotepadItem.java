
package com.smartnote_demo.notepad_creator;

import com.example.smartnote_demo.R;
import com.example.smartnote_demo.R.id;
import com.example.smartnote_demo.R.layout;

import android.R.bool;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView.OnGestureListener;
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
import android.widget.FrameLayout.LayoutParams;
import android.view.View.OnTouchListener;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.DragEvent;
import android.graphics.drawable.Drawable;

public class NotepadItem extends FrameLayout {

	private ImageView mImage;
	private TextView mText;
	private int mIndex;
	
    private SharedPreferences sp;
    private Context mContext;
	// It's needed to find screen coordinates
	private Matrix mCIMatrix;
	private boolean mIsNotepad;
	
	public NotepadItem(Context context,String skin_name,int height,int resId,int index,boolean is_notepad) {
		
		super(context);
		mContext = context;
		String h = ""+height;
		mIsNotepad = is_notepad;
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
				//(200,300);
						//LayoutParams.MATCH_PARENT, 
						//LayoutParams.MATCH_PARENT);	
		params.weight = 1;
		
		this.setLayoutParams(params);
	 	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_skin, this, true);
		
		mImage = (ImageView)itemTemplate.findViewById(R.id.notepad_image);
		mImage.setImageResource(resId);
		mText = (TextView)itemTemplate.findViewById(R.id.notepad_skin_name);

		mText.setText("  ");
		
		mImage.setAlpha(0.5f);
		mIndex = index; 
		this.setOnClickListener(new NotepadClickListener());
				
	}	
	
	public String getName(){
		return mText.getText().toString();
	}	
	
	public void setIndex(int index) {
		this.mIndex = index;
	}

	public int getIndex() {
		return mIndex;
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

	public void setChosen(boolean chosen) {
		mImage.setAlpha(chosen?1.0f:0.5f);
	}
	

	
	
	private final class NotepadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//send notification to parent to unchose the item
			Log.v("click","item clicked");
			mImage.setAlpha(1.0f);
			NotepadCreator creator = (NotepadCreator)mContext;
			//open view for new notepad create	
			creator.setChosen(mIsNotepad, mIndex);
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



 





