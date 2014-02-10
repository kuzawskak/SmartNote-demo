package com.smartnote_demo.directories_menu;

import java.io.File;
import java.io.FileInputStream;
import com.example.smartnote_demo.R;

import com.smartnote_demo.notepad.CanvasActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * Class representing Notepad item (only the UI part - template, name)
 * to be shown in Directories activity
 *
 */
public class DirItem extends FrameLayout {

	private ImageView mImage;
	private TextView mText;
	private String mName;
	private Context mContext;
	private int index;
	private int mDatabaseId;
	
	public DirItem(Context context,String filename,int height,int skin_id, int site_id,String name,int id_in_database) {
		
		super(context);
		mContext = context;
		mDatabaseId = id_in_database;
		mName = name;
		
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT);

		params.weight = 1;
		
		this.setLayoutParams(params);
	 	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_dir, this, true);

		mImage = (ImageView)itemTemplate.findViewById(R.id.item_image);
		Log.d("directories","creating dir item: "+skin_id);
		mImage.setImageResource(skin_id);
		mText = (TextView)itemTemplate.findViewById(R.id.grid_item_label);
		mText.setText(mName);
	
		this.setOnClickListener(new NotepadClickListener());
		
	    this.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				//SHOW CONTEXT MENU TO DELETE
				//or change settings
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
	
	
	public int getDatabaseId() {
		return mDatabaseId;
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
	

	
	public void setImageBitmap(Bitmap bitmap){
		mImage.setImageBitmap(bitmap);
		
	}
	
	public void setText(String txt){
		mText.setText(txt);
	}
	
	private final class NotepadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {		
				//load notepad intent dedicated for the selected one
				Intent notepad_intent = new Intent(v.getContext(),CanvasActivity.class);
				CanvasActivity.notepad_name=mName;
				v.getContext().startActivity(notepad_intent);
		}	
	}
	
	 
}



 





