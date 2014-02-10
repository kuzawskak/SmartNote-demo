package com.smartnote_demo.notepad_creator;

import com.example.smartnote_demo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * Class representing NotepadItem (notepad_template/site_template)
 *
 */
public class NotepadItem extends FrameLayout {

	private ImageView mImage;

	//mIndex represents index for parent
	private int mIndex;
	//mResId represents value in resources for bitmap
	private int mResId;
    private Context mContext;
	private boolean mIsNotepad;
	
	public NotepadItem(Context context,String skin_name,int height,int resId,int index,boolean is_notepad) {
		
		super(context);
		mContext = context;
		mIsNotepad = is_notepad;
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
	
		params.weight = 1;
		
		this.setLayoutParams(params);
	 	LayoutInflater inflater = LayoutInflater.from(context);
		View itemTemplate = inflater.inflate(R.layout.notepad_skin, this, true);
		
		mImage = (ImageView)itemTemplate.findViewById(R.id.notepad_image);
		mImage.setImageResource(resId);
			
		mImage.setAlpha(0.5f);
		mIndex = index;
		mResId = resId; 
		
		this.setOnClickListener(new NotepadClickListener());
				
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

	public void setChosen(boolean chosen) {
		mImage.setAlpha(chosen?1.0f:0.5f);
	}
	
	private final class NotepadClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			//send notification to parent to unchose the item
			mImage.setAlpha(1.0f);
			NotepadCreator creator = (NotepadCreator)mContext;
			//open view for new notepad create	
			creator.setChosen(mIsNotepad, mIndex,mResId);
		}
	
	}

}

