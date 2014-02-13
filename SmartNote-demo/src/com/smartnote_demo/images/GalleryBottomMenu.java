package com.smartnote_demo.images;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.app.Activity;


import com.example.smartnote_demo.ExitButtonListener;
import com.example.smartnote_demo.R;

public class GalleryBottomMenu extends LinearLayout {
			
	/** Camera request. */
	private static final int CAMERA_REQUEST = 1001;
	/** Gallery request. */
	private static final int GALLERY_REQUEST = 1002;
	/** Picture cropping request. */
	
	
			private ImageView TakePhotoButton;
			private ImageView ImportImagesButton;
			private Context mContext;
			public GalleryBottomMenu(Context context, AttributeSet attrs) {
				super(context, attrs);
				mContext = context;
				// TODO Auto-generated constructor stub
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT);
				
				this.setLayoutParams(params);
				
			  	LayoutInflater inflater = LayoutInflater.from(context);
				View itemTemplate = inflater.inflate(R.layout.gallery_bottom_buttons, this, true);
			  	
				TakePhotoButton = (ImageView)itemTemplate.findViewById(R.id.take_photo);

				
				TakePhotoButton.setOnClickListener(new ExitButtonListener());
				ImportImagesButton = (ImageView)itemTemplate.findViewById(R.id.import_photos);
				
				ImportImagesButton.setOnClickListener(new ExitButtonListener());
			}
			
			public GalleryBottomMenu(Context context) {
				super(context);
				mContext = context;
				LinearLayout.LayoutParams params = 
						new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, 
								LayoutParams.WRAP_CONTENT);
				
				this.setLayoutParams(params);
				
			  	LayoutInflater inflater = LayoutInflater.from(context);
				View itemTemplate = inflater.inflate(R.layout.gallery_bottom_buttons, this, true);
			  	
				TakePhotoButton = (ImageView)itemTemplate.findViewById(R.id.take_photo);

				
				TakePhotoButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						
						((Activity) mContext).startActivityForResult(cameraIntent, CAMERA_REQUEST);
						
					}
				});
				ImportImagesButton = (ImageView)itemTemplate.findViewById(R.id.import_photos);
				
				ImportImagesButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final Intent galleryIntent = new Intent(Intent.ACTION_CHOOSER,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						((Activity) mContext).startActivityForResult(galleryIntent, GALLERY_REQUEST);
						
					}
				});
			}	
	}


