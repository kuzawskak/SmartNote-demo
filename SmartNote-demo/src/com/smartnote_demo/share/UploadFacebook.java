package com.smartnote_demo.share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.samsung.spensdk.SCanvasView;
import com.smartnote_demo.notepad.CanvasActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class UploadFacebook extends AsyncTask<Void,Long,Boolean>{

	
	private SCanvasView		mSCanvas;
	private Facebook mFacebook;
	private CanvasActivity mSCanvasActivity;
	
	public UploadFacebook(SCanvasView canvas,CanvasActivity activity,Facebook fb) {
		mSCanvas = canvas;
		mFacebook = fb;
		mSCanvasActivity  = activity;
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		//get actual bitmap from canvas and share it (without saving on device!)
		//convert it to ByteArray
		Bitmap bmCanvas = mSCanvas.getCanvasBitmap(false);  
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    bmCanvas.compress(Bitmap.CompressFormat.JPEG,100,bos);
	    byte[] photoBytes = bos.toByteArray();
	    
	    //set parameters for POST action
	    String albumId = "me";
	    Bundle parameters = new Bundle();
	    parameters.putByteArray("picture", photoBytes);
	     
	    //start JSON request to upload pictures on logged user's gallery
	    try {
	        String resp = mFacebook.request(albumId + "/photos", parameters, "POST");
	        JSONObject json = Util.parseJson(resp);
	    } catch ( IOException e ) {
	    	Toast.makeText(mSCanvasActivity,"IOException", Toast.LENGTH_SHORT).show();
	    	Log.v("facebook","IOException");
	    } catch ( FacebookError e ) {
	    	Toast.makeText(mSCanvasActivity,"Facebook Error", Toast.LENGTH_SHORT).show();
	    	Log.v("facebook","FacebookError");
	    } catch ( JSONException e ) {
	    	Toast.makeText(mSCanvasActivity,"JSONExceptionl", Toast.LENGTH_SHORT).show();
	    	Log.v("facebook","JSONException");
	    }
	    Toast.makeText(mSCanvasActivity,"Image uploaded to your gallery", Toast.LENGTH_SHORT).show();
		return null;
	}

}
