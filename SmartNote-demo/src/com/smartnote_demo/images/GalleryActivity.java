package com.smartnote_demo.images;

import java.util.ArrayList;
import com.example.smartnote_demo.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Toast;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
 
public class GalleryActivity extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
	
	public static boolean only_preview = true;
	ImageView TakePhotoButton;
	ImageView ImportImagesButton;
	Context mContext;
	Gallery g;
	/** Camera request. */
	private static final int CAMERA_REQUEST = 1001;
	/** Gallery request. */
	private static final int GALLERY_REQUEST = 1002;
	/** Picture cropping request. */
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	mContext = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_activity);
 
        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
 
        g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);

        
		TakePhotoButton = (ImageView)findViewById(R.id.take_photo);


		TakePhotoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				
				((Activity) mContext).startActivityForResult(cameraIntent, CAMERA_REQUEST);
				
			}
		});
		
		ImportImagesButton = (ImageView)findViewById(R.id.import_photos);
		

		ImportImagesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final Intent galleryIntent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				((Activity) mContext).startActivityForResult(galleryIntent, GALLERY_REQUEST);
				
			}
		});
		
		
		if(!only_preview) {
		mSwitcher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//load to stamp
				Intent resultInt = new Intent();
				
				Uri u= ImportedImages.ThumbList.get(0);
				//String parsed = u.getPath();
				resultInt.putExtra("stamp_path",u);
				setResult(RESULT_OK, resultInt);

				finish();
			}
		});
		}
        
    }
 
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        mSwitcher.setImageURI(ImportedImages.ThumbList.get(position));
    }
 
    public void onNothingSelected(AdapterView<?> parent) {
    }
 
    public View makeView() {
        ImageView i = new ImageView(this);
        i.setBackgroundColor(0xFF000000);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return i;
    }
 
    private ImageSwitcher mSwitcher;
 
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {
            mContext = c;
        }
 
        public int getCount() {
        	if(ImportedImages.ThumbList!=null) {
            return (ImportedImages.ThumbList).size();
        	} else return 0;
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i = new ImageView(mContext);
            if(ImportedImages.ThumbList!=null) {
            i.setImageURI(ImportedImages.ThumbList.get(position));//position]);
            }
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            //i.setBackgroundResource(R.drawable.picture_frame);
            return i;
        }
 
 
    }
 
    
    
    
    
    @Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

		if ((requestCode == CAMERA_REQUEST || requestCode == GALLERY_REQUEST) && resultCode == RESULT_OK) {
			if (data == null) {
				Toast.makeText(this, "Something was wrong - no find photo try again in other resources",
						Toast.LENGTH_LONG).show();
				return;
			}else {
				//add selected item to list with thumbs and update adapter
				ImportedImages.ThumbList.add(data.getData());
				g.setAdapter(new ImageAdapter(this));
				
			}			
		}
	}

 
}