
package com.example.smartnote_demo;

import com.example.smartnote_demo.R;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
	
	public DirItem(Context context,String filename) {
		
		super(context);
		
		FrameLayout.LayoutParams params = 
				new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 
						LayoutParams.WRAP_CONTENT);
		
		this.setLayoutParams(params);
		
	  	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.item, this, true);
	  	
		
		mImage = (ImageView)itemTemplate.findViewById(R.id.item_image);
		mText = (TextView)itemTemplate.findViewById(R.id.item_text);
		mText.setText(filename);
		
	    this.setOnTouchListener(new MyTouchListener());
	
	    this.setOnDragListener(new MyDragListener());
				
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
	    public boolean onTouch(View view, MotionEvent motionEvent) {
	      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
	        ClipData data = ClipData.newPlainText("", "");
	        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	        view.startDrag(data, shadowBuilder, view, 0);
	       // view.setVisibility(View.INVISIBLE);
	        return true;
	      } else {
	        return false;
	      }
	    }
	  }
	
	

	  class MyDragListener implements OnDragListener {
	    Drawable enterShape = getResources().getDrawable(R.drawable.notepad);
	    Drawable normalShape = getResources().getDrawable(R.drawable.notepad);

	    @Override
	    public boolean onDrag(View v, DragEvent event) {
	      int action = event.getAction();
	      switch (event.getAction()) {
	      case DragEvent.ACTION_DRAG_STARTED:
	        // do nothing
	        break;
	      case DragEvent.ACTION_DRAG_ENTERED:
	        v.setBackgroundDrawable(enterShape);
	        break;
	      case DragEvent.ACTION_DRAG_EXITED:
	        v.setBackgroundDrawable(normalShape);
	        break;
	      case DragEvent.ACTION_DROP:
	        // Dropped, reassign View to ViewGroup
	        View view = (View) event.getLocalState();
	        LinearLayout owner = (LinearLayout) view.getParent();
	        owner.removeView(view);
	     // Get scroll view out of the way
	        owner.setVisibility(View.GONE);

	        // Put the child view into scrollview's parent view
	       // parentLayout.addView(scrollChildLayout);
	        owner.invalidate();
	       // LinearLayout container = (LinearLayout) v;
	        //container.addView(view);
	        //view.setVisibility(View.VISIBLE);
	        break;
	      case DragEvent.ACTION_DRAG_ENDED:
	        v.setBackgroundDrawable(normalShape);
	      default:
	        break;
	      }
	      return true;
	    }
	  }
}



 





