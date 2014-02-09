package com.smartnote_demo.share;


import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.samsung.spensdk.SCanvasView;
import com.smartnote_demo.notepad.CanvasActivity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class FacebookLogin extends AsyncTask<Void, Integer, Boolean> {

	private CanvasActivity mActivity;
	private SCanvasView	mSCanvas;
	private Facebook mFacebook;
	private SharedPreferences mPreferencesForFB;
	public FacebookLogin(SCanvasView scanvas,CanvasActivity activity,Facebook fb, SharedPreferences sp) {
		mSCanvas = scanvas;
		mActivity  = activity;
		mPreferencesForFB= sp;
		mFacebook = fb;
	}
	
	
	@Override
	protected Boolean doInBackground(Void... params) {
		
		Log.v("facebook","do in background");
		//login to facebook if the session is not valid
		String[] perm = {"publish_stream","photo_upload"};
		mFacebook.authorize(mActivity,perm, new DialogListener() {
			
			@Override
			public void onFacebookError(FacebookError e) {
				Log.v("facebook","do in background");
				// TODO Auto-generated method stub
				Toast.makeText(mActivity,"onFacebookError", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				Toast.makeText(mActivity,"onError", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onComplete(Bundle values) {
				Log.v("facebook","do in background");
				// TODO Auto-generated method stub
				Editor  editor = mPreferencesForFB.edit();
				editor.putString("access_token",mFacebook.getAccessToken());
				editor.putLong("acccess_expires", mFacebook.getAccessExpires());
				editor.commit();
				Toast.makeText(mActivity,"onComplete", Toast.LENGTH_SHORT).show();
			
						//start new async task
						//UiAsyncTask task = new UiAsyncTask();
						//task.doInBackground(null);
						UploadFacebook upload_async_task = new UploadFacebook(mSCanvas,mActivity, mFacebook);
						upload_async_task.doInBackground(null);
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}});

		return true;
	}

}



