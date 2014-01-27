package com.smartnote_demo.notepad_creator;

import com.example.smartnote_demo.R;
import com.smartnote_demo.directories_menu.DirItem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


public class NotepadCreator extends Activity {
	
	private Context mContext;

	private Button mCreateButton;
	private Button mCancelButton;
	private EditText mEditText;
	private int chosen_notepadskin_no = 0;
	private int chosen_siteskin_no = 0;
	private LinearLayout NotepadSkins;
	private LinearLayout SiteSkins;
	private int[] NotepadSkinIds={
			R.drawable.skin1,
			R.drawable.skin2,
			R.drawable.skin3,
			R.drawable.skin4,
			R.drawable.skin5,
			R.drawable.skin6,
		
	};
	
	private int[] SiteSkinIds={
			R.drawable.page1,
			R.drawable.page2,
			R.drawable.page3,
			R.drawable.page4,
			R.drawable.page5,
			
	};
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	        setContentView(R.layout.notepad_creator);
	        mContext = this;
	        mEditText = (EditText)findViewById(R.id.editText1);
	        SharedPreferences sp  = getSharedPreferences("NEW_NOTEPAD", 0);
	        SharedPreferences.Editor editor = sp.edit();
	        editor.putInt("template_id", 0);
	        editor.putInt("site_id", 0);
	        editor.putString("name", "");
	        editor.commit();
	        	        
	        NotepadSkins = (LinearLayout)findViewById(R.id.NotepadSkinsContainer);
	        NotepadSkins.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					Log.v("click","creator clicked");
					// TODO Auto-generated method stub
					
				}
			});
	        SiteSkins = (LinearLayout)findViewById(R.id.NotepadSitesContainer);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			
			//NotepadSkins.setPadding(10, 10, 10, 10);
			//SiteSkins.setPadding(10, 10, 10, 10);
			for(int i=0;i<NotepadSkinIds.length;i++)
			{		
				NotepadSkins.addView(createNotePad((int)(height*0.9),NotepadSkinIds[i],i,true));
				String h= ""+height;
				Log.d("height",h);
			}
			
			for(int i=0;i<SiteSkinIds.length;i++)
			{	
				SiteSkins.addView(createNotePad((int)(height*0.9),SiteSkinIds[i],i,false));
				String h= ""+height;
				Log.d("height",h);
			}
				
			/*setting first templates as chose by default*/
			NotepadItem first_item  = (NotepadItem)NotepadSkins.getChildAt(0);
			first_item.setChosen(true);
			first_item  = (NotepadItem)SiteSkins.getChildAt(0);
			first_item.setChosen(true);
			
			mEditText.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					SharedPreferences sp  = getSharedPreferences("NEW_NOTEPAD", 0);
			        SharedPreferences.Editor editor = sp.edit();
			        editor.putString("name", s.toString());
			        editor.commit();
				}
			});
	
	}
	public NotepadItem createNotePad(int height,int id,int i,boolean is_notepad){
		NotepadItem notePadView = new NotepadItem(this,"skin",height,id,i,is_notepad);

		return notePadView;		
	}
	
	public void setChosen(boolean notepad, int number) {
		if(notepad && number!=chosen_notepadskin_no) {
			   SharedPreferences sp  = getSharedPreferences("NEW_NOTEPAD", 0);
		        SharedPreferences.Editor editor = sp.edit();
		        editor.putInt("template_id", number);
		        editor.commit();
			NotepadItem item = (NotepadItem)NotepadSkins.getChildAt(chosen_notepadskin_no);
			item.setChosen(false);
			chosen_notepadskin_no = number;
		}
		else if (!notepad && number!=chosen_siteskin_no) {
			   SharedPreferences sp  = getSharedPreferences("NEW_NOTEPAD", 0);
		        SharedPreferences.Editor editor = sp.edit();
		        editor.putInt("site_id", number);
		        editor.commit();
			NotepadItem item = (NotepadItem)SiteSkins.getChildAt(chosen_siteskin_no);
			item.setChosen(false);
			chosen_siteskin_no = number;
		}
	}
	
}

